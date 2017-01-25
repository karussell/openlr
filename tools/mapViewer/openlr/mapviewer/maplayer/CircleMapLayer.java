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
import openlr.map.InvalidMapDataException;
import openlr.map.utils.GeometryUtils;
import openlr.mapviewer.MapViewer;

/**
 * The Class CircleMapLayer.
 */
public class CircleMapLayer extends PropertyObservingMapLayer {

	/** The Constant STEP_SIZE. */
	private static final int STEP_SIZE = 2;

	/** The Constant METER_PER_KILOMETER. */
	private static final double METER_PER_KILOMETER = 1000.0;

	/** The Constant LAYER_ID. */
	private static final int LAYER_ID = 123;

	/** The position. */
	private final GeoCoordinates position;

	/** The radius. */
	private final long radius;

	/** The color. */
	private Color colorPoint;

	/** The stroke size. */
	private int strokeSizePoint;

	/** The color. */
	private Color colorBoundary;

	/** The stroke size. */
	private int strokeSizeBoundary;

	/**
	 * Instantiates a new circle map layer.
	 * 
	 * @param repo
	 *            the repo
	 * @param pos
	 *            the pos
	 * @param rad
	 *            the rad
	 */
	public CircleMapLayer(
			final GeoCoordinates pos, final long rad) {
		super(LAYER_ID, "circle", MapLayer.MapLayerOrder.TOP);
		position = pos;
		radius = rad;
		colorPoint = MapViewer.PROPERTIES.getLocationGeoCoordColor();
		strokeSizePoint = MapViewer.PROPERTIES.getNodeStrokeSize();
		colorBoundary = Color.red;
		strokeSizeBoundary = 4;
	}
	
	/**
	 * Instantiates a new circle map layer.
	 *
	 * @param repo the repo
	 * @param pos the pos
	 * @param rad the rad
	 * @param c the c
	 * @param stroke the stroke
	 */
	public CircleMapLayer(
			final GeoCoordinates pos, final long rad, final Color c, final int stroke) {
		super(LAYER_ID, "circle", MapLayer.MapLayerOrder.TOP);
		position = pos;
		radius = rad;
		colorPoint = c;
		strokeSizePoint = stroke;
		colorBoundary = c;
		strokeSizeBoundary = stroke;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void draw(final Graphics2D g,
			final JMapTransformMercator transform, final JMapDataRepository repo) {
		double lon = position.getLongitudeDeg();
		double lat = position.getLatitudeDeg();
		Point p = transform.getPixel(lon, lat);
		int diff = strokeSizePoint / 2;
		g.setColor(colorPoint);
		g.drawOval(p.x - diff, p.y - diff, strokeSizePoint, strokeSizePoint);
		try {
			Point prev = null;
			Point first = null;
			g.setColor(colorBoundary);
			g.setStroke(new BasicStroke(strokeSizeBoundary));
			for (int i = 0; i < GeometryUtils.FULL_CIRCLE_DEGREE; i += STEP_SIZE) {
				GeoCoordinates coord = GeometryUtils
						.determineCoordinateInDistance(lon, lat, i,
								radius / METER_PER_KILOMETER);
				Point next = transform.getPixel(coord.getLongitudeDeg(), coord.getLatitudeDeg());
				if (first == null) {
					first = next;
				} else {
					g.drawLine(prev.x, prev.y, next.x, next.y);
				}
				prev = next;
			}
			g.drawLine(prev.x, prev.y, first.x, first.y);
		} catch (InvalidMapDataException e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void updateProperty(final String changedKey) {
		colorPoint = MapViewer.PROPERTIES.getLocationGeoCoordColor();
		strokeSizePoint = MapViewer.PROPERTIES.getNodeStrokeSize();
		colorBoundary = Color.red;
		strokeSizeBoundary = 4;
	}

}
