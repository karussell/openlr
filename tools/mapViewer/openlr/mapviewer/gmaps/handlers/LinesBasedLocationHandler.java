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

import java.util.Collection;
import java.util.List;

import openlr.location.Location;
import openlr.map.GeoCoordinates;
import openlr.map.Line;
import openlr.mapviewer.gmaps.scriptlets.LocationLinesFromShape;

/**
 * The handler for locations that provide lines.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
abstract class LinesBasedLocationHandler implements LocationHandler {

    /**
     * The processed location object.
     */
    private final Location loc;

    /**
     * The HTML code used to draw the lines.
     */
    private final String lineColor;

    /**
     * Creates a new line location handler.
     * 
     * @param location
     *            The location to draw.
     * @param lineColorHtml
     *            The HTML code used to draw the lines.
     */
    public LinesBasedLocationHandler(final Location location,
            final String lineColorHtml) {
        loc = location;
        lineColor = lineColorHtml;
    }

    @Override
    public final String getDrawingCommands() {

        List<Line> lines = getLocationLines();

        StringBuffer scriptCode = new StringBuffer();

        scriptCode.append(new LocationLinesFromShape("locationPath", lines,
                lineColor, loc.getPositiveOffset(), loc.getNegativeOffset())
                .toString());

        addAdditionalCommands(scriptCode);

        return scriptCode.toString();
    }

    /**
     * Hook for sub-classes to add additional code to the generated JavaScript.
     * The default implementation is empty.
     * 
     * @param scriptCode
     *            The script code so far
     */
    protected void addAdditionalCommands(
            @SuppressWarnings("unused") final StringBuffer scriptCode) {
    }

    /**
     * Delivers the lines that relate to the location.
     * 
     * @return The location lines
     */
    protected abstract List<Line> getLocationLines();

    /**
     * Hook for sub-classes to provide additional geo-coordinates to be
     * considered for calculating the locations' bounding box besides the shape
     * points of the LRP path.
     * 
     * @param additions
     *            The list to add the coordinates to. This will be an empty
     *            list.
     */
    protected void putAdditionalCoordinatesForBoundingBox(
            @SuppressWarnings("unused") final Collection<GeoCoordinates> additions) {
    }

    /**
     * Delivers the reference to the location object
     * 
     * @return The location object
     */
    final Location getLocation() {
        return loc;
    }
}
