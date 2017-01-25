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
import openlr.mapviewer.MapViewer;

/**
 * The Class RectangleMapLayer.
 */
public class RectangleMapLayer extends PropertyObservingMapLayer {
	
	/** The Constant ID. */
	private static final int ID = 3576;
	
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
	
	/**
	 * Instantiates a new rectangle map layer.
	 *
	 * @param repo the repo
	 * @param ll the ll
	 * @param ur the ur
	 */
	public RectangleMapLayer(final GeoCoordinates ll, final GeoCoordinates ur) {
		super(ID, "rectangle", MapLayer.MapLayerOrder.TOP);
		lowerLeft = ll;
		upperRight = ur;
		colorLine = MapViewer.PROPERTIES.getLocationEncoderColor();
		colorCoord = MapViewer.PROPERTIES.getLocationGeoCoordColor();
		strokeLine = MapViewer.PROPERTIES.getLineStrokeSize() + 2;
		strokeCoord = MapViewer.PROPERTIES.getNodeStrokeSize();
	}

	/**
	 * Instantiates a new rectangle map layer.
	 *
	 * @param repo the repo
	 * @param ll the ll
	 * @param ur the ur
	 * @param c the c
	 * @param stroke the stroke
	 */
	public RectangleMapLayer(
			final GeoCoordinates ll, final GeoCoordinates ur,
			final Color c, final int stroke) {
		super(ID, "rectangle", MapLayer.MapLayerOrder.TOP);
		lowerLeft = ll;
		upperRight = ur;
		colorLine = c;
		colorCoord = c;
		strokeLine = stroke;
		strokeCoord = stroke;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void draw(final Graphics2D g, final JMapTransformMercator transform, final JMapDataRepository repo) {
		if (upperRight == null) {
			double lon = lowerLeft.getLongitudeDeg();
			double lat = lowerLeft.getLatitudeDeg();
			Point p = transform.getPixel(lon, lat);
			int diff = strokeCoord / 2;
			g.setColor(colorCoord);
			g.setStroke(new BasicStroke(strokeCoord));
			g.drawLine(p.x - diff, p.y - diff, p.x + diff, p.y + diff);
			g.drawLine(p.x - diff, p.y + diff, p.x + diff, p.y - diff);
		} else {
			double lon = lowerLeft.getLongitudeDeg();
			double lat = lowerLeft.getLatitudeDeg();
			Point pLL = transform.getPixel(lon, lat);
			int diff = strokeCoord / 2;
			g.setColor(colorCoord);
			g.setStroke(new BasicStroke(strokeCoord));
			g.drawLine(pLL.x - diff, pLL.y - diff, pLL.x + diff, pLL.y + diff);
			g.drawLine(pLL.x - diff, pLL.y + diff, pLL.x + diff, pLL.y - diff);
			lon = upperRight.getLongitudeDeg();
			lat = upperRight.getLatitudeDeg();
			Point pUR = transform.getPixel(lon, lat);
			g.drawLine(pUR.x - diff, pUR.y - diff, pUR.x + diff, pUR.y + diff);
			g.drawLine(pUR.x - diff, pUR.y + diff, pUR.x + diff, pUR.y - diff);
			try {
				RectangleCorners corners = new RectangleCorners(lowerLeft, upperRight);
				Point pUL = transform.getPixel(corners.getUpperLeft().getLongitudeDeg(), corners.getUpperLeft().getLatitudeDeg());
				Point pLR = transform.getPixel(corners.getLowerRight().getLongitudeDeg(), corners.getLowerRight().getLatitudeDeg());
				g.setColor(colorLine);
				g.setStroke(new BasicStroke(strokeLine));
				g.drawLine(pLL.x, pLL.y, pUL.x, pUL.y);
				g.drawLine(pUL.x, pUL.y, pUR.x, pUR.y);
				g.drawLine(pUR.x, pUR.y, pLR.x, pLR.y);
				g.drawLine(pLR.x, pLR.y, pLL.x, pLL.y);
			} catch (InvalidMapDataException e) {
				e.printStackTrace();
			}
		}
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
