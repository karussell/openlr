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
package openlr.geomap;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * This class maps AWT mouse listeners to geo map map listeners that provide
 * spatial information besides the pure pixel coordinate on the map pane. This
 * is an internal functionality that switches between the published API
 * {@link MapMouseListener} and the internal implementation based on AWT.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
class MapMouseListenerAdapter implements MouseListener, MouseMotionListener {

    /**
     * The encapsulated map graphics listener that gets the event information.
     */
    private final MapMouseListener delegate;

    /**
     * The map graphics object.
     */
    private final JMapPane mapPanel;

    /**
     * The Constructor.
     * 
     * @param mapPane
     *            The assigned map graphics pane.
     * @param listener
     *            The encapsulated map graphics listener that gets the event
     *            information.
     */
    public MapMouseListenerAdapter(final JMapPane mapPane,
            final MapMouseListener listener) {

        delegate = listener;
        this.mapPanel = mapPane;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseMoved(final MouseEvent ev) {
        delegate.mouseMoved(new MapMouseEvent(ev, mapPanel));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseExited(final MouseEvent ev) {
        MapMouseEvent event = new MapMouseEvent(ev, mapPanel);
        delegate.mouseExited(event);
    }

    @Override
    public final void mouseClicked(final MouseEvent ev) {
        MapMouseEvent event = new MapMouseEvent(ev, mapPanel);
        delegate.mouseClicked(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseEntered(final MouseEvent ev) {
        MapMouseEvent event = new MapMouseEvent(ev, mapPanel);
        delegate.mouseEntered(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mousePressed(final MouseEvent ev) {
        MapMouseEvent event = new MapMouseEvent(ev, mapPanel);
        delegate.mousePressed(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseReleased(final MouseEvent ev) {
        MapMouseEvent event = new MapMouseEvent(ev, mapPanel);
        delegate.mouseReleased(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseDragged(final MouseEvent ev) {
        MapMouseEvent event = new MapMouseEvent(ev, mapPanel);
        delegate.mouseDragged(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return super.toString() + "-> " + delegate.toString();
    }

    /**
     * Delivers the wrapped {@link MapMouseListener} this class delegates to
     * 
     * @return The wrapped {@link MapMouseListener}
     */
    MapMouseListener getDelegate() {
        return delegate;
    }
}
