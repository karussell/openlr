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

import openlr.LocationType;
import openlr.geomap.MapLayer;
import openlr.location.Location;
import openlr.location.data.Orientation;
import openlr.location.data.SideOfRoad;
import openlr.map.GeoCoordinates;
import openlr.map.InvalidMapDataException;
import openlr.map.Line;
import openlr.map.MapDatabase;

/**
 * The Class LocationTypeHandler.
 */
abstract class AbstractLocationType {

	/** The last action. */
	protected String lastAction = "";

	/**
	 * Gets the last action.
	 * 
	 * @return the last action
	 */
	public final String getLastAction() {
		return lastAction;
	}

    /**
     * Build a {@link Location} instance from the current data if all necessary
     * data is available. Throws an exception otherwise. Implementations of this
     * method should never return {@code null}.
     * 
     * @param identifier
     *            The identifier the location object shall get
     * 
     * @return the valid location
     * @throws InvalidMapDataException
     *             the invalid map data exception
     * @throws LocationIncompleteException
     *             If the current data is not complete and a valid
     *             {@link Location} cannot be created.
     */
    public abstract Location createLocation(final String identifier)
            throws InvalidMapDataException, LocationIncompleteException;

    /**
     * Delivers the location type
     * @return The type
     */
    public abstract LocationType getLocationType();
    
	/**
	 * Gets the location info.
	 *
	 * @return the location info
	 */
	public abstract List<String> getLocationInfo();

	/**
	 * Gets the location layer.
	 *
	 * @return the location layer
	 * @throws InvalidMapDataException 
	 */
	public abstract MapLayer getLocationMapLayer() throws InvalidMapDataException;

	/**
	 * Supports rows columns.
	 *
	 * @return true, if successful
	 */
	public abstract boolean supportsRowsColumns();

	/**
	 * Sets the rows columns.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 */
	public abstract void setRowsColumns(int rows, int columns);

	/**
	 * Gets the columns.
	 *
	 * @return the columns
	 */
	public abstract int getColumns();

	/**
	 * Gets the rows.
	 *
	 * @return the rows
	 */
	public abstract int getRows();

	/**
	 * Supports upper right coord.
	 *
	 * @return true, if successful
	 */
	public abstract boolean supportsUpperRightCoord();

	/**
	 * Sets the upper right coord.
	 *
	 * @param ur the new upper right coord
	 */
	public abstract void setUpperRightCoord(GeoCoordinates ur);

	/**
	 * Gets the upper right coord.
	 *
	 * @return the upper right coord
	 */
	public abstract GeoCoordinates getUpperRightCoord();

	/**
	 * Supports lower left coord.
	 *
	 * @return true, if successful
	 */
	public abstract boolean supportsLowerLeftCoord();

	/**
	 * Sets the lower left coord.
	 *
	 * @param ll the new lower left coord
	 */
	public abstract void setLowerLeftCoord(GeoCoordinates ll);

	/**
	 * Gets the lower left coord.
	 *
	 * @return the lower left coord
	 */
	public abstract GeoCoordinates getLowerLeftCoord();

	/**
	 * Supports radius.
	 *
	 * @return true, if successful
	 */
	public abstract boolean supportsRadius();

	/**
	 * Sets the radius.
	 *
	 * @param radius the new radius
	 */
	public abstract void setRadius(long radius);

	/**
	 * Supports line.
	 *
	 * @return true, if successful
	 */
	public abstract boolean supportsLine();

	/**
	 * Adds the or remove line.
	 *
	 * @param l the l
	 */
	public abstract void addOrRemoveLine(Line l);

	/**
	 * Supports orientation.
	 *
	 * @return true, if successful
	 */
	public abstract boolean supportsOrientation();

	/**
	 * Sets the orientation.
	 *
	 * @param o the new orientation
	 */
	public abstract void setOrientation(Orientation o);

	/**
	 * Supports side of road.
	 *
	 * @return true, if successful
	 */
	public abstract boolean supportsSideOfRoad();

	/**
	 * Sets the side of road.
	 *
	 * @param s the new side of road
	 */
	public abstract void setSideOfRoad(SideOfRoad s);

	/**
	 * Sets the location.
	 *
	 * @param loc the new location
	 * @throws InvalidMapDataException the invalid map data exception
	 */
	public abstract void setLocation(Location loc) throws InvalidMapDataException;

	/**
	 * Supports geo coordinate poi.
	 *
	 * @return true, if successful
	 */
	public abstract boolean supportsGeoCoordinatePoi();

	/**
	 * Sets the geo coordinate poi.
	 *
	 * @param lon the lon
	 * @param lat the lat
	 * @throws InvalidMapDataException the invalid map data exception
	 */
	public abstract void setGeoCoordinatePoi(double lon, double lat) throws InvalidMapDataException;

	/**
	 * Supports neg offset.
	 *
	 * @return true, if successful
	 */
	public abstract boolean supportsNegOffset();

	/**
	 * Sets the neg offset.
	 *
	 * @param no the new neg offset
	 * @throws InvalidMapDataException 
	 */
	public abstract void setNegOffset(int no) throws InvalidMapDataException;

	/**
	 * Sets the pos offset.
	 *
	 * @param po the new pos offset
	 * @throws InvalidMapDataException the invalid map data exception
	 */
	public abstract void setPosOffset(int po) throws InvalidMapDataException;

	/**
	 * Supports pos offset.
	 *
	 * @return true, if successful
	 */
	public abstract boolean supportsPosOffset();
	
	/**
	 * Gets the pos offset.
	 *
	 * @return the pos offset
	 */
	public abstract int getPosOffset();
	
	/**
	 * Gets the neg offset.
	 *
	 * @return the neg offset
	 */
	public abstract int getNegOffset();

	/**
	 * Offsets selectable.
	 *
	 * @return true, if successful
	 */
	public abstract boolean offsetsSelectable();

	/**
	 * Sets the neg offset.
	 *
	 * @param lon the lon
	 * @param lat the lat
	 * @throws InvalidMapDataException the invalid map data exception
	 */
	public abstract void setNegOffset(double lon, double lat) throws InvalidMapDataException;

	/**
	 * Sets the pos offset.
	 *
	 * @param lon the lon
	 * @param lat the lat
	 * @throws InvalidMapDataException the invalid map data exception
	 */
	public abstract void setPosOffset(double lon, double lat) throws InvalidMapDataException;

	/**
	 * Gets the orientation.
	 *
	 * @return the orientation
	 */
	public abstract Orientation getOrientation();

	/**
	 * Gets the side of road.
	 *
	 * @return the side of road
	 */
	public abstract SideOfRoad getSideOfRoad();
	
	/**
	 * Provides coverage information.
	 *
	 * @return true, if successful
	 */
	public abstract boolean providesCoverageInformation();
	
	/**
	 * Build the Layer for the given lines, this method is relevant to draw the
	 * layer for covered lines as well as intersected line of an area location.
	 *
	 * @param map the map database
	 * @return the layer to be draw
	 * @author DLR. e.V. (LTouk)
	 */
	public abstract MapLayer getCoverageMapLayer(final MapDatabase map);

}
