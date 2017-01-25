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
import openlr.map.RectangleCorners;
import openlr.map.utils.GeometryUtils;
import openlr.mapviewer.MapViewer;

/**
 * The Class GridMapLayer.
 */
public class GridMapLayer extends PropertyObservingMapLayer {

	/** The Constant ID. */
	private static final int ID = 3577;

	/** The color line. */
	private Color colorLine;

	/** The color n off. */
	private Color colorCoord;

	/** The lower left. */
	private final GeoCoordinates lowerLeft;

	/** The upper right. */
	private final GeoCoordinates upperRight;

	/** The stroke line. */
	private int strokeLine;

	/** The stroke coord. */
	private int strokeCoord;

	/** The n rows. */
	private final int nRows;

	/** The n columns. */
	private final int nColumns;

	/**
	 * Instantiates a new grid map layer.
	 * 
	 * @param repo
	 *            the repo
	 * @param ll
	 *            the ll
	 * @param ur
	 *            the ur
	 * @param rows
	 *            the rows
	 * @param columns
	 *            the columns
	 */
	public GridMapLayer(final GeoCoordinates ll,
			final GeoCoordinates ur, final int rows, final int columns) {
		super(ID, "rectangle",  MapLayer.MapLayerOrder.TOP);
		lowerLeft = ll;
		upperRight = ur;
		nRows = rows;
		nColumns = columns;
		colorLine = MapViewer.PROPERTIES.getLocationEncoderColor();
		colorCoord = MapViewer.PROPERTIES.getLocationGeoCoordColor();
		strokeLine = MapViewer.PROPERTIES.getLineStrokeSize() + 2;
		strokeCoord = MapViewer.PROPERTIES.getNodeStrokeSize();
	}

	/**
	 * Instantiates a new grid map layer.
	 * 
	 * @param repo
	 *            the repo
	 * @param ll
	 *            the ll
	 * @param ur
	 *            the ur
	 * @param rows
	 *            the rows
	 * @param columns
	 *            the columns
	 * @param c
	 *            the c
	 * @param stroke
	 *            the stroke
	 */
	public GridMapLayer(final GeoCoordinates ll,
			final GeoCoordinates ur, final int rows, final int columns,
			Color c, final int stroke) {
		super(ID, "rectangle", MapLayer.MapLayerOrder.TOP);
		lowerLeft = ll;
		upperRight = ur;
		nRows = rows;
		nColumns = columns;
		colorLine = c;
		colorCoord = c;
		strokeLine = stroke;
		strokeCoord = stroke;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void draw(final Graphics2D g,
			final JMapTransformMercator transform, final JMapDataRepository repo) {
		if (lowerLeft != null) {
			if (upperRight == null) {
				double lon = lowerLeft.getLongitudeDeg();
				double lat = lowerLeft.getLatitudeDeg();
				Point p = transform.getPixel(lon, lat);
				int diff = strokeCoord / 2;
				g.setColor(colorCoord);
				g.setStroke(new BasicStroke(strokeCoord));
				drawCross(g, p, diff);
			} else {
				double lon = lowerLeft.getLongitudeDeg();
				double lat = lowerLeft.getLatitudeDeg();
				Point pLL = transform.getPixel(lon, lat);
				int diff = strokeCoord / 2;
				lon = upperRight.getLongitudeDeg();
				lat = upperRight.getLatitudeDeg();
				Point pUR = transform.getPixel(lon, lat);
				try {
					RectangleCorners corners = new RectangleCorners(lowerLeft,
							upperRight);
					GeoCoordinates upperLeft = corners.getUpperLeft();
					GeoCoordinates lowerRight = corners.getLowerRight();
					Point pUL = transform.getPixel(upperLeft.getLongitudeDeg(),
							upperLeft.getLatitudeDeg());
					Point pLR = transform.getPixel(
							lowerRight.getLongitudeDeg(),
							lowerRight.getLatitudeDeg());
					// base rectangle
					g.setColor(colorLine);
					g.setStroke(new BasicStroke(strokeLine));
					g.drawLine(pLL.x, pLL.y, pUL.x, pUL.y);
					g.drawLine(pUL.x, pUL.y, pUR.x, pUR.y);
					g.drawLine(pUR.x, pUR.y, pLR.x, pLR.y);
					g.drawLine(pLR.x, pLR.y, pLL.x, pLL.y);
					double distNorth = GeometryUtils.distance(
							lowerLeft.getLongitudeDeg(),
							lowerLeft.getLatitudeDeg(),
							upperLeft.getLongitudeDeg(),
							upperLeft.getLatitudeDeg());
					double distEast = GeometryUtils.distance(
							lowerLeft.getLongitudeDeg(),
							lowerLeft.getLatitudeDeg(),
							lowerRight.getLongitudeDeg(),
							lowerRight.getLatitudeDeg());
					for (int i = 1; i <= nRows; i++) {
						GeoCoordinates nextNorth = GeometryUtils
								.determineCoordinateInDistance(
										lowerLeft.getLongitudeDeg(),
										lowerLeft.getLatitudeDeg(), 0,
										(i * distNorth) / 1000.);
						Point pNorth = transform.getPixel(
								nextNorth.getLongitudeDeg(),
								nextNorth.getLatitudeDeg());
						drawCross(g, pNorth, diff);
						for (int j = 1; j <= nColumns; j++) {
							GeoCoordinates nextEast = GeometryUtils
									.determineCoordinateInDistance(
											lowerLeft.getLongitudeDeg(),
											lowerLeft.getLatitudeDeg(), 90,
											(j * distEast) / 1000.0);
							Point pEast = transform.getPixel(
									nextEast.getLongitudeDeg(),
									nextEast.getLatitudeDeg());
							drawCross(g, pEast, diff);
							Point midPoint = new Point(pEast.x, pNorth.y);
							drawCross(g, midPoint, diff);
						}
					}
				} catch (InvalidMapDataException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Draw cross.
	 * 
	 * @param g
	 *            the g
	 * @param p
	 *            the p
	 * @param diff
	 *            the diff
	 */
	private void drawCross(final Graphics2D g, final Point p, final int diff) {
		g.drawLine(p.x - diff, p.y - diff, p.x + diff, p.y + diff);
		g.drawLine(p.x - diff, p.y + diff, p.x + diff, p.y - diff);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void updateProperty(final String changedKey) {
		colorLine = MapViewer.PROPERTIES.getLocationEncoderColor();
		colorCoord = MapViewer.PROPERTIES.getLocationGeoCoordColor();
		strokeLine = MapViewer.PROPERTIES.getLineStrokeSize() + 2;
		strokeCoord = MapViewer.PROPERTIES.getNodeStrokeSize();
	}

}
