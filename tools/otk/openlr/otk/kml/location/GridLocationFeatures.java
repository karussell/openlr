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
**/

/**
 *  Copyright (C) 2009-12 TomTom International B.V.
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
package openlr.otk.kml.location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import openlr.location.GridLocation;
import openlr.map.GeoCoordinates;
import openlr.map.InvalidMapDataException;
import openlr.map.utils.GeometryUtils;
import openlr.otk.kml.ContentProvider;
import openlr.otk.kml.KmlGenerationException;
import openlr.otk.kml.KmlUtil;
import de.micromata.opengis.kml.v_2_2_0.Feature;

/**
 * A content provider that delivers the KML features for drawing a grid
 * location.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
class GridLocationFeatures extends ContentProvider<GridLocation> {

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
     * Creates a new rectangle writer
     * 
     * @param location
     *            The rectangle location instance to draw
     */
    public GridLocationFeatures(final GridLocation location) {
        super(location);
        
        addGeneralData("Rows", "" + location.getNumberOfRows());
        addGeneralData("Columns", "" + location.getNumberOfColumns());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<Feature> createContent(final GridLocation location)
            throws KmlGenerationException {

        List<Feature> features = new ArrayList<Feature>();
        List<GeoCoordinates> corners = new ArrayList<GeoCoordinates>(
                location.getCornerPoints());

        int columns = location.getNumberOfColumns();
        int rows = location.getNumberOfRows();

        GeoCoordinates lowerLeft = corners.get(INDEX_LOWER_LEFT);
        GeoCoordinates lowerRight = corners.get(INDEX_LOWER_RIGHT);
        GeoCoordinates upperLeft = corners.get(INDEX_UPPER_LEFT);

        try {

            // draw vertical lines
            features.addAll(drawLines(lowerLeft, upperLeft, rows, lowerRight,
                    columns + 1));
            // draw horizontal lines
            features.addAll(drawLines(lowerLeft, lowerRight, columns,
                    upperLeft, rows + 1));

        } catch (InvalidMapDataException e) {
            throw new KmlGenerationException(
                    "Grid location definition results in invalid geo coordinates",
                    e);
        }

        return features;
    }

    /**
     * Draws parallel lines. The base line is defined between
     * <code>startPoint</code> and <code>directionPoint</code>. Its length is
     * multiplied by the <code>lengthFactor</code>. The resulting line is
     * parallel translated into <code>intervalPoint</code>. The same translation
     * is repeated in excess of the intervalPoint so that in the end
     * <code>numberLines</code> lines are created.
     * 
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
     * @return The list of KML features describing the generated lines
     * @throws InvalidMapDataException
     *             If some of the geo-coordinates calculated are out of valid
     *             bounds.
     */
    private List<Feature> drawLines(final GeoCoordinates startPoint,
            final GeoCoordinates directionPoint, final double lengthFactor,
            final GeoCoordinates intervalPoint, final int numberLines)
            throws InvalidMapDataException {
        List<Feature> features = new ArrayList<Feature>();

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

            features.add(KmlUtil.createLineString(Arrays.asList(start, end),
                    "", "" + i, "", LocationStyleIdentifier.LINE));

        }

        return features;
    }
}
