/**
* Licensed to the TomTom International B.V. under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  TomTom International B.V.
* licenses this file to you under the Apache License, 
* Version 2.0 (the "License"); you may not use this file except 
* in compliance with the License.  You may obtain a copy of the License at
* 
* http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

/**
 *  Copyright (C) 2009-2012 TomTom International B.V.
 *
 *   TomTom (Legal Department)
 *   Email: legal@tomtom.com
 *
 *   TomTom (Technical contact)
 *   Email: openlr@tomtom.com
 *
 *   Address: TomTom International B.V., Oosterdoksstraat 114, 1011DK Amsterdam,
 *   the Netherlands
 */
package openlr.mapviewer.gui.panels;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import openlr.geomap.JMapPane;
import openlr.geomap.MapMouseListener;
import openlr.geomap.tools.CursorTool;
import openlr.mapviewer.gui.panels.MapTool.CloseAction;

/**
 * This class channels all the requests to set and remove tools to the map pane.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class MapToolManager {

    /**
     * A reference to the pane used for drawing the map graphics.
     */
    private final JMapPane mapPane;

    /**
     * The list of currently active UI tools
     */
    private final Map<Class<? extends MapTool>, MapTool> activeTools = new HashMap<Class<? extends MapTool>, MapTool>();

    /**
     * Creates a new instance.
     * 
     * @param pane
     *            The map pane that is managed by this instance.
     */
    MapToolManager(final JMapPane pane) {
        mapPane = pane;
    }

    /**
     * Adds a map mouse listener that gets informed about mouse events on the
     * map graphics.
     * 
     * @param listener
     *            The map mouse listener to add
     */
    public final void addMapMouseListener(final MapMouseListener listener) {

        mapPane.addMapMouseListener(listener);

    }

    /**
     * Removes the given instance of {@link MapMouseListener} from the list of
     * active listeners.
     * 
     * @param listener
     *            The listener instance to remove
     */
    public final void removeMapMouseListener(final MapMouseListener listener) {

        mapPane.removeMapMouseListener(listener);

    }

    /**
     * Sets the cursor tool and replaces the former one. The former cursor tool
     * will get called by its {@link CursorTool#deactivate()} method if the new tool
     * instance is not equal to the former one.
     * 
     * @param newTool
     *            The cursor tool to set.
     */
    public final void setCursorTool(final CursorTool newTool) {

        mapPane.setCursorTool(newTool);
    }

    /**
     * Delivers the currently active cursor tool, can be {@code null}.
     * 
     * @return The active cursor tool
     */
    public final CursorTool getActiveCursorTool() {
        return mapPane.getActiveCursorTool();
    }

    /**
     * Registers a new active UI tool for the map associated with this tool
     * manager. If there is already a tool of the same Java class registered a
     * RuntimeException will be thrown. Please note that it is possible to check
     * for an already registered tool via {@link #getActiveUITool(Class)}.
     * 
     * @param tool
     *            The tool to register
     */
    public final void addUITool(final MapTool tool) {

        Class<? extends MapTool> clazz = tool.getClass();
        if (!activeTools.containsKey(clazz)) {
            activeTools.put(clazz, tool);

            tool.addCloseAction(new UnregisterToolOnCloseAction(clazz));
        } else {
            throw new IllegalStateException(
                    "There is already a tool of this class registered: "
                            + clazz);
        }
    }

    /**
     * Unregisters the specified UI tool for the map associated with this tool
     * manager. Calls {@link MapTool#close()} on the removed tool.
     * 
     * @param <T>
     *            The concrete class of the expected tool
     * @param clazz
     *            The tool to unregister
     * @return The removed instance of there was one registered for this class,
     *         otherwise {@code null}
     */
    public final <T extends MapTool> T removeUITool(final Class<T> clazz) {
        return removeUITool(clazz, true);
    }

    /**
     * Internal version of {@link #removeUITool(Class)} that allows to suppress
     * the call to {@link MapTool#close()}. This is used to remove the tool
     * inside a closing process without calling {@link MapTool#close()} again.
     * 
     * @param <T>
     *            The concrete class of the expected tool
     * @param clazz
     *            The tool to unregister
     * @param callClose
     *            If {@code true} the call to {@link MapTool#close()} is
     *            done, otherwise suppressed.
     * @return The removed instance of there was one registered for this class,
     *         otherwise {@code null}
     */
    private <T extends MapTool> T removeUITool(final Class<T> clazz,
            final boolean callClose) {
        MapTool tool = activeTools.remove(clazz);
        if (tool != null && clazz.isInstance(tool)) {
            if (callClose) {
                tool.close();
            }
            return clazz.cast(tool);
        }
        return null;
    }

    /**
     * Delivers the yet registered tool of this class or {@code null} if no tool
     * of this type is currently registered to the related map.
     * 
     * @param <T>
     *            The concrete class of the expected tool
     * @param clazz
     *            The concrete class of the toll to retrieve.
     * @return The registered tool instance or {@code null}
     */
    public final <T extends MapTool> T getActiveUITool(final Class<T> clazz) {
        MapTool tool = activeTools.get(clazz);
        if (tool != null && clazz.isInstance(tool)) {
            return clazz.cast(tool);
        }
        return null;
    }

    /**
     * Delivers the currently active UI tools
     * 
     * @return The active cursor tool
     */
    public final synchronized Collection<MapTool> getActiveUITools() {
        return activeTools.values();
    }

    /**
     * This class implements a {@link CloseAction} that unregisters the tool
     * from the tool manager the time it is closed
     */
    private final class UnregisterToolOnCloseAction implements CloseAction {
        /**
         * 
         */
        private final Class<? extends MapTool> toolClass;

        /**
         * Creates a new instance of the job
         * 
         * @param clazz
         *            The tool class to unregister
         */
        private UnregisterToolOnCloseAction(final Class<? extends MapTool> clazz) {
            this.toolClass = clazz;
        }

        @Override
        public void close() {
            // set suppress call to close() because running this CloseAction
            // indicates that we are actually inside a close() processing
            removeUITool(toolClass, false);
        }
    }
}
