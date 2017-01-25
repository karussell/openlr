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
package openlr.mapviewer;

import openlr.map.MapDatabase;
import openlr.mapviewer.gui.layer.MapLayerStore;
import openlr.mapviewer.gui.panels.OpenLRMapPane;
import openlr.mapviewer.location.LocationHolder;
import openlr.mapviewer.utils.observer.ObserverManager;

/**
 * The Class MapsHolder.
 */
public class MapsHolder {

	/**
	 * The Enum MapIndex.
	 */
	public enum MapIndex {

		/** The first map. */
		FIRST_MAP,

		/** The second map. */
		SECOND_MAP;
	}
	    
    /**
     * Stores the currently active location change observers
     */
    private final ObserverManager<MapChangeObserver, MapsHolder> mapChangeListeners;    

	/** flag which map is active */
	private MapIndex activeMap = MapIndex.FIRST_MAP;

	/** The first map data. */
	private final MapData firstMapData;

	/** The second map data. */
	private final MapData secondMapData;

	private final OpenLRMapPane mapPaneFirst;

	private final OpenLRMapPane mapPaneSecond;

	private final LocationHolder locHolderFirst;

	private final LocationHolder locHolderSecond;

	/**
	 * Instantiates a new maps holder.
	 * 
	 * @param mapData
	 *            the map data
	 */
	public MapsHolder(final MapData mapData) {

		this(mapData, null);
	}

	/**
	 * Instantiates a new maps holder.
	 * 
	 * @param mapDataFirst
	 *            the map data first
	 * @param mapDataSecond
	 *            the map data second
	 */
	public MapsHolder(final MapData mapDataFirst, final MapData mapDataSecond) {
	    mapChangeListeners = new ObserverManager<MapChangeObserver, MapsHolder>(
                this);
		firstMapData = mapDataFirst;
		mapPaneFirst = new OpenLRMapPane(mapDataFirst.getMapDatabase(),
				mapDataFirst.getMapLayerStore(), mapDataFirst.getRepo());
		locHolderFirst = new LocationHolder(mapDataFirst);
        if (mapDataSecond != null) {
            secondMapData = mapDataSecond;
            locHolderSecond = new LocationHolder(mapDataSecond);
            mapPaneSecond = new OpenLRMapPane(mapDataSecond.getMapDatabase(),
                    mapDataSecond.getMapLayerStore(), mapDataSecond.getRepo());
        } else {
            secondMapData = null;
            mapPaneSecond = null;
            locHolderSecond = null;
        }
    }

	/**
	 * Checks for single map.
	 * 
	 * @return true, if successful
	 */
	public final boolean hasSingleMap() {
		return secondMapData == null;
	}

	/**
	 * Checks for double map.
	 * 
	 * @return true, if successful
	 */
	public final boolean hasTwoMaps() {
		return secondMapData != null;
	}

	/**
	 * Gets the map database.
	 * 
	 * @param idx
	 *            the idx
	 * @return the map database
	 */
	public final MapDatabase getMapDatabase(final MapIndex idx) {
		if (idx == MapIndex.SECOND_MAP && secondMapData != null) {
			return secondMapData.getMapDatabase();
		} else {
			return firstMapData.getMapDatabase();
		}
	}

	/**
	 * Gets the encoder map name.
	 * 
	 * @param idx
	 *            the idx
	 * @return the file name of the first map
	 */
	public final String getMapName(final MapIndex idx) {
		if (idx == MapIndex.SECOND_MAP && secondMapData != null) {
			return secondMapData.getMapName();
		} else {
			return firstMapData.getMapName();
		}
	}

	/**
	 * Gets the encoder map layer store.
	 * 
	 * @param idx
	 *            the idx
	 * @return the encoder map layer store
	 */
	public final MapLayerStore getMapLayerStore(final MapIndex idx) {
		if (idx == MapIndex.SECOND_MAP && secondMapData != null) {
			return secondMapData.getMapLayerStore();
		} else {
			return firstMapData.getMapLayerStore();
		}
	}

	/**
	 * Gets the encoder map pane.
	 * 
	 * @param idx
	 *            the idx
	 * @return the encoder map pane
	 */
	public final OpenLRMapPane getMapPane(final MapIndex idx) {
		if (idx == MapIndex.SECOND_MAP && mapPaneSecond != null) {
			return mapPaneSecond;
		} else {
			return mapPaneFirst;
		}
	}

	public final LocationHolder getLocationHolder(final MapIndex idx) {
		if (idx == MapIndex.SECOND_MAP && locHolderSecond != null) {
			return locHolderSecond;
		} else {
			return locHolderFirst;
		}
	}
    
	/**
	 * Sets the map active.
	 * 
	 * @param mapIdx
	 *            the new map active
	 */
	public final void setMapActive(final MapIndex mapIdx) {
		if (activeMap != mapIdx) {
		    activeMap = mapIdx;

		    mapChangeListeners.notifyObservers();
		}
	}

	/**
	 * Gets the map active.
	 * 
	 * @return the map active
	 */
	public final MapIndex getMapActive() {
		return activeMap;
	}
	
    /**
     * Adds a map change listener. The sequence the registered listeners get
     * informed is not defined! This will lead to a hard reference from this
     * object to the listener. For memory purposes clients should therefore take
     * care to remove the listener properly if they are intended to shut down!
     * 
     * @param listener
     *            The listener to add
     */
    public final void addMapChangeListener(final MapChangeObserver listener) {
        mapChangeListeners.addObserver(listener);
    }

	/**
	 * Removes a registered map change listener.
	 * @param listener The listener instance to remove
	 */
	public final void removeMapChangeListener(final MapChangeObserver listener) {
	       mapChangeListeners.addObserver(listener);
	}
}
