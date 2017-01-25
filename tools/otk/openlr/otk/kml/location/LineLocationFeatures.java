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

import openlr.location.LineLocation;
import openlr.location.Location;
import openlr.map.GeoCoordinates;
import openlr.map.Line;
import openlr.otk.kml.KmlUtil;
import de.micromata.opengis.kml.v_2_2_0.Feature;

/**
 * A content provider that delivers the KML features for drawing a line
 * location.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
class LineLocationFeatures extends
        AbstractLineBasedLocationFeatures<LineLocation> {

    /**
     * Creates a new content writer
     * 
     * @param location
     *            The object to draw
     */
    public LineLocationFeatures(final LineLocation location) {
        super(location);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final List<Feature> createAdditionalContent(
            final Location location) {

        List<Feature> features = new ArrayList<Feature>();

        List<? extends Line> lines = location.getLocationLines();

        Line startLine = lines.get(0);
        Line endLine = lines.get(lines.size() - 1);

        final GeoCoordinates posOffPoint = startLine.getGeoCoordinateAlongLine(location
                .getPositiveOffset());

        String positiveOffsetText = "";
        if (location.hasPositiveOffset()) {
            positiveOffsetText = String.format("(Positive offset %d meters)",
                    location.getPositiveOffset());
        }

        features.add(KmlUtil.createPoint(posOffPoint.getLongitudeDeg(),
                posOffPoint.getLatitudeDeg(), "positiveOffset", "Start",
                "Start of this location " + positiveOffsetText,
                LocationStyleIdentifier.POSITIVE_OFFSET));

        final GeoCoordinates negOffPoint = endLine.getGeoCoordinateAlongLine(endLine
                .getLineLength() - location.getNegativeOffset());

        String negativeOffsetText = "";
        if (location.hasNegativeOffset()) {
            negativeOffsetText = String.format("(Negative offset %d meters)",
                    location.getNegativeOffset());
        }
        features.add(KmlUtil.createPoint(negOffPoint.getLongitudeDeg(),
                negOffPoint.getLatitudeDeg(), "negativeOffset", "End",
                "End of this location " + negativeOffsetText,
                LocationStyleIdentifier.NEGATIVE_OFFSET));

        return features;

    }

}
