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
import openlr.rawLocRef.RawLocationReference;
import de.micromata.opengis.kml.v_2_2_0.Feature;

/**
 * This content provider creates KML features for drawing rectangle or grid
 * location references
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 * @param <T>
 *            The concrete type of location reference to process, should be
 *            {@link openlr.rawLocRef.RawGridLocRef} or
 *            {@link openlr.rawLocRef.RawRectangleLocRef}.
 */
class RectangleBasedLocationReferenceFeatures<T extends RawLocationReference>
        extends ContentProvider<T> {

    /**
     * Creates a new content writer
     * 
     * @param locationRef
     *            The object to draw
     */
    public RectangleBasedLocationReferenceFeatures(final T locationRef) {
        super(locationRef);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<Feature> createContent(final T locRef) {

        List<Feature> features = new ArrayList<Feature>();
        GeoCoordinates lowerLeft = locRef.getLowerLeftPoint();
        GeoCoordinates upperRight = locRef.getUpperRightPoint();

        features.add(KmlUtil.createPoint(lowerLeft.getLongitudeDeg(),
                lowerLeft.getLatitudeDeg(), "lowerLeft", "Lower left",
                Formatter.formatCoord(lowerLeft), LocRefStyleIdentifier.POINT));

        features.add(KmlUtil.createPoint(upperRight.getLongitudeDeg(),
                upperRight.getLatitudeDeg(), "upperRight", "Upper right",
                Formatter.formatCoord(upperRight), LocRefStyleIdentifier.POINT));
        return features;
    }
}
