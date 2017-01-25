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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import openlr.location.Location;
import openlr.map.GeoCoordinates;
import openlr.map.InvalidMapDataException;
import openlr.map.utils.GeometryUtils;
import openlr.mapviewer.gmaps.scriptlets.Polyline;
import openlr.mapviewer.gmaps.scriptlets.PolylineOptions;

/**
 * This class creates the JavaScript code to draw a grid location.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class GridLocationHandler implements LocationHandler {

    /**
     * The index of the lower left corner in the sequence of corner points of
     * the grid
     */
    private static final int INDEX_LOWER_LEFT = 0;
    /**
     * The index of the upper lower right in the sequence of corner points of
     * the grid
     */
    private static final int INDEX_LOWER_RIGHT = 1;
    /**
     * The index of the upper left corner in the sequence of corner points of
     * the grid
     */
    private static final int INDEX_UPPER_LEFT = 3;

    /**
     * The location to draw.
     */
    private final Location loc;

    /**
     * The color to draw the grid in
     */
    private final String color;

    /**
     * Creates a new instance.
     * 
     * @param location
     *            The location to draw
     * @param lineColor
     *            The color to draw the lines
     */
    public GridLocationHandler(final Location location, final String lineColor) {
        loc = location;
        color = lineColor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getDrawingCommands() {

        StringBuilder script = new StringBuilder();

        List<GeoCoordinates> corners = new ArrayList<GeoCoordinates>(
                loc.getCornerPoints());

        int columns = loc.getNumberOfColumns();
        int rows = loc.getNumberOfRows();

        GeoCoordinates lowerLeft = corners.get(INDEX_LOWER_LEFT);
        GeoCoordinates lowerRight = corners.get(INDEX_LOWER_RIGHT);
        GeoCoordinates upperLeft = corners.get(INDEX_UPPER_LEFT);

        PolylineOptions polylineOptions = new PolylineOptions("lineOptions",
                color, PolylineOptions.STROKE_OPACITY_DEFAULT,
                PolylineOptions.STROKE_WEIGHT_DEFAULT);
        
        script.append(polylineOptions);

        // draw vertical lines
        try {
            script.append(addLines("vert", lowerLeft, upperLeft, rows,
                    lowerRight, columns + 1, polylineOptions));
            // draw horizontal lines
            script.append(addLines("hor", lowerLeft, lowerRight, columns,
                    upperLeft, rows + 1, polylineOptions));
        } catch (InvalidMapDataException e) {
            throw new IllegalStateException(
                    "Could not calculate lines for drawing grid cells", e);
        }

        return script.toString();
    }

    /**
     * Draws parallel lines. The base line is defined between
     * {@code startPoint} and {@code directionPoint}. Its length is
     * multiplied by the {@code lengthFactor}. The resulting line is
     * parallel translated into {@code intervalPoint}. The same translation
     * is repeated in excess of the intervalPoint so that in the end
     * {@code numberLines} lines are created.
     * 
     * @param variablePrefix
     *            A prefix for variable name generated for the required
     *            JavaScript elements
     * @param startPoint
     *            The start point
     * @param directionPoint
     *            The point defining the base line together with the start point
     * @param lengthFactor
     *            The factor that multiplies the base line in excess of the
     *            direction point
     * @param intervalPoint
     *            The point describing the interval of drawing parallel lines.
     *            The interval width is the distance between start and interval
     *            point.
     * @param numberLines
     *            The number of lines to create
     * @param polylineOptions
     *            The options drawing the grid lines
     * @return The list of KML features describing the generated lines
     * @throws InvalidMapDataException
     *             If some of the geo-coordinates calculated are out of valid
     *             bounds.
     */
    private String addLines(final String variablePrefix,
            final GeoCoordinates startPoint,
            final GeoCoordinates directionPoint, final double lengthFactor,
            final GeoCoordinates intervalPoint, final int numberLines,
            final PolylineOptions polylineOptions)
            throws InvalidMapDataException {

        StringBuilder script = new StringBuilder();
        // vector calculation, calculate the fourth corner point of the logical
        // rectangle to get the parallel line to the line between start and
        // direction point
        double parallelPointLong = intervalPoint.getLongitudeDeg()
                + (directionPoint.getLongitudeDeg() - startPoint
                        .getLongitudeDeg());
        double parallelPointLat = intervalPoint.getLatitudeDeg()
                + (directionPoint.getLatitudeDeg() - startPoint
                        .getLatitudeDeg());

        for (int i = 0; i < numberLines; i++) {
            GeoCoordinates start = GeometryUtils.pointAlongLine(
                    startPoint.getLongitudeDeg(), startPoint.getLatitudeDeg(),
                    intervalPoint.getLongitudeDeg(),
                    intervalPoint.getLatitudeDeg(), i);

            GeoCoordinates p2 = GeometryUtils.pointAlongLine(
                    directionPoint.getLongitudeDeg(),
                    directionPoint.getLatitudeDeg(), parallelPointLong,
                    parallelPointLat, i);

            GeoCoordinates end = GeometryUtils.pointAlongLine(
                    start.getLongitudeDeg(), start.getLatitudeDeg(),
                    p2.getLongitudeDeg(), p2.getLatitudeDeg(), lengthFactor);

            script.append(new Polyline(Arrays.asList(start, end),
                    variablePrefix + i, polylineOptions));

        }

        return script.toString();
    }
}
