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
import openlr.geomap.MapLayer;
import openlr.geomap.transform.JMapTransformMercator;
import openlr.map.Line;
import openlr.mapviewer.MapViewer;

/**
 * The Class CoverageMapLayer.
 */
public class CoverageMapLayer extends PropertyObservingMapLayer {

	/** The Constant ID. */
	private static final int ID = 1001;

	/** The covered. */
	private final List<Line> covered = new ArrayList<Line>();

	/** The intersected. */
	private final List<Line> intersected = new ArrayList<Line>();

	private Color coveredColor;
	private int coveredStroke;
	private Color intersectedColor;
	private int intersectedStroke;

	/**
	 * Instantiates a new coverage map layer.
	 * 
	 * @param map
	 *            the map
	 * @param coveredL
	 *            the covered l
	 * @param intersectedL
	 *            the intersected l
	 */
	public CoverageMapLayer(
			final List<Line> coveredL, final List<Line> intersectedL) {
		super(ID, "coveredLines", MapLayer.MapLayerOrder.MIDDLE);
		covered.addAll(coveredL);
		intersected.addAll(intersectedL);
		coveredColor = MapViewer.PROPERTIES.getAreaLocationCoveredLinesColor();
		intersectedColor = MapViewer.PROPERTIES
				.getAreaLocationIntersectedLinesColor();
		coveredStroke = 2;
		intersectedStroke = 2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void draw(final Graphics2D g,
			final JMapTransformMercator transform, final JMapDataRepository repo) {
		g.setColor(coveredColor);
		g.setStroke(new BasicStroke(coveredStroke));
		for (Line l : covered) {
			drawLine(g, repo.getLine(l.getID()).getShapeCoordinates(), transform);
		}
		g.setColor(intersectedColor);
		g.setStroke(new BasicStroke(intersectedStroke));
		for (Line l : intersected) {
			drawLine(g, repo.getLine(l.getID()).getShapeCoordinates(), transform);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void updateProperty(final String changedKey) {
		coveredColor = MapViewer.PROPERTIES.getAreaLocationCoveredLinesColor();
		intersectedColor = MapViewer.PROPERTIES
				.getAreaLocationIntersectedLinesColor();
		coveredStroke = 2;
		intersectedStroke = 2;
	}

}
