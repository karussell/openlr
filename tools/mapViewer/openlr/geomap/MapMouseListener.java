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

/**
 * This class provides an interface for listeners that get informed about
 * mouse events on the map graphics.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public interface MapMouseListener {

    /**
     * Respond to a mouse click event received from the map pane
     *
     * @param ev the mouse event
     */
    void mouseClicked(MapMouseEvent ev);

    /**
     * Respond to a mouse entered event received from the map pane
     *
     * @param ev the mouse event
     */
    void mouseEntered(MapMouseEvent ev);

    /**
     * Respond to a mouse exited event received from the map pane
     *
     * @param ev the mouse event
     */
    void mouseExited(MapMouseEvent ev);

    /**
     * Respond to a mouse movement event received from the map pane
     *
     * @param ev the mouse event
     */
    void mouseMoved(MapMouseEvent ev);

    /**
     * Respond to a mouse button press event received from the map pane
     *
     * @param ev the mouse event
     */
    void mousePressed(MapMouseEvent ev);

    /**
     * Respond to a mouse button release event received from the map pane
     *
     * @param ev the mouse event
     */
    void mouseReleased(MapMouseEvent ev); 
    
    /**
     * Respond to a mouse dragged event received from the map pane
     *
     * @param ev the mouse event
     */
    void mouseDragged(MapMouseEvent ev);    
}
