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
package openlr.otk.kml.locationreference;

import java.util.ArrayList;
import java.util.List;

import openlr.LocationReferencePoint;
import openlr.Offsets;
import openlr.otk.kml.ContentProvider;
import openlr.otk.kml.KmlUtil;
import openlr.rawLocRef.RawLocationReference;
import de.micromata.opengis.kml.v_2_2_0.Feature;

/**
 * Generates KML features of the list of location reference points provided by
 * the location reference.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 * @param <T>
 *            The concrete type of location reference to process, must be one
 *            that delivers content for
 *            {@link RawLocationReference#getLocationReferencePoints()}
 */
class LRPBasedLocationReferenceFeatures<T extends RawLocationReference> extends
        ContentProvider<T> {

    /**
     * Creates a new location reference writer
     * 
     * @param locRef
     *            The object to draw
     */
    public LRPBasedLocationReferenceFeatures(final T locRef) {
        super(locRef);
    }

    /**
     * Generates KML features that visualize a line location reference.
     * 
     * @param locRef
     *            the location reference to draw
     * @return The list of generated KML features
     */
    @Override
    public final List<Feature> createContent(final T locRef) {

        List<Feature> features = createBaseContent(locRef);

        addContent(features, locRef);

        return features;
    }

    /**
     * Generates KML features of the list of location reference points provided
     * by the location reference. the location reference
     * 
     * @param locRef
     *            The location reference to draw
     * @return The list of features visualizing the location reference points
     */
    private List<Feature> createBaseContent(final T locRef) {

        List<Feature> features = new ArrayList<Feature>();
        List<? extends LocationReferencePoint> locRefPoints = locRef
                .getLocationReferencePoints();

        Offsets offsets = locRef.getOffsets();

        for (int i = 0; i < locRefPoints.size(); i++) {
            final LocationReferencePoint locRefPoint = locRefPoints.get(i);
            final String name = String.format("LRP-%d", locRefPoint.getSequenceNumber());
            final StringBuilder description = new StringBuilder();
            description.append(String.format("Longitude: %f",
                    locRefPoint.getLongitudeDeg()));
            description.append(String.format("<br/>Latitude: %f",
                    locRefPoint.getLatitudeDeg()));
            description.append(String.format("<br/>Functional road class: %s",
                    locRefPoint.getFRC()));
            description.append(String.format("<br/>Form of way: %s",
                    locRefPoint.getFOW()));
            description.append(String.format("<br/>Bearing: %s",
                    Math.round(locRefPoint.getBearing())));
            if (i < locRefPoints.size() - 1) {
                final int distanceToNext = locRefPoint.getDistanceToNext();
                description.append(String.format(
                        "<br/>Lowest FRC to next point: %s",
                        locRefPoint.getLfrc()));
                description.append(String.format(
                        "<br/>Distance to next point: %dm", distanceToNext));
            }

            if (i == 0) {
                if ((offsets != null) && offsets.hasPositiveOffset()) {
                    description.append(String.format(
                            "<br/>Positive offset: %d", offsets
                                    .getPositiveOffset(locRefPoint
                                            .getDistanceToNext())));
                }
            } else if (i == locRefPoints.size() - 1) {
                if ((offsets != null) && offsets.hasNegativeOffset()) {
                    description.append(String.format(
                            "<br/>Negative offset: %d", offsets
                                    .getNegativeOffset(locRefPoints.get(i - 1)
                                            .getDistanceToNext())));
                }
            }
            description.append("<br/>");
            features.add(KmlUtil.createPoint(locRefPoint.getLongitudeDeg(),
                    locRefPoint.getLatitudeDeg(), name, name,
                    description.toString(), LocRefStyleIdentifier.LRP));
        }

        return features;
    }

    /**
     * Possibility to add content by deriving classes
     * 
     * @param features
     *            The feature list so far to add the additional content to
     * @param locRef
     *            The location reference to process
     */
    void addContent(@SuppressWarnings("unused") final List<Feature> features,
            @SuppressWarnings("unused") final T locRef) {
    }

}
