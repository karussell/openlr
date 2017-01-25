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

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import openlr.geomap.JMapDataRepository;
import openlr.geomap.MapLayer;
import openlr.geomap.transform.JMapTransformMercator;
import openlr.map.FormOfWay;
import openlr.map.Line;
import openlr.map.MapDatabase;
import openlr.mapviewer.MapViewer;

/**
 * The Class FOWMapLayer.
 */
public class FOWMapLayer extends PropertyObservingMapLayer {

	/** The Constant ORDER. */
	private static final MapLayerOrder ORDER = MapLayerOrder.MIDDLE;

	/** The ids. */
	private final Set<Long> ids = new HashSet<Long>();

	/** The color. */
	private Color color;
	
	/** The property string. */
	private final String propertyString;

	/**
	 * Instantiates a new fOW map layer.
	 *
	 * @param id the id
	 * @param name the name
	 * @param mdb the mdb
	 * @param fow the fow
	 * @param visibility the visibility
	 */
	public FOWMapLayer(final int id, final String name,
			final MapDatabase mdb, final FormOfWay fow,
			final boolean visibility) {
		super(id, name, ORDER, visibility);
		Iterator<? extends Line> iter = mdb.getAllLines();
		while (iter.hasNext()) {
			Line l = iter.next();
			if (l.getFOW() == fow) {
				ids.add(l.getID());
			}
		}
		propertyString = MapViewer.PROPERTIES.getFowColorProperty(fow.getID());
		color = MapViewer.PROPERTIES.getColorProperty(propertyString);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void draw(final Graphics2D g, final JMapTransformMercator transform, final JMapDataRepository repo) {
		g.setColor(color);
		for (Long id : ids) {
			drawLine(g, repo.getLine(id).getShapeCoordinates(), transform);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void updateProperty(final String changedKey) {
		if (propertyString.equals(changedKey)) {
			color = MapViewer.PROPERTIES.getColorProperty(propertyString);
		}
	}

}
