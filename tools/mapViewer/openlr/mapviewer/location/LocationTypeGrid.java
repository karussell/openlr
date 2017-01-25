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
import openlr.decoder.worker.coverage.GridCoverage;
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
import openlr.mapviewer.maplayer.GridMapLayer;
import openlr.mapviewer.utils.Formatter;

/**
 * The Class LocationTypeGrid.
 */
public class LocationTypeGrid extends LocationTypeArea {

	/** The Constant DEFAULT_ROWS. */
	private static final int DEFAULT_ROWS = 3;

	/** The Constant DEFAULT_COLUMNS. */
	private static final int DEFAULT_COLUMNS = 2;

	/** The left coordinate. */
	private GeoCoordinates lowerLeftCoord = null;
	/** The right coordinate. */
	private GeoCoordinates upperRightCoord = null;

	/** the number of rows for grid location default value is 2. */
	private int rows = DEFAULT_ROWS;

	/** the number of columns for grid location default value is 2. */
	private int columns = DEFAULT_COLUMNS;

	/**
	 * {@inheritDoc}
	 */
	@Override
    public final Location createLocation(final String identifier)
            throws InvalidMapDataException, LocationIncompleteException {
	    
        if (lowerLeftCoord != null && upperRightCoord != null && rows > 0
                && columns > 0) {
            Location loc = LocationFactory.createGridLocationFromBasisCell(
                    identifier, lowerLeftCoord.getLongitudeDeg(),
                    lowerLeftCoord.getLatitudeDeg(),
                    upperRightCoord.getLongitudeDeg(),
                    upperRightCoord.getLatitudeDeg(), columns, rows);
            return loc;
        } else {
            throw new LocationIncompleteException(createIncompleteLocError());
        }
    }
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final List<String> getLocationInfo() {
		List<String> info = new ArrayList<String>();
		info.add("NROWS:" + rows + " NCOLS:" + columns);
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
	public final MapLayer getLocationMapLayer()
			throws InvalidMapDataException {
		return new GridMapLayer(lowerLeftCoord,
				upperRightCoord, rows, columns);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean supportsRowsColumns() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setRowsColumns(final int r, final int c) {
		rows = r;
		columns = c;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int getColumns() {
		return columns;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int getRows() {
		return rows;
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
		if (loc.getLocationType() == LocationType.GRID) {
			lowerLeftCoord = loc.getLowerLeftPoint();
			upperRightCoord = loc.getUpperRightPoint();
			rows = loc.getNumberOfRows();
			columns = loc.getNumberOfColumns();
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
    protected final void updateCoverage(final MapDatabase map)
            throws InvalidMapDataException, OpenLRDecoderProcessingException {
		areaLocCoveredLines.clear();
		areaLocIntersectedLines.clear();
        if (lowerLeftCoord != null && upperRightCoord != null) {
            GridCoverage coverage = new GridCoverage(lowerLeftCoord,
                    upperRightCoord, columns, rows);
            AffectedLines affected = coverage.getAffectedLines(map);
            areaLocCoveredLines.addAll(affected.getCoveredLines());
            areaLocIntersectedLines.addAll(affected.getIntersectedLines());
        }

	}
	
    /**
     * Creates the error message describing which parameter is missing on a
     * complete location
     * 
     * @return The error message
     */
    private String createIncompleteLocError() {

        String result;
        if (lowerLeftCoord == null) {
            result = "Lower left coordinate missing";
        } else if (upperRightCoord == null) {
            result = "Upper right coordinate missing";
        } else if (rows < 1) {
            result = "Number of rows not valid: " + rows;

        } else if (columns < 1) {
            result = "Number of columns not valid: " + columns;
        } else {
            throw new IllegalStateException(
                    "Unhandled state of incomopleteness.");
        }

        return result;
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
    public final LocationType getLocationType() {
        return LocationType.GRID;
    }	
}
