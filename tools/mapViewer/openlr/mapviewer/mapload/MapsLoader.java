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
package openlr.mapviewer.mapload;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ServiceLoader;

import openlr.geomap.JMapDataRepository;
import openlr.geomap.MapLayer;
import openlr.map.InvalidMapDataException;
import openlr.map.MapDatabase;
import openlr.map.loader.MapLoadParameter;
import openlr.map.loader.OpenLRMapLoader;
import openlr.map.loader.OpenLRMapLoaderException;
import openlr.mapviewer.MapData;
import openlr.mapviewer.MapViewerException;
import openlr.mapviewer.MapsHolder;
import openlr.mapviewer.gui.layer.MapLayerStore;
import openlr.mapviewer.gui.layer.MapLayerStoreHelper;
import openlr.mapviewer.mapload.MapLoadDialog.MapNumber;
import openlr.mapviewer.mapload.MapLoadDialog.ReturnCode;
import cern.colt.map.OpenIntObjectHashMap;

/**
 * The Class MapsLoader.
 */
public class MapsLoader {

	/** The Constant MAP_LOAD_STEPS_PER_MAP. */
	private static final int MAP_LOAD_STEPS_PER_MAP = 6;

	/** The loader. */
	private final List<OpenLRMapLoader> loader = new ArrayList<OpenLRMapLoader>();

	/** The load dialog. */
	private final MapLoadDialog loadDialog;

	/**
	 * Instantiates a new maps loader.
	 * 
	 * @throws MapViewerException
	 *             the map viewer exception
	 */
	public MapsLoader() throws MapViewerException {
		ServiceLoader<OpenLRMapLoader> encoders = ServiceLoader
				.load(OpenLRMapLoader.class);
		for (OpenLRMapLoader l : encoders) {
			loader.add(l);
		}
		if (loader.isEmpty()) {
			throw new MapViewerException("No map loader found!");
		}		
		loadDialog = new MapLoadDialog(loader);
	}

	/**
	 * Gets the nr of map loader.
	 * 
	 * @return the nr of map loader
	 */
	public final int getNrOfMapLoader() {
		return loader.size();
	}

	/**
	 * Load maps.
	 *
	 * @return the maps holder
	 * @throws InvalidMapDataException the invalid map data exception
	 * @throws MapViewerException the map viewer exception
	 */
	public final MapsHolder loadMaps() throws InvalidMapDataException,
			MapViewerException {
		loadDialog.setModal(true);
		loadDialog.setVisible(true);
		if (loadDialog.getReturnCode() == ReturnCode.ABORT) {
			return null;
		}
		MapsHolder mapsHolder = null;
		if (!loadDialog.twoMapsSelected()) {
			mapsHolder = loadSingleMap();

		} else {
			mapsHolder = loadTwoMaps();
		}
		return mapsHolder;
	}

	/**
	 * Load single map.
	 *
	 * @return the maps holder
	 * @throws InvalidMapDataException the invalid map data exception
	 * @throws MapViewerException the map viewer exception
	 */
	private MapsHolder loadSingleMap() throws InvalidMapDataException,
			MapViewerException {
		MapLoadProgressInfo loadProgress = new MapLoadProgressInfo(
				MAP_LOAD_STEPS_PER_MAP);
		loadProgress.setVisible(true);
		Collection<MapLoadParameter> params = loadDialog
				.getParameters(MapNumber.ONE);
		OpenLRMapLoader selectedLoader = loadDialog
				.getMapLoaderSelectionIndex(MapNumber.ONE);
		MapData data = loadMap(selectedLoader, params,
				loadDialog.getMapName(MapNumber.ONE), loadProgress, null, true);
		return new MapsHolder(data);
	}

	/**
	 * Load map.
	 *
	 * @param currentLoader the loader
	 * @param params the params
	 * @param name the name
	 * @param loadProgress the load progress
	 * @param mapIdentifier the map identifier
	 * @param lastMap the last map
	 * @return the maps holder
	 * @throws InvalidMapDataException the invalid map data exception
	 */
	private MapData loadMap(final OpenLRMapLoader currentLoader,
			final Collection<MapLoadParameter> params, final String name,
			final MapLoadProgressInfo loadProgress, final String mapIdentifier,
			final boolean lastMap) throws InvalidMapDataException {
		MapDatabase mdb = null;
		MapLayerStore mapStore = null;
		String mapName = name;
		try {
			loadProgress.setStepStarting(prepareCurrentTaskString(
					"Loading map data", mapIdentifier));
			mdb = currentLoader.load(params);
			if (mapName == null || mapName.isEmpty()) {
				mapName = currentLoader.getMapDescriptor();
			}
			loadProgress.setStepFinished();
			if (!loadProgress.isVisible()) {
				return null;
			}
			mapStore = new MapLayerStore(createLinesMapLayer(loadProgress,
					mapIdentifier), createNodesMapLayer(loadProgress,
					mapIdentifier), createFRCLayer(mdb, loadProgress,
					mapIdentifier), createFOWLayer(mdb, loadProgress,
					mapIdentifier));
		} catch (MapViewerException e) {
			e.printStackTrace();
			return null;
		} catch (OpenLRMapLoaderException e) {
			e.printStackTrace();
			return null;
		}
		if (!loadProgress.isVisible()) {
			return null;
		}
		loadProgress.setStepStarting(prepareCurrentTaskString(
				"Preparing shapes", mapIdentifier));
		JMapDataRepository repo = new JMapDataRepository(mdb);
		loadProgress.setStepFinished();
		if (!loadProgress.isVisible()) {
			return null;
		}
		if (lastMap) {
			loadProgress.setVisible(false);
		}
		return new MapData(mapStore, mdb, mapName, repo);
	}

