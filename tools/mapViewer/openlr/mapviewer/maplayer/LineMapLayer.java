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
import openlr.map.GeoCoordinates;
import openlr.map.Line;
import openlr.mapviewer.MapViewer;

/**
 * The Class LineMapLayer.
 */
public class LineMapLayer extends PropertyObservingMapLayer {

	/** The Constant ID. */
	private static final int ID = 47875;

	/** The lines. */
	private final List<Line> lines;

	/** The pos off. */
	private final GeoCoordinates posOff;

	/** The neg off. */
	private final GeoCoordinates negOff;

	/** The color line. */
	private Color colorLine;

	/** The color n off. */
	private Color colorNOff;

	/** The color p off. */
	private Color colorPOff;

	/** The color start. */
	private Color colorStart;

	/** The stroke start. */
	private int strokeStart;

	/** The stroke line. */
	private int strokeLine;

	/** The stroke n off. */
	private int strokeNOff;

	/** The stroke p off. */
	private int strokePOff;

	/**
	 * Instantiates a new line map layer.
	 * 
	 * @param repo
	 *            the repo
	 * @param ls
	 *            the ls
	 * @param pOff
	 *            the off, if null then ignored
	 * @param nOff
	 *            the n off, if null then ignored
	 */
	public LineMapLayer(final List<? extends Line> ls,
			final GeoCoordinates pOff, final GeoCoordinates nOff) {
		super(ID, "line", MapLayer.MapLayerOrder.TOP);
		lines = new ArrayList<Line>();
		lines.addAll(ls);
		posOff = pOff;
		negOff = nOff;
		setDrawingAttributesFromProperties();
	}

	/**
	 * Instantiates a new line map layer.
	 *
	 * @param ls the ls
	 * @param pOff the off
	 * @param nOff the n off
	 * @param c the c
	 * @param stroke the stroke
	 */
	public LineMapLayer(
			final List<? extends Line> ls, final GeoCoordinates pOff,
			final GeoCoordinates nOff, final Color c, final int stroke) {
		super(ID, "line", MapLayer.MapLayerOrder.TOP);
		lines = new ArrayList<Line>();
		lines.addAll(ls);
		posOff = pOff;
		negOff = nOff;
		colorLine = c;
		colorPOff = c;
		colorNOff = c;
		colorStart = c;
		strokeLine = stroke;
		strokeNOff = stroke;
		strokePOff = stroke;
		strokeStart = stroke;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void draw(final Graphics2D g,
			final JMapTransformMercator transform, final JMapDataRepository repo) {
		g.setColor(colorLine);
		g.setStroke(new BasicStroke(strokeLine));
		for (Line l : lines) {
			drawLine(g, repo.getLine(l.getID()).getShapeCoordinates(), transform);
		}
        if (!lines.isEmpty()) {
            Line firstLine = lines.get(0);
            Point startNode = transform.getPixel(firstLine.getStartNode()
                    .getLongitudeDeg(), firstLine.getStartNode()
                    .getLatitudeDeg());
            int diff = strokeStart / 2;
            g.setColor(colorStart);
            g.setStroke(new BasicStroke(strokeStart));
            g.drawOval(startNode.x - diff, startNode.y - diff, strokeStart, strokeStart);
        }
		if (posOff != null) {
			Point posOffPoint = transform.getPixel(posOff.getLongitudeDeg(),
					posOff.getLatitudeDeg());
			int diff = strokePOff / 2;
			g.setColor(colorPOff);
			g.setStroke(new BasicStroke(strokePOff));
			g.drawOval(posOffPoint.x - diff, posOffPoint.y - diff, strokePOff, strokePOff);
		}
		if (negOff != null) {			
		    Point negOffPoint = transform.getPixel(negOff.getLongitudeDeg(),
					negOff.getLatitudeDeg());
			int diff = strokeNOff / 2;
			g.setColor(colorNOff);
			g.setStroke(new BasicStroke(strokeNOff));
			g.drawOval(negOffPoint.x - diff, negOffPoint.y - diff, strokeNOff, strokeNOff);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void updateProperty(final String changedKey) {
	    setDrawingAttributesFromProperties();
	}

	/**
	 * (Re)sets all the drawing parameters from the MapViewer properties
	 */
    private void setDrawingAttributesFromProperties() {
        colorLine = MapViewer.PROPERTIES.getLocationEncoderColor();
        colorPOff = MapViewer.PROPERTIES.getPosOffColor();
        colorNOff = MapViewer.PROPERTIES.getNegOffColor();
        colorStart = MapViewer.PROPERTIES.getLocationStartNodeColor();
        strokeLine = MapViewer.PROPERTIES.getLineStrokeSize() + 2;
        strokeNOff = MapViewer.PROPERTIES.getNodeStrokeSize();
        strokePOff = MapViewer.PROPERTIES.getNodeStrokeSize();
        strokeStart = MapViewer.PROPERTIES.getNodeStrokeSize();
    }
    
}
