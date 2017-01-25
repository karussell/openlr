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
package openlr.geomap;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Comparator;
import java.util.List;

import openlr.geomap.transform.JMapTransformMercator;
import openlr.map.GeoCoordinates;

/**
 * The Class MapLayer.
 */
public abstract class MapLayer {

	/** The is visible. */
	protected boolean isVisible;

	/** The layer id. */
	private final int layerID;

	/** The layer name. */
	private final String layerName;

	/** The order. */
	private final MapLayerOrder order;

	/**
	 * Instantiates a new map layer.
	 * 
	 * @param id
	 *            the id
	 * @param name
	 *            the name
	 * @param o
	 *            the o
	 */
	public MapLayer(final int id, final String name, final MapLayerOrder o) {
		this(id, name, o, true);
	}

	/**
	 * Instantiates a new map layer.
	 * 
	 * @param id
	 *            the id
	 * @param name
	 *            the name
	 * @param o
	 *            the o
	 * @param visibility
	 *            the visibility
	 */
	public MapLayer(final int id, final String name, final MapLayerOrder o,
			final boolean visibility) {
		layerID = id;
		layerName = name;
		order = o;
		isVisible = visibility;
	}

	/**
	 * Draw.
	 * 
	 * @param g
	 *            the g
	 * @param transform
	 *            the transform
	 */
	public abstract void draw(final Graphics2D g,
			JMapTransformMercator transform, JMapDataRepository repo);

	/**
	 * Checks if is visible.
	 * 
	 * @return true, if is visible
	 */
	public final boolean isVisible() {
		return isVisible;
	}

	/**
	 * Sets the visible.
	 * 
	 * @param visible
	 *            the new visible
	 */
	public final void setVisible(final boolean visible) {
		isVisible = visible;
	}

	/**
	 * Gets the order.
	 * 
	 * @return the order
	 */
	public final MapLayerOrder getOrder() {
		return order;
	}

	/**
	 * Gets the layer id.
	 * 
	 * @return the layer id
	 */
	public final int getLayerID() {
		return layerID;
	}

	/**
	 * Gets the layer name.
	 * 
	 * @return the layer name
	 */
	public final String getLayerName() {
		return layerName;
	}

	/**
	 * Draw line.
	 * 
	 * @param g
	 *            the g
	 * @param l
	 *            the l
	 * @param transform
	 *            the transform
	 */
	protected final void drawLine(final Graphics2D g,
			final List<GeoCoordinates> l, final JMapTransformMercator transform) {
		for (int i = 0; i < l.size() - 1; i++) {
			Point p0 = transform.getPixel(l.get(i).getLongitudeDeg(), l.get(i)
					.getLatitudeDeg());
			Point p1 = transform.getPixel(l.get(i + 1).getLongitudeDeg(), l
					.get(i + 1).getLatitudeDeg());
			g.drawLine(p0.x, p0.y, p1.x, p1.y);
		}
	}
			

	/**
	 * Draw line.
	 * 
	 * @param g
	 *            the g
	 * @param l
	 *            the l
	 * @param transform
	 *            the transform
	 */
//	protected final void drawLine(final Graphics2D g, final Line l,
//			final JMapTransformMercator transform) {
//		Path2D.Double shape = l.getShape();
//		PathIterator iter = shape.getPathIterator(null);
//		double[] coords = new double[6];
//		Point p0;
//		Point prev = null;
//		while (!iter.isDone()) {
//			iter.currentSegment(coords);
//			iter.next();
//			p0 = transform.getPoint(coords[0], coords[1]);
//			if (prev != null) {
//				g.drawLine(prev.x, prev.y, p0.x, p0.y);
//			}
//			prev = p0;
//		}
//	}

	/**
	 * The Enum MapLayerOrder.
	 */
	public enum MapLayerOrder {

		/** The top. */
		TOP,
		/** The middle. */
		MIDDLE,
		/** The bottom. */
		BOTTOM
	}

	/**
	 * The Class MapLayerComparator.
	 */
	public static class MapLayerComparator implements Comparator<MapLayer> {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final int compare(final MapLayer o1, final MapLayer o2) {
			if (o1.order.ordinal() < o2.order.ordinal()) {
				return 1;
			} else if (o1.order.ordinal() > o2.order.ordinal()) {
				return -1;
			}
			return 0;
		}

	}

}
