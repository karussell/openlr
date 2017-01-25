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
package openlr.geomap;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import openlr.geomap.transform.JMapTransformMercator;

/**
 * The Class MapLayerHandler.
 */
public class MapLayerHandler {

	/** The registered layers. */
	private final List<MapLayer> registeredLayers = new ArrayList<MapLayer>();

	/**
	 * Register.
	 * 
	 * @param layer
	 *            the layer
	 */
	public final void register(final MapLayer layer) {
		registeredLayers.add(layer);
		Collections.sort(registeredLayers, new MapLayer.MapLayerComparator());
	}

	/**
	 * Unregister.
	 * 
	 * @param id
	 *            the id
	 */
	public final void unregister(final int id) {
		if (registeredLayers.size() > id && id >= 0) {
			registeredLayers.remove(id);
		}
	}

	/**
	 * Unregister.
	 * 
	 * @param layer
	 *            the layer
	 */
	public final void unregister(final MapLayer layer) {
		registeredLayers.remove(layer);
	}

	/**
	 * Sets the all unvisible.
	 */
	public final void setAllUnvisible() {
		for (MapLayer layer : registeredLayers) {
			layer.setVisible(false);
		}
	}

	/**
	 * Draw layer.
	 * 
	 * @param g
	 *            the g
	 * @param transform
	 *            the transform
	 */
	public final void drawLayer(final Graphics2D g,
			final JMapTransformMercator transform, final JMapDataRepository repo) {
		for (MapLayer layer : registeredLayers) {
			if (layer.isVisible()) {
				layer.draw(g, transform, repo);
			}
		}
	}
}
