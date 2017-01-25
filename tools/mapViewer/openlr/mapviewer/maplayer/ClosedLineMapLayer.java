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
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import openlr.geomap.JMapDataRepository;
import openlr.geomap.MapLayer;
import openlr.geomap.transform.JMapTransformMercator;
import openlr.map.Line;
import openlr.mapviewer.MapViewer;

/**
 * The Class ClosedLineMapLayer.
 */
public class ClosedLineMapLayer extends PropertyObservingMapLayer {

	/** The Constant ID. */
	private static final int ID = 47876;

	/** The lines. */
	private final List<Line> lines;

	/** The color line. */
	private Color colorLine;

	/** The color start. */
	private Color colorStart;

	/** The stroke start. */
	private int strokeStart;

	/** The stroke line. */
	private int strokeLine;

	/**
	 * Instantiates a new closed line map layer.
	 * 
	 * @param repo
	 *            the repo
	 * @param ls
	 *            the ls
	 */
	public ClosedLineMapLayer(final List<? extends Line> ls) {
		super(ID, "closed line", MapLayer.MapLayerOrder.TOP);
		lines = new ArrayList<Line>();
		lines.addAll(ls);
		colorLine = MapViewer.PROPERTIES.getLocationEncoderColor();
		colorStart = MapViewer.PROPERTIES.getLocationStartNodeColor();
		strokeLine = MapViewer.PROPERTIES.getLineStrokeSize() + 2;
		strokeStart = MapViewer.PROPERTIES.getNodeStrokeSize();
	}

	/**
	 * Instantiates a new closed line map layer.
	 *
	 * @param repo the repo
	 * @param ls the ls
	 * @param c the c
	 * @param stroke the stroke
	 */
	public ClosedLineMapLayer(
			final List<? extends Line> ls, final Color c, final int stroke) {
		super(ID, "closed line", MapLayer.MapLayerOrder.TOP);
		lines = new ArrayList<Line>();
		lines.addAll(ls);
		colorLine = c;
		colorStart = c;
		strokeLine = stroke;
		strokeStart = stroke;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void draw(final Graphics2D g,
			final JMapTransformMercator transform, JMapDataRepository repo) {
		if (!lines.isEmpty()) {
			g.setColor(colorLine);
			g.setStroke(new BasicStroke(strokeLine));
			for (Line l : lines) {
				drawLine(g, repo.getLine(l.getID()).getShapeCoordinates(), transform);
			}
			// Start node
			Point p = transform.getPixel(lines.get(0).getStartNode()
					.getLongitudeDeg(), lines.get(0).getStartNode()
					.getLatitudeDeg());
			int diff = strokeStart / 2;
			g.setColor(colorStart);
			g.setStroke(new BasicStroke(strokeStart));
			g.drawOval(p.x - diff, p.y - diff, strokeStart, strokeStart);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void updateProperty(final String changedKey) {
		colorLine = MapViewer.PROPERTIES.getLocationEncoderColor();
		colorStart = MapViewer.PROPERTIES.getLocationStartNodeColor();
		strokeLine = MapViewer.PROPERTIES.getLineStrokeSize() + 2;
		strokeStart = MapViewer.PROPERTIES.getNodeStrokeSize();
	}

}
