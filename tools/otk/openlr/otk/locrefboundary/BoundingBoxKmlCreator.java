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
package openlr.otk.locrefboundary;

import java.util.ArrayList;
import java.util.List;

import openlr.map.GeoCoordinates;
import openlr.map.RectangleCorners;
import openlr.otk.kml.ContentProvider;
import openlr.otk.kml.KmlGenerationException;
import openlr.otk.kml.KmlUtil;
import openlr.otk.kml.StyleIdentifier;
import openlr.otk.utils.Formatter;
import de.micromata.opengis.kml.v_2_2_0.Feature;

/**
 * Draws a bounding box in KML.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
class BoundingBoxKmlCreator extends ContentProvider<RectangleCorners> {

    /**
     * The name of the box to draw in the KML
     */
    private final String kmlName;

    /**
     * The description of the KML element
     */
    private final String desc;

    /**
     * The related style
     */
    private final StyleIdentifier style;

    /**
     * Sets up a drawn bounding box
     * 
     * @param bbox
     *            The corner points of the bounding box
     * @param name
     *            The name of the KML element
     * @param description
     *            The description of the KML element
     * @param styleID
     *            The related style
     * 
     */
    public BoundingBoxKmlCreator(final RectangleCorners bbox,
            final String name, final String description,
            final StyleIdentifier styleID) {
        super(bbox);
        this.kmlName = name;
        this.desc = description;
        this.style = styleID;
        addGeneralData("lower left", Formatter.formatCoord(bbox.getLowerLeft()));
        addGeneralData("upper right",
                Formatter.formatCoord(bbox.getUpperRight()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<Feature> createContent(final RectangleCorners bbox)
            throws KmlGenerationException {

        List<Feature> features = new ArrayList<Feature>();

        List<GeoCoordinates> corners = new ArrayList<GeoCoordinates>();
        corners.addAll(bbox.getCornerPoints());
        corners.add(bbox.getCornerPoints().get(0));

        features.add(KmlUtil.createLineString(corners, "bbox", kmlName, desc,
                style));

        return features;
    }
}
