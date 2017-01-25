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

import openlr.map.GeoCoordinates;
import openlr.otk.kml.ContentProvider;
import openlr.otk.kml.KmlUtil;
import openlr.otk.utils.Formatter;
import openlr.rawLocRef.RawPolygonLocRef;
import de.micromata.opengis.kml.v_2_2_0.Feature;

/**
 * This content provided creates the KML for drawing a polygon location
 * reference
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
class PolygonLocationReferenceFeatures extends
        ContentProvider<RawPolygonLocRef> {

    /**
     * Creates a new content writer
     * 
     * @param locationRef
     *            The object to draw
     */
    public PolygonLocationReferenceFeatures(
            final RawPolygonLocRef locationRef) {
        super(locationRef);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<Feature> createContent(
            final RawPolygonLocRef locationRef) {
        

        List<? extends GeoCoordinates> points = locationRef.getCornerPoints();
        List<Feature> features = new ArrayList<Feature>(points.size());
        
        int counter = 1;
        for (GeoCoordinates coordinate : points) {
            Feature point = KmlUtil.createPoint(coordinate.getLongitudeDeg(),
                    coordinate.getLatitudeDeg(), "", "Coordinate " + counter++,
                    Formatter.formatCoord(coordinate), LocRefStyleIdentifier.POINT);
            features.add(point);
        }
        

        return features;
    }
}
