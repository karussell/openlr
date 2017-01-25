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
package openlr.mapviewer.gmaps.handlers;

import static openlr.mapviewer.gmaps.scriptlets.ScriptConstants.NEW_LINE;
import openlr.location.CircleLocation;
import openlr.map.GeoCoordinates;
import openlr.mapviewer.gmaps.GoogleMapsStarter;

/**
 * This class creates the JavaScript code to draw a circle location.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
class CircleLocationHandler implements LocationHandler {

    /**
     * The processed location.
     */
    private CircleLocation loc;

    /**
     * The line color
     */
    private final String color;

    /**
     * Performs initializing.
     * 
     * @param location
     *            The processed location.
     * @param lineColor
     *            The color of the circle lines
     */
    public CircleLocationHandler(final CircleLocation location,
            final String lineColor) {
        loc = location;
        color = lineColor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDrawingCommands() {

        StringBuilder script = new StringBuilder();
        GeoCoordinates center = loc.getCenterPoint();

        script.append("var circleCenter = new google.maps.LatLng(")
                .append(center.getLatitudeDeg()).append(", ")
                .append(center.getLongitudeDeg()).append(");");

        script.append("var circleOptions = { ").append(NEW_LINE);
        script.append("strokeColor: \"").append(color).append("\",")
                .append(NEW_LINE);
        script.append("strokeOpacity: 0.5,").append(NEW_LINE);
        script.append("strokeWeight: 4,").append(NEW_LINE);
        script.append(" fillOpacity: 0,").append(NEW_LINE);
        script.append("map: ").append(GoogleMapsStarter.IDENTIFIER_MAP_OBJECT)
                .append(",").append(NEW_LINE);
        script.append("center: circleCenter,").append(NEW_LINE);
        script.append("radius: ").append(loc.getRadius()).append(NEW_LINE);
        script.append("};").append(NEW_LINE);
        script.append("var cityCircle = new google.maps.Circle(circleOptions);");

        return script.toString();
    }
}
