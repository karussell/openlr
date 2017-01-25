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

import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;

/**
 * This abstract base class defines classes that implement tools that refer to a
 * displayed map. Multiple of these tools can be active in parallel on one map
 * instance. However, they have to be of different class.<br>
 * The base class provides the major UI frame for its implementing sub-class.
 * Sub-classes can retrieve it for adding their UI elements via
 * {@link #getDialogFrame()}. The base class implements the job to set the
 * dialog invisible when the component hidden event of the main frame is
 * executed. Further necessary actions when the tool is deactivated can be
 * registered via {@link #addCloseAction(CloseAction)}.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public abstract class MapTool {

    /**
     * The list of close tasks.
     */
    private final Set<CloseAction> closeTasks = new HashSet<CloseAction>();

    /**
     * The frame of the tool UI
     */
    private final JFrame dialogFrame = new JFrame();

    /**
     * Creates a new instance
     */
    public MapTool() {

        dialogFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // the standard close action
        addCloseAction(new HideDialogCloseAction(dialogFrame));

        // this window listener ensures the closing actions are called if the 
        // frame is closed via dispose
        dialogFrame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(final WindowEvent e) {
                close();
            }
        });
        
        // this window listener ensures the closing actions are called if the 
        // frame is closed via setVisible(false)
        dialogFrame.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentHidden(final ComponentEvent e) {
                close();
            } 
        });
    }

    /**
     * Adds an close task to this tool implementation.
     * 
     * @param task
     *            The task implementation.
     */
    public final void addCloseAction(final CloseAction task) {
        closeTasks.add(task);
    }

    /**
     * Removes an close task instance from the collection of close actions.
     * 
     * @param task
     *            The task instance to remove.
     */
    public final void removeCloseAction(final CloseAction task) {
        closeTasks.remove(task);
    }

    /**
     * Delivers the main dialog frame that contains the UI elements of this tool
     * 
     * @return The major AWT Frame of this tool
     */
    protected final JFrame getDialogFrame() {
        return dialogFrame;
    }

    /**
     * Executes all registered closing actions when this tool is deactivated.
     */
    public final void close() {
        for (CloseAction task : closeTasks) {
            task.close();
        }
    }


    /**
     * Defines the interface for unset task implementations.
     */
    public interface CloseAction {

        /**
         * This method is called for this task by the assigned cursor tool while
         * it is closed.
         */
        void close();
    }
    
    /**
     * Implements a the closing action for a {@link MapTool} that sets the
     * related dialog invisible.
     */
    private static final class HideDialogCloseAction implements CloseAction {

        /**
         * The related window of the tool.
         */
        private final Window dialog;

        /**
         * Creates a new instance
         * 
         * @param toolWindow
         *            The related window to set invisible
         */
        HideDialogCloseAction(final Window toolWindow) {
            this.dialog = toolWindow;
        }

        /**
         * Sets the specified window invisible
         */
        @Override
        public void close() {
            if (dialog.isVisible()) {
                dialog.dispose();
            }
        }
    }
}
