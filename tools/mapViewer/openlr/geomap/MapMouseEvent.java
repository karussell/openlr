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
import java.awt.geom.Point2D;

import openlr.geomap.transform.JMapTransformMercator;
import openlr.map.GeoCoordinates;
import openlr.map.GeoCoordinatesImpl;

/**
 * 
 * This class defines a mouse event on the map graphics.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class MapMouseEvent {
    
    /**
     * Constants of the mouse buttons possibly involved in this event.
     */
    public enum Button {
        /**
         * No mouse button involved.
         */
        NO_BUTTON, 
        
        /**
         * Mouse button #1 involved.
         */
        BUTTON_1, 
        
        /**
         * Mouse button #2 involved.
         */        
        BUTTON_2, 
        
        /**
         * Mouse button #3 involved.
         */        
        BUTTON_3
    }

    /** The map pane. */
    private final JMapPane mapPane;

    /**
     * The wrapped AWT event
     */
    private final MouseEvent baseEvent;

    /**
     * The geo coordinate that relates to the mouse event on the map, lazy initialized
     */
    private GeoCoordinates coordinate;

    /**
     * Creates a new instance
     * @param awtEvent The wrapped AWT event
     * @param pane The map pane
     */
    MapMouseEvent(final MouseEvent awtEvent, final JMapPane pane) {
        super();
        this.baseEvent = awtEvent;
        this.mapPane = pane;
    }

    /**
     * Delivers the pixel x coordinate on the map pane
     * 
     * @return The pixel x coordinate
     */
    public final int getPanelX() {
        return baseEvent.getX();
    }

    /**
     * Delivers the pixel y coordinate on the map pane. <strong>The coordinate
     * source of the pixel coordinate system is the lower left!</strong>
     * 
     * @return The pixel y coordinate
     */
    public final int getPanelY() {
        // transform from the top aligned AWT coordinate system to the Geomap
        // default with coordinates source down left
        return mapPane.getHeight() - baseEvent.getY();
    }

    /**
     * Delivers the geo coordinate that relates to the mouse event on the map
     * @return The geo coordinate
     */
    public final GeoCoordinates getCoordinate() {

        if (coordinate == null) {
            JMapTransformMercator transform = mapPane.getTransform();
            Point2D.Double ul = transform.getGeoCoordinate(getPanelX(),
                    getPanelY());

            coordinate = GeoCoordinatesImpl.newGeoCoordinatesUnchecked(ul.x,
                    ul.y);
        }

        return coordinate;
    }
    
    /**
     * Delivers the map pane that is the source of this event.
     * @return The related map pane
     */
    public final JMapPane getMapPane() {
        return mapPane;
    }

    /**
     * Returns the number of mouse clicks associated with this event.
     * 
     * @return integer value for the number of clicks
     */
    public final int getClickCount() {
        return baseEvent.getClickCount();
    }

    /**
     * Returns which, if any, of the mouse buttons has changed state.
     * 
     * @return one of the following enums: {@link Button#NO_BUTTON},
     *         {@link Button#BUTTON_1}, {@link Button#BUTTON_2},
     *         {@link Button#BUTTON_3}.
     */
    public final Button getButton() {
        switch (baseEvent.getButton()) {
        case MouseEvent.BUTTON1:
            return Button.BUTTON_1;
        case MouseEvent.BUTTON2:
            return Button.BUTTON_2;
        case MouseEvent.BUTTON3:
            return Button.BUTTON_3;
        default:
            return Button.NO_BUTTON;
        }
    }

}
