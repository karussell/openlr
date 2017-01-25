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

import java.util.ArrayList;
import java.util.List;

import openlr.LocationType;
import openlr.decoder.OpenLRDecoderProcessingException;
import openlr.decoder.worker.coverage.RectangleCoverage;
import openlr.geomap.MapLayer;
import openlr.location.Location;
import openlr.location.LocationFactory;
import openlr.location.data.AffectedLines;
import openlr.location.data.Orientation;
import openlr.location.data.SideOfRoad;
import openlr.map.GeoCoordinates;
import openlr.map.GeoCoordinatesImpl;
import openlr.map.InvalidMapDataException;
import openlr.map.Line;
import openlr.map.MapDatabase;
import openlr.map.RectangleCorners;
import openlr.mapviewer.maplayer.RectangleMapLayer;
import openlr.mapviewer.utils.Formatter;

/**
 * The Class LocationTypeRectangle.
 */
public class LocationTypeRectangle extends LocationTypeArea {

	/** The left coordinate. */
	private GeoCoordinates lowerLeftCoord = null;
	/** The right coordinate. */
	private GeoCoordinates upperRightCoord = null;

	/**
	 * {@inheritDoc}
	 */
    @Override
    public final Location createLocation(final String identifier) throws InvalidMapDataException,
            LocationIncompleteException {
        if (upperRightCoord != null && lowerLeftCoord != null) {
            Location loc = LocationFactory.createRectangleLocation(
                    identifier, lowerLeftCoord.getLongitudeDeg(),
                    lowerLeftCoord.getLatitudeDeg(),
                    upperRightCoord.getLongitudeDeg(),
                    upperRightCoord.getLatitudeDeg());
            return loc;
        } else {
            String coordinateMissing;
            if (lowerLeftCoord == null) {
                coordinateMissing = "Lower left";
            } else {
                coordinateMissing = "Upper right";
            }
            throw new LocationIncompleteException(coordinateMissing
                    + " coordinate missing");
        }
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final List<String> getLocationInfo() {
		List<String> info = new ArrayList<String>();
		if (lowerLeftCoord != null) {
			info.add("LowerLeft Point: "
					+ Formatter.COORD_FORMATTER.format(lowerLeftCoord
							.getLongitudeDeg())
					+ "/"
					+ Formatter.COORD_FORMATTER.format(lowerLeftCoord
							.getLatitudeDeg()));
		}
		if (upperRightCoord != null) {
			info.add("UpperRight Point: "
					+ Formatter.COORD_FORMATTER.format(upperRightCoord
							.getLongitudeDeg())
					+ "/"
					+ Formatter.COORD_FORMATTER.format(upperRightCoord
							.getLatitudeDeg()));
		}
		if (areaLocCoveredLines != null && !areaLocCoveredLines.isEmpty()) {
			info.add("+++++ covered Lines ++++++++++ ");
			int count = 1;
			for (Line l : areaLocCoveredLines) {
				info.add(count + ") Line ID: " + l.getID());
				count++;
			}
		}
		if (areaLocIntersectedLines != null
				&& !areaLocIntersectedLines.isEmpty()) {
			info.add("+++++ Intersected Lines ++++++++++ ");
			int count = 1;
			for (Line l : areaLocIntersectedLines) {
				info.add(count + ") Line ID: " + l.getID());
				count++;
			}
		}
		return info;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final MapLayer getLocationMapLayer() {
		return new RectangleMapLayer(lowerLeftCoord,
				upperRightCoord);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean supportsRowsColumns() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setRowsColumns(final int rows, final int columns) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int getColumns() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int getRows() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean supportsUpperRightCoord() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public final void setUpperRightCoord(final GeoCoordinates ur) {
		upperRightCoord = ur;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final GeoCoordinates getUpperRightCoord() {
		return upperRightCoord;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean supportsLowerLeftCoord() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setLowerLeftCoord(final GeoCoordinates ll) {
		lowerLeftCoord = ll;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final GeoCoordinates getLowerLeftCoord() {
		return lowerLeftCoord;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean supportsRadius() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setRadius(final long radius) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean supportsLine() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void addOrRemoveLine(final Line l) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean supportsOrientation() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setOrientation(final Orientation o) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean supportsSideOfRoad() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setSideOfRoad(final SideOfRoad s) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setLocation(final Location loc)
			throws InvalidMapDataException {
		if (loc.getLocationType() == LocationType.RECTANGLE) {
			lowerLeftCoord = loc.getLowerLeftPoint();
			upperRightCoord = loc.getUpperRightPoint();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean supportsGeoCoordinatePoi() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setGeoCoordinatePoi(final double lon, final double lat)
			throws InvalidMapDataException {
		if (lowerLeftCoord != null && upperRightCoord != null) {
			return;
		}
		if (lowerLeftCoord == null) {
			lowerLeftCoord = new GeoCoordinatesImpl(lon, lat);
		} else {
			GeoCoordinates newCoord = new GeoCoordinatesImpl(lon, lat);
			RectangleCorners rc = LocationHelper.orderCoordinates(
					lowerLeftCoord, newCoord);
			lowerLeftCoord = rc.getLowerLeft();
			upperRightCoord = rc.getUpperRight();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean supportsNegOffset() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setNegOffset(final int no) throws InvalidMapDataException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setPosOffset(final int po) throws InvalidMapDataException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean supportsPosOffset() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean offsetsSelectable() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setNegOffset(final double lon, final double lat)
			throws InvalidMapDataException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setPosOffset(final double lon, final double lat)
			throws InvalidMapDataException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Orientation getOrientation() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final SideOfRoad getSideOfRoad() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
    final void updateCoverage(final MapDatabase map)
            throws InvalidMapDataException, OpenLRDecoderProcessingException {
		areaLocCoveredLines.clear();
		areaLocIntersectedLines.clear();
        if (lowerLeftCoord != null && upperRightCoord != null) {
            RectangleCoverage coverage = new RectangleCoverage(lowerLeftCoord,
                    upperRightCoord);
            AffectedLines affected = coverage.getAffectedLines(map);
            areaLocCoveredLines.addAll(affected.getCoveredLines());
            areaLocIntersectedLines.addAll(affected.getIntersectedLines());
        }
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int getPosOffset() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int getNegOffset() {
		throw new UnsupportedOperationException();
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public LocationType getLocationType() {
        return LocationType.RECTANGLE;
    }	
}
