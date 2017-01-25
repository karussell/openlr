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
package openlr.mapviewer.location;

import java.util.List;

import javax.swing.JOptionPane;

import openlr.map.GeoCoordinates;
import openlr.map.GeoCoordinatesImpl;
import openlr.map.InvalidMapDataException;
import openlr.map.Line;
import openlr.map.RectangleCorners;

/**
 * The Class LocationHelper.
 */
public final class LocationHelper {

	/**
	 * Utility class shall not be instantiated.
	 */
	private LocationHelper() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Checks if the line l is connected to the end of the current location.
	 * 
	 * @param lines
	 *            the lines
	 * @param l
	 *            the line
	 * @return true, if l is connected to the end of the location
	 */
	public static boolean checkConnectionEnd(final List<Line> lines,
			final Line l) {
		if (lines.isEmpty()) {
			return true;
		}
		Line lastLine = lines.get(lines.size() - 1);
		if (lastLine.getEndNode().getID() != l.getStartNode().getID()) {
			return false;
		}
		return true;
	}

	/**
	 * Checks if this is the last line of the location.
	 * 
	 * @param lines
	 *            the lines
	 * @param line
	 *            the line
	 * @return true, if it is the last line
	 */
	public static boolean isLastLine(final List<Line> lines, final Line line) {
		return (lines.get(lines.size() - 1).getID() == line.getID());
	}

	/**
	 * Checks if this is the last line of the location.
	 * 
	 * @param lines
	 *            the lines
	 * @param line
	 *            the line
	 * @return true, if it is the last line
	 */
	public static boolean isFirstLine(final List<Line> lines, final Line line) {
		return (lines.get(0).getID() == line.getID());
	}

	/**
	 * Checks if the line l is connected to the start of the current location.
	 * 
	 * @param lines
	 *            the lines
	 * @param l
	 *            the line
	 * @return true, if l is connected to the start of the location
	 */
	public static boolean checkConnectionStart(final List<Line> lines,
			final Line l) {
		if (lines.isEmpty()) {
			return true;
		}
		Line firstLine = lines.get(0);
		if (firstLine.getStartNode().getID() != l.getEndNode().getID()) {
			return false;
		}
		return true;
	}

	/**
	 * Check positive offset.
	 * 
	 * @param lines
	 *            the lines
	 * @param value
	 *            the value
	 * @return true, if successful
	 */
	public static boolean checkPositiveOffset(final List<Line> lines,
			final int value) {
		boolean ret = false;
		if (!lines.isEmpty()) {
			Line l = lines.get(0);
			if (l.getLineLength() < value) {
				JOptionPane.showMessageDialog(null,
						"Positive offset value exceeds length of first line!",
						"Positive offset error", JOptionPane.ERROR_MESSAGE);
			} else {
				ret = true;
			}
		}
		return ret;
	}

	/**
	 * Check negative offset.
	 * 
	 * @param lines
	 *            the lines
	 * @param value
	 *            the value
	 * @return true, if successful
	 */
	public static boolean checkNegativeOffset(final List<Line> lines,
			final int value) {
		boolean ret = false;
		if (!lines.isEmpty()) {
			Line l = lines.get(lines.size() - 1);
			if (l.getLineLength() < value) {
				JOptionPane.showMessageDialog(null,
						"Positive offset value exceeds length of first line!",
						"Positive offset error", JOptionPane.ERROR_MESSAGE);
			} else {
				ret = true;
			}
		}
		return ret;
	}

	/**
	 * Recalculate pos off coordinate.
	 * 
	 * @param lines
	 *            the lines
	 * @param poffCoord
	 *            the poff coord
	 * @return the offset data
	 * @throws InvalidMapDataException
	 *             the invalid map data exception
	 */
	public static OffsetData calculatePosOffInMeter(final List<Line> lines,
			final GeoCoordinates poffCoord) throws InvalidMapDataException {
		if (!lines.isEmpty()) {
			Line l = lines.get(0);
			int poff = l.measureAlongLine(poffCoord.getLongitudeDeg(),
					poffCoord.getLatitudeDeg());
			GeoCoordinates newCoord = l.getGeoCoordinateAlongLine(poff);
			return new OffsetData(poff, newCoord);
		}
		return null;
	}

	/**
	 * Recalculate pos off coordinate.
	 * 
	 * @param lines
	 *            the lines
	 * @param poff
	 *            the poff
	 * @return the offset data
	 * @throws InvalidMapDataException
	 *             the invalid map data exception
	 */
	public static OffsetData calculatePosOffCoord(final List<Line> lines,
			final int poff) throws InvalidMapDataException {
		if (!lines.isEmpty()) {
			Line l = lines.get(0);
			boolean cutNeeded = poff >= l.getLineLength();
			int newPoff = poff;
			if (cutNeeded) {
				// if offset is greater than the first line- set offset to
				// line length minus 1 meter
				newPoff = l.getLineLength() - 1;
			}
			GeoCoordinates poffCoord = l.getGeoCoordinateAlongLine(newPoff);
			return new OffsetData(newPoff, poffCoord);
		}
		return null;
	}

