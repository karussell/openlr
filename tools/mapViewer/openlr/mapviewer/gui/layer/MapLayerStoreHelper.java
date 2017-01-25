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
package openlr.mapviewer.gui.layer;

import java.util.List;

import openlr.geomap.MapLayer;
import openlr.map.FormOfWay;
import openlr.map.FunctionalRoadClass;
import openlr.map.MapDatabase;
import openlr.mapviewer.maplayer.AllLinesMapLayer;
import openlr.mapviewer.maplayer.AllNodesMapLayer;
import openlr.mapviewer.maplayer.FOWMapLayer;
import openlr.mapviewer.maplayer.FRCMapLayer;
import cern.colt.map.OpenIntObjectHashMap;

/**
 * The Class MapLayerStoreHelper.
 */
public final class MapLayerStoreHelper {
	
	/**
	 * Utility class shall not be instantiated.
	 */
	private MapLayerStoreHelper() {
		throw new UnsupportedOperationException();
	}

	/** The next id. */
	private static int nextID;

	/**
	 * Creates the frc layer.
	 *
	 * @param map the map
	 * @return the open int object hash map
	 */
	public static OpenIntObjectHashMap createFRCLayer(final MapDatabase map) {
		List<FunctionalRoadClass> countFRC = FunctionalRoadClass.getFRCs();
		OpenIntObjectHashMap layers = new OpenIntObjectHashMap(
				FunctionalRoadClass.getFRCs().size());
		for (FunctionalRoadClass frc : countFRC) {
			FRCMapLayer frcLayer = new FRCMapLayer(getNextLayerID(),
					frc.name(), map, frc, false);
			layers.put(frc.getID(), frcLayer);
		}
		return layers;
	}
	
	/**
	 * Creates the fow layer.
	 *
	 * @param map the map
	 * @return the open int object hash map
	 */
	public static OpenIntObjectHashMap createFOWLayer(final MapDatabase map) {
		List<FormOfWay> countFOW = FormOfWay.getFOWs();
		OpenIntObjectHashMap layers = new OpenIntObjectHashMap(
				FormOfWay.getFOWs().size());
		for (FormOfWay fow : countFOW) {
			FOWMapLayer fowLayer = new FOWMapLayer(getNextLayerID(), fow.name(), map,
					fow, false);
			layers.put(fow.getID(), fowLayer);
		}
		return layers;
	}
	
	/**
	 * Creates the lines layer.
	 *
	 * @param map the map
	 * @return the map layer
	 */
	public static MapLayer createLinesLayer() {		
		MapLayer lineLayer = new AllLinesMapLayer(getNextLayerID());
		return lineLayer;
	}
	
	/**
	 * Creates the nodes layer.
	 *
	 * @param map the map
	 * @return the map layer
	 */
	public static MapLayer createNodesLayer() {
		MapLayer lineLayer = new AllNodesMapLayer(getNextLayerID());
		return lineLayer;
	}

	/**
	 * Gets the next layer id.
	 *
	 * @return the next layer id
	 */
	public static int getNextLayerID() {
		return nextID++;
	}

}
