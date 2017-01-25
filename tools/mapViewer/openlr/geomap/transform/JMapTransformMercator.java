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
package openlr.geomap.transform;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

/**
 * The Class JMapTransformMercator.
 */
public class JMapTransformMercator  {
	
	/**
     * The factor necessary to adapt the scale factor for the X axis
     */
    private static final double X_SCALE_FACTOR = 0.67;

    /** The Constant PI. */
	private static final double HALF_CIRCLE = 180.0;	
	
	/** The scale_. */
	private final double scale; // default scale factor (screen width)/(map width)
	
	/** The center screen_. */
	private final double centerLong;
	
	/** The center screen. */
	private final double centerScreen; // map center coordinate (lon)
												// and the screen center (x)

	/** The y1_. */
	private final double lat; // map coordinates

	/**
	 * Instantiates a new j map transform mercator.
	 *
	 * @param screen the screen
     * @param lowerLeftLong
     *            the longitude value of the lower left
     * @param lowerLeftLat
     *            the latitude value of the lower left
     * @param upperRightLon
     *            the longitude value of the upper right
     * @param upperRightLat
     *            the longitude value of the upper right
	 */
    public JMapTransformMercator(final Rectangle screen,
            final double lowerLeftLong, final double lowerLeftLat,
            final double upperRightLon, final double upperRightLat) {
		centerLong = lowerLeftLong - (lowerLeftLong - upperRightLon) / 2.0;
		centerScreen = screen.width / 2.0;
		double sx = screen.width / X_SCALE_FACTOR * (lowerLeftLong - upperRightLon);
		double sy = screen.height / (lowerLeftLat - upperRightLat);
		if (sx < sy) {
			scale = sx;
		} else {
			scale = sy;
		}
		lat = lowerLeftLat;
	}

	
    /**
     * Delivers the geo coordinate for a given screen pixel coordinate. 
     *
     * @param p the pixel coordinate
     * @return the world coordinate
     */
	public final Point2D.Double getGeoCoordinate(final Point p) {
		return getGeoCoordinate(p.x, p.y);
	}
	
	/**
     * Delivers the geo coordinate for a given screen pixel coordinate. The
     * pixel value assumes a coordinate system that has the source (0,0) in the
     * lower left.
	 *
	 * @param x the x value of the pixel coordinate
	 * @param y the y value of the pixel coordinate
	 * @return the world coordinate
	 */
	public final Point2D.Double getGeoCoordinate(final int x, final int y) {
		double latScale = Math.cos(((lat - y / scale) / HALF_CIRCLE) * Math.PI);
		return new Point2D.Double(centerLong - (x - centerScreen)
				/ (latScale * scale), (lat - (y / scale)));
	}

	/**
     * Delivers the screen coordinate for a given geo coordinate. The pixel
     * value relates to a coordinate system that has the source (0,0) in the
     * lower left.
	 *
	 * @param longitude the longitude 
	 * @param latitude the latitude
	 * @return the screen pixel coordinate
	 */
	public final Point getPixel(final double longitude, final double latitude) {
		double latScale = Math.cos((latitude / HALF_CIRCLE) * Math.PI);
		Point p = new Point(
				(int) ((centerLong - longitude) * latScale * scale + centerScreen),
				(int) ((lat - latitude) * scale));
		return p;
	}

}
