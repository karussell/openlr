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
import java.util.List;

import openlr.location.CircleLocation;
import openlr.map.GeoCoordinates;
import openlr.map.InvalidMapDataException;
import openlr.map.utils.GeometryUtils;
import openlr.otk.kml.ContentProvider;
import openlr.otk.kml.KmlGenerationException;
import openlr.otk.kml.KmlUtil;
import openlr.otk.utils.Formatter;
import de.micromata.opengis.kml.v_2_2_0.Feature;

/**
 * This writer creates KML drawing a polygon location.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
class CircleLocationFeatures extends ContentProvider<CircleLocation> {

    /**
     * Meters per kilometer
     */
    private static final double METERS_PER_KILOMETER = 1000.0;
    
    /**
     * The angle between two calculations of points on the circle
     */
    private static final int CIRCLE_SAMPLE_RATE = 1;

    /**
     * Creates a new circle writer
     * 
     * @param location
     *            The polygon location instance to draw
     */
    public CircleLocationFeatures(final CircleLocation location) {
        super(location);
        addGeneralData("Radius", Long.toString(location.getRadius()));    
    }

    @Override
    public final List<Feature> createContent(final CircleLocation location) throws KmlGenerationException {

        List<Feature> features = new ArrayList<Feature>();

        GeoCoordinates center = location.getCenterPoint();
        long radius = location.getRadius();

        List<GeoCoordinates> coordinates = new ArrayList<GeoCoordinates>();

        final double sides = 360;

        double radiusInKilometers = radius / METERS_PER_KILOMETER;

        for (int i = 0; i < sides; i++) {

            int angle = i * CIRCLE_SAMPLE_RATE;
            try {
                GeoCoordinates coord = GeometryUtils
                        .determineCoordinateInDistance(
                                center.getLongitudeDeg(),
                                center.getLatitudeDeg(), angle,
                                radiusInKilometers);

                coordinates.add(coord);

            } catch (InvalidMapDataException e) {
                throw new KmlGenerationException(
                        "Circle location definition results in invalid geo coordinates",
                        e);
            }

        }
        coordinates.add(coordinates.get(0));

        features.add(KmlUtil.createLineString(coordinates, location.getID(),
                "circle \"" + location.getID() + "\"", "",
                LocationStyleIdentifier.LINE));
        features.add(KmlUtil.createPoint(center.getLongitudeDeg(),
                center.getLatitudeDeg(), "center", "Center point",
                Formatter.formatCoord(center), LocationStyleIdentifier.POINT));

        return features;
    }
}