	/**
	 * Recalculate neg off coordinate.
	 * 
	 * @param lines
	 *            the lines
	 * @param noffCoord
	 *            the noff coord
	 * @return the offset data
	 * @throws InvalidMapDataException
	 *             the invalid map data exception
	 */
	public static OffsetData calculateNegOffInMeter(final List<Line> lines,
			final GeoCoordinates noffCoord) throws InvalidMapDataException {
		if (!lines.isEmpty()) {
			Line l = lines.get(lines.size() - 1);
			int noff = l.getLineLength()
					- l.measureAlongLine(noffCoord.getLongitudeDeg(),
							noffCoord.getLatitudeDeg());
            GeoCoordinates newCoord = l.getGeoCoordinateAlongLine(l
                    .getLineLength() - noff);
			return new OffsetData(noff, newCoord);
		}
		return null;
	}

	/**
	 * Recalculate neg off coordinate.
	 * 
	 * @param lines
	 *            the lines
	 * @param noff
	 *            the noff
	 * @return the offset data
	 * @throws InvalidMapDataException
	 *             the invalid map data exception
	 */
	public static OffsetData calculateNegOffCoord(final List<Line> lines,
			final int noff) throws InvalidMapDataException {
		if (!lines.isEmpty()) {
			Line l = lines.get(lines.size() - 1);
			boolean cutNeeded = noff >= l.getLineLength();
			int newNoff = noff;
			if (cutNeeded) {
				// if offset is greater than the last line- set offset to
				// line length minus 1 meter
				newNoff = l.getLineLength() - 1;
			}
			GeoCoordinates noffCoord = l.getGeoCoordinateAlongLine(l.getLineLength() - newNoff);
			return new OffsetData(newNoff, noffCoord);
		}
		return null;
	}

	/**
	 * The Class OffsetData.
	 */
	public static class OffsetData {

		/** The distance. */
		private final int distance;

		/** The geo coord. */
		private final GeoCoordinates geoCoord;

		/**
		 * Instantiates a new offset data.
		 * 
		 * @param d
		 *            the d
		 * @param gc
		 *            the gc
		 */
		OffsetData(final int d, final GeoCoordinates gc) {
			distance = d;
			geoCoord = gc;
		}

		/**
		 * @return the distance
		 */
		public final int getDistance() {
			return distance;
		}

		/**
		 * @return the geoCoord
		 */
		public final GeoCoordinates getGeoCoord() {
			return geoCoord;
		}

	}

	/**
	 * Check if the given polygon is valid
	 * 
	 * @param polyPoints
	 *            the poly points
	 * @return true, if is polygon valid
	 */
	public static boolean isPolygonValid(final List<GeoCoordinates> polyPoints) {
		//TODO
		return false;
	}

	/**
	 * Order coordinates.
	 *
	 * @param coord1 the coord1
	 * @param coord2 the coord2
	 * @return the rectangle corners
	 * @throws InvalidMapDataException the invalid map data exception
	 */
	public static RectangleCorners orderCoordinates(
			final GeoCoordinates coord1, final GeoCoordinates coord2) throws InvalidMapDataException {
		GeoCoordinates ll = null;
		GeoCoordinates ur = null;
		if (coord1.getLongitudeDeg() <= coord2.getLongitudeDeg()) {
            if (coord1.getLatitudeDeg() <= coord2.getLatitudeDeg()) {
                ll = coord1;
                ur = coord2;
            } else {
                ll = GeoCoordinatesImpl.newGeoCoordinatesUnchecked(
                        coord1.getLongitudeDeg(), coord2.getLatitudeDeg());
                ur = GeoCoordinatesImpl.newGeoCoordinatesUnchecked(
                        coord2.getLongitudeDeg(), coord1.getLatitudeDeg());
            }
        } else {
            if (coord1.getLatitudeDeg() <= coord2.getLatitudeDeg()) {
                ll = GeoCoordinatesImpl.newGeoCoordinatesUnchecked(
                        coord2.getLongitudeDeg(), coord1.getLatitudeDeg());
                ur = GeoCoordinatesImpl.newGeoCoordinatesUnchecked(
                        coord1.getLongitudeDeg(), coord2.getLatitudeDeg());
            } else {
				ll = coord2;
				ur = coord1;
			}
		}
		return new RectangleCorners(ll, ur);
	}

}
