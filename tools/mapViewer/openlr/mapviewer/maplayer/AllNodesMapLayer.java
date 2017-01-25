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
package openlr.mapviewer.maplayer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Collection;

import openlr.geomap.JMapDataRepository;
import openlr.geomap.transform.JMapTransformMercator;
import openlr.map.Node;
import openlr.mapviewer.MapViewer;

/**
 * The Class AllNodesMapLayer.
 */
public class AllNodesMapLayer extends PropertyObservingMapLayer {

	/** The color. */
	private Color color;

	/** The node size. */
	private int nodeSize;
	


	/**
	 * Instantiates a new all nodes map layer.
	 *
	 * @param layerID the layer id

	 */
	public AllNodesMapLayer(final int layerID) {
		super(layerID, "Nodes", MapLayerOrder.BOTTOM, false);
		color = MapViewer.PROPERTIES.getNodeColor();
		nodeSize = MapViewer.PROPERTIES.getNodeMarkSize();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void draw(final Graphics2D g,
			final JMapTransformMercator transform, final JMapDataRepository repo) {
		g.setColor(color);
		int diff = nodeSize / 2;
		Collection<Node> nodes = repo.getNodes();
		for (Node n : nodes) {
			Point p = transform.getPixel(n.getLongitudeDeg(),
					n.getLatitudeDeg());
			g.fillOval(p.x - diff, p.y - diff, nodeSize, nodeSize);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void updateProperty(final String changedKey) {
		color = MapViewer.PROPERTIES.getNodeColor();
		nodeSize = MapViewer.PROPERTIES.getNodeMarkSize();
	}

}
