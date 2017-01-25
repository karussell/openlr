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

import java.awt.geom.Rectangle2D;
import java.util.Collection;

import openlr.location.Location;
import openlr.map.GeoCoordinates;
import openlr.map.GeoCoordinatesImpl;
import openlr.map.InvalidMapDataException;
import openlr.map.utils.GeometryUtils;

/**
 * The Class BoundingBoxCalculator.
 */
public final class BoundingBoxCalculator {
    
    /** The Constant METER_PER_KILOMETER. */
    private static final double METERS_PER_KILOMETER = 1000.0;

	/**
	 * Utility class shall not be instantiated.
	 */
	private BoundingBoxCalculator() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Calculate bounding box.
	 * 
	 * @param loc
	 *            the loc
	 * @return the rectangle2 d. double
	 */
    public static BoundingBox calculateBoundingBox(final Location loc) {
        BoundingBoxLocationHandler factory = new BoundingBoxLocationHandler();
        return factory.process(loc);
    }
	
	   /**
     * Calculates the bounding box That encloses all the given coordinates.
     * 
     * @param coordinates
     *            The set of coordinates.
     * @return The bounding box
     */
    public static BoundingBox calculateBoundingBoxAroundCoordinates(
            final Collection<GeoCoordinates> coordinates) {

        Rectangle2D.Double rect = new Rectangle2D.Double();
        boolean first = true;
        for (GeoCoordinates gc : coordinates) {
            if (first) {
                rect.setRect(gc.getLongitudeDeg(), gc.getLatitudeDeg(), 0, 0);
                first = false;
            } else {
                rect.add(gc.getLongitudeDeg(), gc.getLatitudeDeg());
            }
        }
        
        GeoCoordinates ll = GeoCoordinatesImpl.newGeoCoordinatesUnchecked(
                rect.getMinX(), rect.getMinY());
        GeoCoordinates ur = GeoCoordinatesImpl.newGeoCoordinatesUnchecked(
                rect.getMaxX(), rect.getMaxY());

        return new BoundingBox(ll, ur);
    }

    /**
     * Calculates the bounding box around a circle.
     * 
     * @param center
     *            The center coordinate
     * @param radiusMeters
     *            The radius of the circle in meters
     * @return The bounding box
     */
    public static BoundingBox calculateBoundingBoxAroundCircle(
            final GeoCoordinates center, final long radiusMeters) {

        Rectangle2D.Double rect = new Rectangle2D.Double();

        try {
            double distanceKm = radiusMeters / METERS_PER_KILOMETER;

            GeoCoordinates top = GeometryUtils.determineCoordinateInDistance(
                    center.getLongitudeDeg(), center.getLatitudeDeg(), 0,
                    distanceKm);
            GeoCoordinates right = GeometryUtils.determineCoordinateInDistance(
                    center.getLongitudeDeg(), center.getLatitudeDeg(),
                    (int) GeometryUtils.QUARTER_CIRCLE, distanceKm);
            GeoCoordinates bottom = GeometryUtils
                    .determineCoordinateInDistance(center.getLongitudeDeg(),
                            center.getLatitudeDeg(),
                            (int) GeometryUtils.HALF_CIRCLE, distanceKm);
            GeoCoordinates left = GeometryUtils.determineCoordinateInDistance(
                    center.getLongitudeDeg(), center.getLatitudeDeg(),
                    (int) GeometryUtils.THREE_QUARTER_CIRCLE, distanceKm);
            rect.setRect(left.getLongitudeDeg(), bottom.getLatitudeDeg(),
                    right.getLongitudeDeg() - left.getLongitudeDeg(),
                    top.getLatitudeDeg() - bottom.getLatitudeDeg());

            GeoCoordinates ll = GeoCoordinatesImpl.newGeoCoordinatesUnchecked(
                    rect.getMinX(), rect.getMinY());
            GeoCoordinates ur = GeoCoordinatesImpl.newGeoCoordinatesUnchecked(
                    rect.getMaxX(), rect.getMaxY());

            return new BoundingBox(ll, ur);

        } catch (InvalidMapDataException e) {
            throw new IllegalStateException("Could not calculate bounding box",
                    e);
        }
    }	
}
