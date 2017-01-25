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
import openlr.mapviewer.maplayer.PointAlongMapLayer;
import openlr.mapviewer.utils.LineNameResolver;

/**
 * The Class LocationTypePointAlong.
 */
public class LocationTypePointAlong extends AbstractLocationType {

	/** The line. */
	private Line line;

	/** The poff. */
	private int poff;

	/** The poff coord. */
	private GeoCoordinates poffCoord;

	/** The side of road. */
	private SideOfRoad sideOfRoad = SideOfRoad.getDefault();

	/** The orientation. */
	private Orientation orientation = Orientation.getDefault();


	/**
	 * {@inheritDoc}
	 */
	@Override
    public final Location createLocation(final String identifier)
            throws InvalidMapDataException, LocationIncompleteException {
        if (line != null) {
            Location loc = LocationFactory
                    .createPointAlongLineLocationWithSideAndOrientation(
                            identifier, line, poff, sideOfRoad,
                            orientation);
            return loc;
        } else {
            throw new LocationIncompleteException(
                    "Location not valid, line not set");
        }
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final List<String> getLocationInfo() {
		List<String> info = new ArrayList<String>();
		if (line != null) {
			String s = Long.toString(line.getID());
			String s1 = "";
			Map<Locale, List<String>> names = line.getNames();
			if (names != null && !names.isEmpty()) {
				s1 = LineNameResolver.resolveLineName(names);
			}
			info.add(s + " (" + s1 + ")");
			if (poff > 0) {
				info.add("pos. Offset: " + poff + "m");
			}
		}
		return info;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final MapLayer getLocationMapLayer() {
		return new PointAlongMapLayer(line, poffCoord);
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
	public final void addOrRemoveLine(final Line l) {
		if (line != l) {
			line = l;
			poff = -1;
			poffCoord = null;
			lastAction = "line added";
		} else {
			line = null;
			poff = -1;
			poffCoord = null;
			lastAction = "line removed";
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean supportsOrientation() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setOrientation(final Orientation o) {
		orientation = o;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean supportsSideOfRoad() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setSideOfRoad(final SideOfRoad s) {
		sideOfRoad = s;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setLocation(final Location loc)
			throws InvalidMapDataException {
		if (loc.getLocationType() == LocationType.POINT_ALONG_LINE) {
			line = loc.getPoiLine();
			setPosOffset(loc.getPositiveOffset());
	        sideOfRoad = loc.getSideOfRoad();
	        orientation = loc.getOrientation();
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
	public final void setGeoCoordinatePoi(final double lon, final double lat)
			throws InvalidMapDataException {
		throw new UnsupportedOperationException();
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
	public final void setNegOffset(final int no) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setPosOffset(final int po) throws InvalidMapDataException {
		if (offsetsSelectable()) {
			List<Line> lines = new ArrayList<Line>();
			lines.add(line);
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
		return line != null;
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
		if (offsetsSelectable()) {
			List<Line> lines = new ArrayList<Line>();
			lines.add(line);
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
		return orientation;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final SideOfRoad getSideOfRoad() {
		return sideOfRoad;
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
		throw new UnsupportedOperationException();
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public final LocationType getLocationType() {
        return LocationType.POINT_ALONG_LINE;
    }
}
