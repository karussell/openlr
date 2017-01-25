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
package openlr.mapviewer.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import openlr.map.GeoCoordinates;
import openlr.map.Line;
import openlr.map.MapDatabase;
import openlr.map.utils.GeometryUtils;

/**
 * The class FindClosestLine adds the utility to find the closest line to a given
 * coordinate. 
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public final class FindClosestLine {
	
	/** The Constant FULL_CIRCLE. */
	private static final int FULL_CIRCLE = 360;
	

	/** The Constant LINE_SEARCH_RADIUS. */
	private static final int LINE_SEARCH_RADIUS = 100;
	
	/**
	 * Utility class cannot be instantiated.
	 */
	private FindClosestLine() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Determine the closest line to a given coordinate. The search will be executed
	 * in an area around the coordinate. If no line is found within this area or
	 * the search is ambiguous then the return value is null. If two lines exist
	 * with the same distance (a bi-directional road) then the line will be returned
	 * where the coordinate is on the right side of the line according to the line
	 * direction.
	 * 
	 * @param map the map database
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * 
	 * @return the line
	 */
	public static Line determineClosestLine(final MapDatabase map, final double x, final double y) {
		Iterator<? extends Line> mapLines = map.findLinesCloseByCoordinate(x, y, LINE_SEARCH_RADIUS);
		List<Line> closestLines  = findClosestLines(x, y, mapLines);
		Line closestLine = null;
		if (closestLines.size() == 1 || closestLines.size() == 2) {
			if (closestLines.size() == 1) {
				closestLine = closestLines.get(0);
			} else {
				Line l1 = closestLines.get(0);
				Line l2 = closestLines.get(1);
				closestLine = determineRightSideLine(x, y, l1,
						l2);
			}
		}
		return closestLine;
	}


	/**
	 * Find closest lines in a set of lines. There might be several lines with
	 * the same distance to the given coordinate.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param mapLines the lines to be filtered
	 * 
	 * @return the closest lines
	 */
	private static List<Line> findClosestLines(final double x, final double y, final Iterator<? extends Line> mapLines) {
		List<Line> closest = new ArrayList<Line>();
		int dist = Integer.MAX_VALUE;
		//System.out.println("findClosestLine (" + x + " -- " + y + ")");
		while (mapLines.hasNext()) {
			Line l = mapLines.next();
			int d = l.distanceToPoint(x, y);
			//System.out.println(l.getID() + ": "+ d);
			if (d < dist) {
				closest.clear();
				closest.add(l);
				dist = d;
			} else if (d == dist) {
				closest.add(l);
			}
		}
		return closest;
	}
	
	
	/**
	 * Determines the line where the given coordinate is on the right side of
	 * the line according to the line direction.
	 * 
	 * @param l1 the first line
	 * @param l2 the second line
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * 
	 * @return the right side line
	 */
	private static Line determineRightSideLine(final double x, final double y,
			final Line l1, final Line l2) {
		int dist1 = l1.measureAlongLine(x, y);
		GeoCoordinates p1 = l1.getGeoCoordinateAlongLine(dist1);
		int dist2 = l2.measureAlongLine(x, y);
		GeoCoordinates p2 = l2.getGeoCoordinateAlongLine(dist2);
		double l1Course = course(p1.getLongitudeDeg(), p1.getLatitudeDeg(), l1.getEndNode().getLongitudeDeg(), l1
				.getEndNode().getLatitudeDeg());
		double l2Course = course(p2.getLongitudeDeg(), p2.getLatitudeDeg(), l2.getEndNode().getLongitudeDeg(), l2
				.getEndNode().getLatitudeDeg());
		double orthoDist1 = orthogonalDistance(x, y, p1.getLongitudeDeg(), p2.getLatitudeDeg(), l1Course);
		double orthoDist2 = orthogonalDistance(x, y, p2.getLongitudeDeg(), p2.getLatitudeDeg(), l2Course);
		if (orthoDist1 > orthoDist2) {
			return l1;
		}
		return l2;
	}

	/**
	 * Calculates the orthogonal distance.
	 * 
	 * @param x1
	 *            the x1
	 * @param y1
	 *            the y1
	 * @param x2
	 *            the x2
	 * @param y2
	 *            the y2
	 * @param course
	 *            the course
	 * 
	 * @return the orthogonal distance
	 */
	private static double orthogonalDistance(final double x1, final double y1,
			final double x2, final double y2, final double course) {
		double otherCourse = course(x1, y1, x2, y2);
		double angle = signedAngleDifference(course, otherCourse);
		return Math.sin(Math.toRadians(angle))
				* GeometryUtils.distance(x1, y1, x2, y2);
	}

	/**
	 * Calculates the signed angle difference.
	 * 
	 * @param alpha
	 *            the angle alpha
	 * @param beta
	 *            the angle beta
	 * 
	 * @return the signed angle difference
	 */
	private static double signedAngleDifference(final double alpha, final double beta) {
		double diff = alpha - beta;
		if (diff < 0) {
			return diff + FULL_CIRCLE;
		}
		return diff;
	}

	/**
	 * Calculates the course.
	 * 
	 * @param x1
	 *            the x1
	 * @param y1
	 *            the y1
	 * @param x2
	 *            the x2
	 * @param y2
	 *            the y2
	 * 
	 * @return the course
	 */
	private static double course(final double x1, final double y1, final double x2,
			final double y2) {
		double lat1 = Math.toRadians(y1);
		double lat2 = Math.toRadians(y2);
		double deltaLon = Math.toRadians(x2 - x1);
		double y = Math.sin(deltaLon) * Math.cos(lat2);
		double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
				* Math.cos(lat2) * Math.cos(deltaLon);
		return (Math.toDegrees(Math.atan2(y, x)) + FULL_CIRCLE) % FULL_CIRCLE;
	}

}
