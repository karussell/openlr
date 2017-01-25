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
import openlr.mapviewer.MapViewer;

/**
 * The Class GeoCoordinateMapLayer.
 */
public class GeoCoordinateMapLayer extends PropertyObservingMapLayer {

	/** The Constant ID. */
	private static final int ID = 787822;

	/** The position. */
	private final GeoCoordinates position;

	/** The color. */
	private Color colorPoint;

	/** The stroke size. */
	private int strokeSizePoint;

	/**
	 * Instantiates a new geo coordinate map layer.
	 * 
	 * @param repo
	 *            the repo
	 * @param poi
	 *            the poi
	 */
	public GeoCoordinateMapLayer(
			final GeoCoordinates poi) {
		super(ID, "geo coordinate",  MapLayerOrder.TOP);
		position = poi;
		colorPoint = MapViewer.PROPERTIES.getLocationGeoCoordColor();
		strokeSizePoint = MapViewer.PROPERTIES.getNodeStrokeSize();
	}

	/**
	 * Instantiates a new geo coordinate map layer.
	 *
	 * @param repo the repo
	 * @param poi the poi
	 * @param c the c
	 * @param stroke the stroke
	 */
	public GeoCoordinateMapLayer(
			final GeoCoordinates poi, final Color c, final int stroke) {
		super(ID, "geo coordinate",  MapLayerOrder.TOP);
		position = poi;
		colorPoint = c;
		strokeSizePoint = stroke;
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
		g.setStroke(new BasicStroke(strokeSizePoint));
		g.drawLine(p.x - diff, p.y - diff, p.x + diff, p.y + diff);
		g.drawLine(p.x - diff, p.y + diff, p.x + diff, p.y - diff);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void updateProperty(final String changedKey) {
		colorPoint = MapViewer.PROPERTIES.getLocationGeoCoordColor();
		strokeSizePoint = MapViewer.PROPERTIES.getNodeStrokeSize();
	}

}
