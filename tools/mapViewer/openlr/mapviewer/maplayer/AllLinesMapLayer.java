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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import openlr.geomap.JMapDataRepository;
import openlr.geomap.transform.JMapTransformMercator;
import openlr.map.Line;
import openlr.mapviewer.MapViewer;

/**
 * The Class AllLinesMapLayer.
 */
public class AllLinesMapLayer extends PropertyObservingMapLayer {

	/** The color. */
	private Color color;

	/** The stroke size. */
	private int strokeSize;

	/**
	 * Instantiates a new all lines map layer.
	 * 
	 * @param id
	 *            the id
	 */
	public AllLinesMapLayer(final int id) {
		super(id, "Lines", MapLayerOrder.BOTTOM);
		color = MapViewer.PROPERTIES.getLineColor();
		strokeSize = MapViewer.PROPERTIES.getLineStrokeSize();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void draw(final Graphics2D g,
			final JMapTransformMercator transform, final JMapDataRepository repo) {
		g.setColor(color);
		g.setStroke(new BasicStroke(strokeSize));
		for (Line ls : repo.getLines()) {
			drawLine(g, ls.getShapeCoordinates(), transform);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void updateProperty(final String changedKey) {
		color = MapViewer.PROPERTIES.getLineColor();
		strokeSize = MapViewer.PROPERTIES.getLineStrokeSize();
	}

}
