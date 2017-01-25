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
import java.util.ArrayList;
import java.util.List;

import openlr.LocationType;
import openlr.geomap.JMapDataRepository;
import openlr.geomap.MapLayer;
import openlr.geomap.transform.JMapTransformMercator;
import openlr.location.Location;
import openlr.map.GeoCoordinates;
import openlr.map.Line;
import openlr.mapviewer.MapViewer;

/**
 * The Class AllLocationsMapLayer.
 */
public class AllLocationsMapLayer extends PropertyObservingMapLayer {

	/** The Constant ID. */
	private static final int ID = 444;

	/** The Constant NAME. */
	private static final String NAME = "All locations";

	/** The Constant STROKE_SIZE. */
	private static final int STROKE_SIZE = 2;

	/** The locations. */
	private final List<Location> locations = new ArrayList<Location>();
	
	/**
	 * The color to draw the locations in
	 */
	private Color color;

	/**
	 * Instantiates a new all locations map layer.
	 * 
	 * @param repo
	 *            the repo
	 * @param locs
	 *            the locs
	 */
	public AllLocationsMapLayer(final List<Location> locs) {
		super(ID, NAME, MapLayerOrder.TOP);
		locations.addAll(locs);
		color = MapViewer.PROPERTIES.getLocationsAllColor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void updateProperty(final String changedKey) {
	    
	    color = MapViewer.PROPERTIES.getLocationsAllColor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void draw(final Graphics2D g,
			final JMapTransformMercator transform, final JMapDataRepository repo) {
		for (Location loc : locations) {
			LocationType lt = loc.getLocationType();
			MapLayer ml = null;
			switch (lt) {
			case CIRCLE:
				ml = new CircleMapLayer(loc.getCenterPoint(),
						loc.getRadius(), color, STROKE_SIZE);
				break;
			case CLOSED_LINE:
				ml = new ClosedLineMapLayer(loc.getLocationLines(), color,
						STROKE_SIZE);
				break;
			case GEO_COORDINATES:
				ml = new GeoCoordinateMapLayer(loc.getPointLocation(),
						color, STROKE_SIZE);
				break;
			case GRID:
				ml = new GridMapLayer(loc.getLowerLeftPoint(),
						loc.getUpperRightPoint(), loc.getNumberOfRows(),
						loc.getNumberOfColumns(), color, STROKE_SIZE);
				break;
			case LINE_LOCATION:
                GeoCoordinates pOffCoord = loc.getLocationLines().get(0)
                        .getGeoCoordinateAlongLine(loc.getPositiveOffset());
                Line lastLine = loc.getLocationLines().get(
                        loc.getLocationLines().size() - 1);
                GeoCoordinates nOffCoord = lastLine
                        .getGeoCoordinateAlongLine(lastLine.getLineLength()
                                - loc.getPositiveOffset());
                ml = new LineMapLayer(loc.getLocationLines(), pOffCoord,
                        nOffCoord, color, STROKE_SIZE);
				break;
			case POI_WITH_ACCESS_POINT:
				ml = new PoiAccessMapLayer(loc.getPoiLine(),
						loc.getAccessPoint(), loc.getPointLocation(), color,
						STROKE_SIZE);
				break;
			case POINT_ALONG_LINE:
				ml = new PointAlongMapLayer(loc.getPoiLine(),
						loc.getAccessPoint(), color, STROKE_SIZE);
				break;
			case POLYGON:
				ml = new PolygonMapLayer(loc.getCornerPoints(), color,
						STROKE_SIZE);
				break;
			case RECTANGLE:
				ml = new RectangleMapLayer(loc.getLowerLeftPoint(),
						loc.getUpperRightPoint(), color, STROKE_SIZE);
				break;
			case UNKNOWN:
			default:
			}
			if (ml != null) {
				ml.draw(g, transform, repo);
			}
		}
	}

}
