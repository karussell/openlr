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
package openlr.mapviewer.utils.bbox;

import openlr.map.GeoCoordinates;
import openlr.map.GeoCoordinatesImpl;

/**
 * This class implements a bounding box of geo coordinates.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class BoundingBox {

    /**
     * The geo-coordinates
     */
    private GeoCoordinates ll, ur;

    /**
     * Creates a new bounding box.
     * 
     * @param lowerLeft
     *            The lower left coordinate
     * @param upperRight
     *            The upper right coordinate
     */
    public BoundingBox(final GeoCoordinates lowerLeft,
            final GeoCoordinates upperRight) {

        if (lowerLeft.getLongitudeDeg() > upperRight.getLongitudeDeg()
                || lowerLeft.getLatitudeDeg() > upperRight.getLatitudeDeg()) {
            throw new IllegalArgumentException(
                    "Wrong bounds! Lower left is not lower than upper right.");
        }
        ll = lowerLeft;
        ur = upperRight;
    }

    /**
     * Delivers the lower left coordinate
     * 
     * @return The lower left
     */
    public final GeoCoordinates getLowerLeft() {
        return ll;
    }

    /**
     * Delivers the upper right coordinate
     * 
     * @return The upper right
     */    
    public final GeoCoordinates getUpperRight() {
        return ur;
    }

    /**
     * Joins the given bounding box with this instance. Returns a new instance
     * of {@link BoundingBox} reflecting the union result.
     * 
     * @param box
     *            The box to join with this bounding box
     * @return A new instance reflecting the result of the union
     */
    public final BoundingBox add(final BoundingBox box) {

        GeoCoordinates otherLL = box.getLowerLeft();
        GeoCoordinates otherUR = box.getUpperRight();
        double minLong = Math.min(ll.getLongitudeDeg(),
                otherLL.getLongitudeDeg());
        double minLat = Math.min(ll.getLatitudeDeg(), otherLL.getLatitudeDeg());
        double maxLong = Math.max(ur.getLongitudeDeg(),
                otherUR.getLongitudeDeg());
        double maxLat = Math.max(ur.getLatitudeDeg(), otherUR.getLatitudeDeg());

        GeoCoordinates upperRight = GeoCoordinatesImpl
                .newGeoCoordinatesUnchecked(maxLong, maxLat);
        GeoCoordinates lowerLeft = GeoCoordinatesImpl
                .newGeoCoordinatesUnchecked(minLong, minLat);
        return new BoundingBox(lowerLeft, upperRight);
    }
}
