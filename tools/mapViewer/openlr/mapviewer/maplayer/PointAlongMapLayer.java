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

import openlr.geomap.JMapDataRepository;
import openlr.geomap.MapLayer;
import openlr.geomap.transform.JMapTransformMercator;
import openlr.map.GeoCoordinates;
import openlr.map.Line;
import openlr.mapviewer.MapViewer;

/**
 * The Class PointAlongMapLayer.
 */
public class PointAlongMapLayer extends PropertyObservingMapLayer {

	/** The Constant ID. */
	private static final int ID = 35343543;

	/** The line. */
	private final Line line;

	/** The pos off. */
	private final GeoCoordinates posOff;

	/** The color line. */
	private Color colorLine;

	/** The color p off. */
	private Color colorPOff;

	/** The color start. */
	private Color colorStart;

	/** The stroke start. */
	private int strokeStart;

	/** The stroke line. */
	private int strokeLine;

	/** The stroke p off. */
	private int strokePOff;

	/**
	 * Instantiates a new point along map layer.
	 * 
	 * @param repo
	 *            the repo
	 * @param l
	 *            the l
	 * @param off
	 *            the off
	 */
	public PointAlongMapLayer(final Line l,
			final GeoCoordinates off) {
		super(ID, "point along", MapLayerOrder.TOP);
		line = l;
		posOff = off;
		colorLine = MapViewer.PROPERTIES.getLocationEncoderColor();
		colorPOff = MapViewer.PROPERTIES.getPosOffColor();
		colorStart = MapViewer.PROPERTIES.getLocationStartNodeColor();
		strokeLine = MapViewer.PROPERTIES.getLineStrokeSize() + 2;
		strokePOff = MapViewer.PROPERTIES.getNodeStrokeSize();
		strokeStart = MapViewer.PROPERTIES.getNodeStrokeSize();
	}

	/**
	 * Instantiates a new point along map layer.
	 * 
	 * @param repo
	 *            the repo
	 * @param l
	 *            the l
	 * @param off
	 *            the off
	 * @param c
	 *            the c
	 * @param stroke
	 *            the stroke
	 */
	public PointAlongMapLayer(final Line l,
			final GeoCoordinates off, final Color c, final int stroke) {
		super(ID, "point along", MapLayerOrder.TOP);
		line = l;
		posOff = off;
		colorLine = c;
		colorPOff = c;
		colorStart = c;
		strokeLine = stroke;
		strokePOff = stroke;
		strokeStart = stroke;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void draw(final Graphics2D g,
			final JMapTransformMercator transform, final JMapDataRepository repo) {
		if (line != null) {
			g.setColor(colorLine);
			g.setStroke(new BasicStroke(strokeLine));
			drawLine(g, repo.getLine(line.getID()).getShapeCoordinates(), transform);
			// Start node
			Point p = transform.getPixel(line.getStartNode().getLongitudeDeg(),
					line.getStartNode().getLatitudeDeg());
			int diff = strokeStart / 2;
			g.setColor(colorStart);
			g.setStroke(new BasicStroke(strokeStart));
			g.drawOval(p.x - diff, p.y - diff, strokeStart, strokeStart);
			if (posOff != null) {
				// posOff
				p = transform.getPixel(posOff.getLongitudeDeg(),
						posOff.getLatitudeDeg());
				diff = strokePOff / 2;
				g.setColor(colorPOff);
				g.setStroke(new BasicStroke(strokePOff));
				g.drawOval(p.x - diff, p.y - diff, strokePOff, strokePOff);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void updateProperty(final String changedKey) {
		colorLine = MapViewer.PROPERTIES.getLocationEncoderColor();
		colorPOff = MapViewer.PROPERTIES.getPosOffColor();
		colorStart = MapViewer.PROPERTIES.getLocationStartNodeColor();
		strokeLine = MapViewer.PROPERTIES.getLineStrokeSize() + 2;
		strokePOff = MapViewer.PROPERTIES.getNodeStrokeSize();
		strokeStart = MapViewer.PROPERTIES.getNodeStrokeSize();
	}

}