	/**
	 * Prepare current task string.
	 * 
	 * @param current
	 *            the current
	 * @param mapIdentifier
	 *            the map identifier
	 * @return the string
	 */
	private String prepareCurrentTaskString(final String current,
			final String mapIdentifier) {
		StringBuilder sb = new StringBuilder();
		sb.append(current);
		if (mapIdentifier != null && !mapIdentifier.isEmpty()) {
			sb.append(" (").append(mapIdentifier).append(")");
		}
		sb.append("...");
		return sb.toString();
	}

	/**
	 * Load two maps.
	 *
	 * @return the maps holder
	 * @throws InvalidMapDataException the invalid map data exception
	 */
	private MapsHolder loadTwoMaps() throws InvalidMapDataException {
		MapLoadProgressInfo loadProgress = new MapLoadProgressInfo(
				2 * MAP_LOAD_STEPS_PER_MAP);
		loadProgress.setVisible(true);
		//load first map completely
		Collection<MapLoadParameter> params1 = loadDialog
				.getParameters(MapNumber.ONE);
		OpenLRMapLoader selectedLoader1 = loadDialog
				.getMapLoaderSelectionIndex(MapNumber.ONE);
		MapData data1 = loadMap(selectedLoader1, params1,
				loadDialog.getMapName(MapNumber.ONE), loadProgress, "first map", false);
		//load second map
		Collection<MapLoadParameter> params2 = loadDialog
				.getParameters(MapNumber.TWO);
		OpenLRMapLoader selectedLoader2 = loadDialog
				.getMapLoaderSelectionIndex(MapNumber.TWO);
		MapData data2 = loadMap(selectedLoader2, params2,
				loadDialog.getMapName(MapNumber.TWO), loadProgress,
				"second map", true);
		return new MapsHolder(data1, data2);
	}

	/**
	 * Creates the lines map layer.
	 * 
	 * @param loadProgress
	 *            the load progress
	 * @param mapIdentifier
	 *            the map identifier
	 * @return the map layer
	 */
	private MapLayer createLinesMapLayer(
			final MapLoadProgressInfo loadProgress, final String mapIdentifier) {
		loadProgress.setStepStarting(prepareCurrentTaskString(
				"Creating all lines layer", mapIdentifier));
		MapLayer layer = MapLayerStoreHelper.createLinesLayer();
		loadProgress.setStepFinished();
		return layer;
	}

	/**
	 * Creates the nodes map layer.
	 * 
	 * @param loadProgress
	 *            the load progress
	 * @param mapIdentifier
	 *            the map identifier
	 * @return the map layer
	 */
	private MapLayer createNodesMapLayer(
			final MapLoadProgressInfo loadProgress, final String mapIdentifier) {
		loadProgress.setStepStarting(prepareCurrentTaskString(
				"Creating all nodes layer", mapIdentifier));
		MapLayer layer = MapLayerStoreHelper.createNodesLayer();
		loadProgress.setStepFinished();
		return layer;
	}

	/**
	 * Creates the fow layer.
	 * 
	 * @param map
	 *            the map
	 * @param loadProgress
	 *            the load progress
	 * @param mapIdentifier
	 *            the map identifier
	 * @return the open int object hash map
	 */
	private OpenIntObjectHashMap createFOWLayer(final MapDatabase map,
			final MapLoadProgressInfo loadProgress, final String mapIdentifier) {
		loadProgress.setStepStarting(prepareCurrentTaskString(
				"Creating FOW layers", mapIdentifier));
		OpenIntObjectHashMap layers = MapLayerStoreHelper.createFOWLayer(map);
		loadProgress.setStepFinished();
		return layers;
	}

	/**
	 * Creates the frc layer.
	 * 
	 * @param map
	 *            the map
	 * @param loadProgress
	 *            the load progress
	 * @param mapIdentifier
	 *            the map identifier
	 * @return the open int object hash map
	 */
	private OpenIntObjectHashMap createFRCLayer(final MapDatabase map,
			final MapLoadProgressInfo loadProgress, final String mapIdentifier) {
		loadProgress.setStepStarting(prepareCurrentTaskString(
				"Creating FRC layers", mapIdentifier));
		OpenIntObjectHashMap layers = MapLayerStoreHelper.createFRCLayer(map);
		loadProgress.setStepFinished();
		return layers;
	}

}
