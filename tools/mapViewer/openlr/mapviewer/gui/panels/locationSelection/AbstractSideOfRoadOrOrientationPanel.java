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
package openlr.mapviewer.gui.panels.locationSelection;

import javax.swing.JPanel;

import openlr.LocationType;
import openlr.mapviewer.location.LocationHolder;

/**
 * This is a base class for the {@link OrientationPanel} and
 * {@link SideOfRoadPanel} that provides common functionality.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
abstract class AbstractSideOfRoadOrOrientationPanel extends JPanel {

    /**
     * default serial id
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * A reference to the currently assigned location holder providing the state
     * of the selection
     */
    protected LocationHolder currentState;

    /**
     * Instantiates a new orientation panel.
     * 
     * @param lh
     *            the location holder
     */
    public AbstractSideOfRoadOrOrientationPanel(final LocationHolder lh) {
        setState(lh);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            setState(currentState);

        } else {
            unset(false);
        }
    }

    /**
     * Resets all the UI sub-elements to the state it has when it contains no
     * location data.
     * 
     * @param clear
     *            If {@code true} the current selection shall be cleared to, not
     *            just set disabled
     */
    protected abstract void unset(boolean clear);

    /**
     * Applies to the orientation setting in the given location holder.
     * 
     * @param locHolder
     *            the location holder providing the orientation setting
     */
    public final void setState(final LocationHolder locHolder) {

        currentState = locHolder;

        boolean isRelevantLocationType = isRelevantLocationType(locHolder);
        if (isRelevantLocationType) {

            handleStateImpl(locHolder);
        } else {
            unset(true);
        }
    }

    /**
     * Template method. This method is called from
     * {@link #setState(LocationHolder)} after it is checked whether this
     * location holder contains the right location type for side of road or
     * orientation attributes.
     * 
     * @param locHolder
     *            The processed location holder
     */
    protected abstract void handleStateImpl(final LocationHolder locHolder);

    /**
     * Checks whether this location holder contains the right location type for
     * side of road or orientation attributes.
     * 
     * @param locHolder
     *            The processed location holder
     * @return {@code true} if it is the proper location type
     */
    private boolean isRelevantLocationType(final LocationHolder locHolder) {
        LocationType locationType = locHolder.getLocationType();
        return locationType == LocationType.POI_WITH_ACCESS_POINT
                || locationType == LocationType.POINT_ALONG_LINE;
    }
}
