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
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import openlr.geomap.JMapDataRepository;
import openlr.geomap.MapLayer;
import openlr.geomap.transform.JMapTransformMercator;
import openlr.map.GeoCoordinates;
import openlr.mapviewer.MapViewer;

/**
 * The Class PolygonMapLayer.
 */
public class PolygonMapLayer extends PropertyObservingMapLayer {

	/** The Constant ID. */
	private static final int ID = 53465468;

	/** The position. */
	private final List<GeoCoordinates> positions;

	/** The color. */
	private Color colorPoint;

	/** The stroke size. */
	private int strokeSizePoint;

	/** The color. */
	private Color colorLine;

	/** The stroke size. */
	private int strokeSizeLine;

	/**
	 * Instantiates a new polygon map layer.
	 * 
	 * @param repo
	 *            the repo
	 * @param coords
	 *            the coords
	 */
	public PolygonMapLayer(
			final List<? extends GeoCoordinates> coords) {
		super(ID, "polygon", MapLayerOrder.TOP);
		positions = new ArrayList<GeoCoordinates>();
		positions.addAll(coords);
		colorPoint = MapViewer.PROPERTIES.getLocationGeoCoordColor();
		strokeSizePoint = MapViewer.PROPERTIES.getNodeStrokeSize();
		colorLine = MapViewer.PROPERTIES.getLocationEncoderColor();
		strokeSizeLine = MapViewer.PROPERTIES.getLineStrokeSize() + 2;
	}

	/**
	 * Instantiates a new polygon map layer.
	 *
	 * @param repo the repo
	 * @param coords the coords
	 * @param c the c
	 * @param stroke the stroke
	 */
	public PolygonMapLayer(
			final List<? extends GeoCoordinates> coords, final Color c,
			final int stroke) {
		super(ID, "polygon", MapLayerOrder.TOP);
		positions = new ArrayList<GeoCoordinates>();
		positions.addAll(coords);
		colorPoint = c;
		strokeSizePoint = stroke;
		colorLine = c;
		strokeSizeLine = stroke;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void draw(final Graphics2D g,
			final JMapTransformMercator transform, final JMapDataRepository repo) {
		Point prev = null;
		Point first = null;
		Stroke pointStroke = new BasicStroke(strokeSizePoint);
		Stroke lineStroke = new BasicStroke(strokeSizeLine);
		for (GeoCoordinates gc : positions) {
			Point current = transform.getPixel(gc.getLongitudeDeg(),
					gc.getLatitudeDeg());
			int diff = strokeSizePoint / 2;
			g.setColor(colorPoint);
			g.setStroke(pointStroke);
			g.drawLine(current.x - diff, current.y - diff, current.x + diff,
					current.y + diff);
			g.drawLine(current.x - diff, current.y + diff, current.x + diff,
					current.y - diff);
			if (positions.size() > 2) {
				if (first == null) {
					first = current;
				} else {
					g.setColor(colorLine);
					g.setStroke(lineStroke);
					g.drawLine(prev.x, prev.y, current.x, current.y);
				}
				prev = current;
			}
		}
		if (first != null && prev != null) {
			g.setColor(colorLine);
			g.setStroke(lineStroke);
			g.drawLine(prev.x, prev.y, first.x, first.y);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void updateProperty(final String changedKey) {
		colorPoint = MapViewer.PROPERTIES.getLocationGeoCoordColor();
		strokeSizePoint = MapViewer.PROPERTIES.getNodeStrokeSize();
		colorLine = MapViewer.PROPERTIES.getLocationEncoderColor();
		strokeSizeLine = MapViewer.PROPERTIES.getLineStrokeSize() + 2;
	}

}
