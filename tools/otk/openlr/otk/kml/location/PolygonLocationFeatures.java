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

import openlr.location.PolygonLocation;
import openlr.map.GeoCoordinates;
import openlr.otk.kml.ContentProvider;
import openlr.otk.kml.KmlUtil;
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
class PolygonLocationFeatures extends ContentProvider<PolygonLocation> {

    /**
     * Creates a new content writer
     * 
     * @param location
     *            The object to draw
     */
    public PolygonLocationFeatures(final PolygonLocation location) {
        super(location);
    }

    /**
     * {@inheritDoc}
     */    
    @Override
    public final List<Feature> createContent(final PolygonLocation location) {

        List<GeoCoordinates> corners = new ArrayList<GeoCoordinates>(
                location.getCornerPoints());

        corners.add(corners.get(0));

        Feature feature = KmlUtil.createLineString(corners, location.getID(),
                "polygon \"" + location.getID() + "\"", "",
                LocationStyleIdentifier.LINE);

        return Arrays.asList(feature);
    }
}
