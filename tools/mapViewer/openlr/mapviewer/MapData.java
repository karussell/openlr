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

import openlr.geomap.JMapDataRepository;
import openlr.map.MapDatabase;
import openlr.mapviewer.gui.layer.MapLayerStore;

/**
 * The Class MapData.
 */
public class MapData {

	/** The map store. */
	private final MapLayerStore mapStore;

	/** The mdb. */
	private final MapDatabase mdb;

	/** The map name. */
	private final String mapName;

	/** The repo. */
	private final JMapDataRepository repo;

	/**
	 * Instantiates a new map data.
	 *
	 * @param mStore the m store
	 * @param map the map
	 * @param mName the m name
	 * @param mRepo the m repo
	 */
	public MapData(final MapLayerStore mStore, final MapDatabase map,
			final String mName, final JMapDataRepository mRepo) {
		mapStore = mStore;
		mdb = map;
		mapName = mName;
		repo = mRepo;
	}

	/**
	 * Gets the map store.
	 * 
	 * @return the mapStore
	 */
	public final MapLayerStore getMapLayerStore() {
		return mapStore;
	}

	/**
	 * Gets the mdb.
	 * 
	 * @return the mdb
	 */
	public final MapDatabase getMapDatabase() {
		return mdb;
	}

	/**
	 * Gets the map name.
	 * 
	 * @return the mapName
	 */
	public final String getMapName() {
		return mapName;
	}

	/**
	 * Gets the repo.
	 * 
	 * @return the repo
	 */
	public final JMapDataRepository getRepo() {
		return repo;
	}
	


}
