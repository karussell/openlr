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
import java.util.Locale;
import java.util.Map;

import openlr.LocationType;
import openlr.geomap.MapLayer;
import openlr.location.Location;
import openlr.location.LocationFactory;
import openlr.location.data.Orientation;
import openlr.location.data.SideOfRoad;
import openlr.map.GeoCoordinates;
import openlr.map.GeoCoordinatesImpl;
import openlr.map.InvalidMapDataException;
import openlr.map.Line;
import openlr.map.MapDatabase;
import openlr.mapviewer.location.LocationHelper.OffsetData;
import openlr.mapviewer.maplayer.LineMapLayer;
import openlr.mapviewer.utils.LineNameResolver;

/**
 * The Class LocationTypeLine.
 */
public class LocationTypeLine extends AbstractLocationType {

	/** The location. */
	private List<Line> lines = new ArrayList<Line>();

	/** The poff. */
	private int poff;

	/** The noff. */
	private int noff;

	/** The poff coord. */
	private GeoCoordinates poffCoord;

	/** The noff coord. */
	private GeoCoordinates noffCoord;


	/**
	 * {@inheritDoc}
	 */
	@Override
    public final Location createLocation(final String identifier) throws 
            LocationIncompleteException {
        if (!lines.isEmpty()) {
            Location loc = LocationFactory.createLineLocationWithOffsets(
                    identifier, lines, poff, noff);
            return loc;
        } else {
            throw new LocationIncompleteException(
                    "Location not valid, no lines set");
        }
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final List<String> getLocationInfo() {
		List<String> info = new ArrayList<String>();
		int count = 1;
		int locLength = 0;
		for (Line l : lines) {
			String s = Long.toString(l.getID());
			String s1 = "";
			Map<Locale, List<String>> names = l.getNames();
			if (names != null && !names.isEmpty()) {
				s1 = LineNameResolver.resolveLineName(names);
			}
			info.add(count + ") " + s + " (" + s1 + ")");
			count++;
			locLength += l.getLineLength();
		}
		if (!lines.isEmpty()) {
			info.add("Length of location: " + locLength + "m");
		}
		if (poff > 0) {
			info.add("pos. Offset: " + poff + "m");
		}
		if (noff > 0) {
			info.add("neg. Offset: " + noff + "m");
		}
		return info;
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
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setUpperRightCoord(final GeoCoordinates ur) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final GeoCoordinates getUpperRightCoord() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean supportsLowerLeftCoord() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setLowerLeftCoord(final GeoCoordinates ll) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final GeoCoordinates getLowerLeftCoord() {
		throw new UnsupportedOperationException();
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
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void addOrRemoveLine(final Line line) {
		if (line == null) {
			lastAction = "";
		}
		if (lines.isEmpty()) {
			lines.add(line);
			lastAction = "line added";
		} else if (LocationHelper.checkConnectionEnd(lines, line)) {
			lines.add(line);
			lastAction = "line added (end)";
		} else if (LocationHelper.isLastLine(lines, line)) {
			lines.remove(lines.size() - 1);
			if (lines.isEmpty()) {
				poffCoord = null;
				poff = 0;
				noffCoord = null;
				noff = 0;
			}
			lastAction = "line removed (end)";
		} else if (LocationHelper.checkConnectionStart(lines, line)) {

			lines.add(0, line);
			lastAction = "line added (front)";
		} else if (LocationHelper.isFirstLine(lines, line)) {
			lines.remove(0);
			if (lines.isEmpty()) {
				poffCoord = null;
				poff = 0;
				noffCoord = null;
				noff = 0;
			}
			lastAction = "line removed (start)";
		} else {
			lastAction = "not connected";
		}
		lastAction = "";
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
		if (loc.getLocationType() == LocationType.LINE_LOCATION) {
			lines.clear();
			lines.addAll(loc.getLocationLines());
			setNegOffset(loc.getNegativeOffset());
			setPosOffset(loc.getPositiveOffset());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean supportsGeoCoordinatePoi() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean supportsNegOffset() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setNegOffset(final int no) throws InvalidMapDataException {
		if (offsetsSelectable()) {
			OffsetData offsets = LocationHelper.calculateNegOffCoord(lines, no);
			noff = offsets.getDistance();
			noffCoord = offsets.getGeoCoord();
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setPosOffset(final int po) throws InvalidMapDataException {
		if (offsetsSelectable()) {
			OffsetData offsets = LocationHelper.calculatePosOffCoord(lines, po);
			poff = offsets.getDistance();
			poffCoord = offsets.getGeoCoord();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean supportsPosOffset() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean offsetsSelectable() {
		return !lines.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setNegOffset(final double lon, final double lat)
			throws InvalidMapDataException {
		if (offsetsSelectable()) {
			OffsetData offsets = LocationHelper.calculateNegOffInMeter(lines,
					new GeoCoordinatesImpl(lon, lat));
			noff = offsets.getDistance();
			noffCoord = offsets.getGeoCoord();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setPosOffset(final double lon, final double lat)
			throws InvalidMapDataException {
		if (offsetsSelectable()) {
			OffsetData offsets = LocationHelper.calculatePosOffInMeter(lines,
					new GeoCoordinatesImpl(lon, lat));
			poff = offsets.getDistance();
			poffCoord = offsets.getGeoCoord();
		}
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
	public final MapLayer getLocationMapLayer() {
	    GeoCoordinates poffCoordPassed = null;
	    GeoCoordinates noffCoordPassed = null;
	    if (poff > 0) {
	        poffCoordPassed = poffCoord;
	    }
	    if (noff > 0) {
	        noffCoordPassed = noffCoord;
	    }
		return new LineMapLayer(lines, poffCoordPassed, noffCoordPassed);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setGeoCoordinatePoi(final double lon, final double lat)
			throws InvalidMapDataException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean providesCoverageInformation() {
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final MapLayer getCoverageMapLayer(final MapDatabase map) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int getPosOffset() {
		return poff;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int getNegOffset() {
		return noff;
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public LocationType getLocationType() {
        return LocationType.LINE_LOCATION;
    }
}
