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
package openlr.mapviewer.gui.panels.toolbar;

import javax.swing.AbstractButton;

import openlr.mapviewer.MapsHolder;
import openlr.mapviewer.MapsHolder.MapIndex;
import openlr.mapviewer.gui.panels.MapTool.CloseAction;

/**
 * This class implements an {@link CloseAction} for
 * {@link openlr.mapviewer.gui.panels.MapTool MapTool}s that de-toggles the
 * related tool-bar button if the removed tool relates to the currently active
 * map.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
final class DeToggleButtonOnToolCloseAction implements CloseAction {

    /**
     * The tool instance
     */
    private final MapIndex relevantIndex;

    /**
     * The related button
     */
    private final AbstractButton button;

    /**
     * The maps holder
     */
    private final MapsHolder mHolder;

    /**
     * Creates a new instance.
     * 
     * @param mapsHolder
     *            The maps holder
     * @param relatedIndex
     *            The related {@link openlr.mapviewer.gui.panels.MapTool
     *            MapTool}
     * @param relatedButton
     *            The related tool-bar button
     */
    DeToggleButtonOnToolCloseAction(final MapsHolder mapsHolder,
            final MapIndex relatedIndex, final AbstractButton relatedButton) {
        relevantIndex = relatedIndex;
        button = relatedButton;
        mHolder = mapsHolder;
    }

    /**
     * De-selects the related tool-bar button if the closed tool relates to the
     * currently active map.
     */
    @Override
    public void close() {

        if (relevantIndex == mHolder.getMapActive()) {

            button.setSelected(false);
        }
    }
}
