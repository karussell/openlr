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

import openlr.location.PointLocation;
import openlr.map.GeoCoordinates;
import openlr.map.Line;
import openlr.map.Node;
import openlr.otk.kml.ContentProvider;
import openlr.otk.kml.KmlUtil;
import de.micromata.opengis.kml.v_2_2_0.Feature;

/**
 * This is a base class for point locations that draws location line and the
 * positive offset location.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 * 
 * @param <T>
 *            The concrete type of {@link PointLocation}
 */
abstract class AbstractPointLocationFeatures<T extends PointLocation> extends
        ContentProvider<T> {

    /**
     * Creates a new content writer
     * 
     * @param location
     *            The object to draw
     */
    public AbstractPointLocationFeatures(final T location) {
        super(location);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Feature> createContent(final T location) {

        List<Feature> features = createLineAndPointAlong(location);
        features.addAll(createAdditionalContent(location));
        return features;
    }

    /**
     * Draws location line and the positive offset location.
     * 
     * @param location
     *            The location to draw
     * @return The list of features that represent the location line and the
     *         positive offset
     */
    private List<Feature> createLineAndPointAlong(final T location) {
        List<Feature> features = new ArrayList<Feature>();

        Line line = location.getPoiLine();

        final GeoCoordinates pt = location.getAccessPoint();
        final String id = "positiveOffset";
        final String name = String.format("Access point (%dm)",
                location.getPositiveOffset());
        features.add(KmlUtil.createPoint(pt.getLongitudeDeg(),
                pt.getLatitudeDeg(), id, name, "",
                LocationStyleIdentifier.POSITIVE_OFFSET));

        Node startNode = line.getStartNode();
        features.add(KmlUtil.createPoint(startNode.getLongitudeDeg(),
                startNode.getLatitudeDeg(), String.valueOf(startNode.getID()),
                String.format("node-%d", startNode.getID()),
                "Start of the related network line",
                LocationStyleIdentifier.POINT));

        Node endNode = line.getEndNode();
        features.add(KmlUtil.createPoint(endNode.getLongitudeDeg(),
                endNode.getLatitudeDeg(), String.valueOf(endNode.getID()),
                String.format("node-%d", endNode.getID()),
                "End of the related network line",
                LocationStyleIdentifier.POINT));

        features.add(KmlUtil.createLineString(line,
                String.valueOf(line.getID()), "Line " + line.getID(), "",
                LocationStyleIdentifier.LINE));
        return features;
    }

    /**
     * Implements additional KML generation for the specific point location
     * implementation
     * 
     * @param location
     *            The location to draw
     * @return The generated KML features
     */
    protected abstract List<Feature> createAdditionalContent(
            PointLocation location);

}
