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
import openlr.mapviewer.maplayer.GeoCoordinateMapLayer;
import openlr.mapviewer.utils.Formatter;

/**
 * The Class LocationTypeGeoCoordinate.
 */
public class LocationTypeGeoCoordinate extends AbstractLocationType {
	
	
	 /** The poi. */
    private GeoCoordinates poi;

 	/**
     * {@inheritDoc}
     */
    @Override
    public final Location createLocation(final String identifier) throws InvalidMapDataException,
            LocationIncompleteException {
        if (poi != null) {
            Location loc = LocationFactory.createGeoCoordinateLocation(
                    identifier, poi.getLongitudeDeg(),
                    poi.getLatitudeDeg());
            return loc;
        } else {
            throw new LocationIncompleteException("Geo coordinate missing");
        }
    }

	/**
     * {@inheritDoc}
     */
	@Override
	public final List<String> getLocationInfo() {
		List<String> info = new ArrayList<String>();
		if (poi != null) {
			info.add("geo coord: "
					+ Formatter.COORD_FORMATTER.format(poi.getLongitudeDeg())
					+ "/"
					+ Formatter.COORD_FORMATTER.format(poi.getLatitudeDeg()));
		}
		return info;
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public final MapLayer getLocationMapLayer() {
		return new GeoCoordinateMapLayer(poi);
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
		throw new UnsupportedOperationException();
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
		throw new UnsupportedOperationException();	}

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
	public final void setLocation(final Location loc) {
		if (loc.getLocationType() == LocationType.GEO_COORDINATES) {
			poi = loc.getPointLocation();
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
		poi = new GeoCoordinatesImpl(lon, lat);		
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
	public final void setPosOffset(final int po) {
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
        return LocationType.GEO_COORDINATES;
    }

}
