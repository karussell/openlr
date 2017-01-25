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
package openlr.mapviewer.gui.utils;

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import openlr.mapviewer.MapViewer;
import openlr.mapviewer.properties.MapViewerPropertiesObserver;

/**
 * This class manages registration and un-registration of a
 * {@link MapViewerPropertiesObserver} depending on the life-cycle of a
 * {@link javax.swing.JComponent JComponent}. It implements the
 * {@link AncestorListener} interface and should be attached as an ancestor
 * listener to the {@link javax.swing.JComponent JComponent} to track for
 * activation and deactivation. It registers the given observer when the
 * jComponent is created and unregisters the observer from the observers list
 * when the component is removed from the UI.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public final class VisibilityDependentPropertiesObservationListener implements
        AncestorListener {

    /**
     * The observer instance to manage
     */
    private final MapViewerPropertiesObserver obs;

    /**
     * Creates a new instance
     * 
     * @param observer
     *            The observer instance to manage
     */
    public VisibilityDependentPropertiesObservationListener(
            final MapViewerPropertiesObserver observer) {
        this.obs = observer;
    }

    /**
     * Adds the observer to the list of properties observers
     * 
     * @param event
     *            The ancestor event
     */
    @Override
    public synchronized void ancestorAdded(final AncestorEvent event) {
        MapViewer.PROPERTIES.addObserver(obs);
    }

    /**
     * Removes the observer from the list of properties observers
     * 
     * @param event
     *            The ancestor event
     */
    @Override
    public synchronized void ancestorRemoved(final AncestorEvent event) {
        MapViewer.PROPERTIES.removeObserver(obs);
    }

    /**
     * Does nothing
     * 
     * @param event
     *            is ignored
     */
    @Override
    public void ancestorMoved(final AncestorEvent event) {
    }
}
