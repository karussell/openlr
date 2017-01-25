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
import java.util.ArrayList;
import java.util.List;

import openlr.geomap.JMapDataRepository;
import openlr.geomap.transform.JMapTransformMercator;
import openlr.map.Line;
import openlr.mapviewer.MapViewer;

/**
 * The Class LineNameSearchMapLayer.
 */
public class LineNameSearchMapLayer extends SearchMapLayer {
	
	/** The Constant NAME. */
	private static final String NAME = "Line name search layer";
	
	/** The lines. */
	private final List<Line> lines;
	
	/** The color line. */
	private Color colorLine;
	
	/** The stroke line. */
	private int strokeLine;

	/**
	 * Instantiates a new line name search map layer.
	 *
	 * @param ls the ls
	 * @param repo the repo
	 */
	public LineNameSearchMapLayer(final List<Line> ls) {
		super(NAME);
		lines = new ArrayList<Line>();
		lines.addAll(ls);
		colorLine = MapViewer.PROPERTIES.getSearchColor();
		strokeLine = MapViewer.PROPERTIES.getLineStrokeSize() + 2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void draw(final Graphics2D g, final JMapTransformMercator transform, final JMapDataRepository repo) {
		g.setColor(colorLine);
		g.setStroke(new BasicStroke(strokeLine));
		for (Line l : lines) {
			drawLine(g, repo.getLine(l.getID()).getShapeCoordinates(), transform);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void updateProperty(final String changedKey) {
		colorLine = MapViewer.PROPERTIES.getSearchColor();
		strokeLine = MapViewer.PROPERTIES.getLineStrokeSize() + 2;		
	}

}
