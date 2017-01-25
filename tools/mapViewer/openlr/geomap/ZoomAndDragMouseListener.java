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

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import openlr.geomap.MapMouseEvent.Button;
import openlr.geomap.transform.JMapTransformMercator;

/**
 * This listener implements zooming and dragging the map graphics.
 */
class ZoomAndDragMouseListener extends MapMouseAdaptor {

	/** The Constant ZOOM_IN_FACTOR. */
	private static final double ZOOM_IN_FACTOR = 4d;

	/** The Constant ZOOM_OUT_FACTOR. */
	private static final double ZOOM_OUT_FACTOR = 1.3;

	/** The map panel. */
	private final JMapPane mapPanel;

	/** The press point. */
	private Point pressPoint;

	/** The is drag. */
	private boolean isDrag = false;

	/** The click point. */
	private Point clickPoint;

	/**
	 * Instantiates a new map panel mouse listener.
	 * 
	 * @param mp
	 *            the mp
	 */
	public ZoomAndDragMouseListener(final JMapPane mp) {
		mapPanel = mp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
    public final void mouseClicked(final MapMouseEvent e) {
        clickPoint = new Point(e.getPanelX(), e.getPanelY());
        if (e.getClickCount() > 1) {
            Button btn = e.getButton();
            if (btn == MapMouseEvent.Button.BUTTON_1) {
                zoom(clickPoint);
            } else if (btn == MapMouseEvent.Button.BUTTON_3) {
                dezoom(clickPoint);
            }
        }
    }

	/**
	 * Zoom.
	 * 
	 * @param p
	 *            the p
	 */
	private void zoom(final Point p) {
		Rectangle mapBounds = mapPanel.getBounds();
		JMapTransformMercator transform = mapPanel.getTransform();
        Point2D.Double ul = transform.getGeoCoordinate(0, mapBounds.height);
        Point2D.Double lr = transform.getGeoCoordinate(mapBounds.width, 0);
		Point2D.Double center = transform.getGeoCoordinate(p.x, p.y);
		double width = Math.abs((ul.x - lr.x) / ZOOM_IN_FACTOR);
		double height = Math.abs((ul.y - lr.y) / ZOOM_IN_FACTOR);
		Point2D.Double mp0 = new Point2D.Double(center.x - width, center.y
				- height);
		Point2D.Double mp1 = new Point2D.Double(center.x + width, center.y
				+ height);
		mapPanel.setViewport(mp0.x, mp0.y, mp1.x, mp1.y);
		mapPanel.repaint();
	}

	/**
	 * Dezoom.
	 * 
	 * @param p
	 *            the p
	 */
	private void dezoom(final Point p) {
		Rectangle mapBounds = mapPanel.getBounds();
		
		JMapTransformMercator transform = mapPanel.getTransform();
        Point2D.Double ul = transform.getGeoCoordinate(0, mapBounds.height);
        Point2D.Double lr = transform.getGeoCoordinate(mapBounds.width, 0);
		Point2D.Double center = transform.getGeoCoordinate(p.x, p.y);
		double width = Math.abs((ul.x - lr.x) * ZOOM_OUT_FACTOR);
		double height = Math.abs((ul.y - lr.y) * ZOOM_OUT_FACTOR);
		Point2D.Double mp0 = new Point2D.Double(center.x - width, center.y
				- height);
		Point2D.Double mp1 = new Point2D.Double(center.x + width, center.y
				+ height);
		mapPanel.setViewport(mp0.x, mp0.y, mp1.x, mp1.y);
		mapPanel.repaint();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void mousePressed(final MapMouseEvent e) {
		pressPoint = new Point(e.getPanelX(), e.getPanelY());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void mouseReleased(final MapMouseEvent e) {
		if (isDrag) {
			Rectangle mapBounds = mapPanel.getBounds();
			JMapTransformMercator transform = mapPanel.getTransform();
	        Point2D.Double ul = transform.getGeoCoordinate(0, mapBounds.height);
	        Point2D.Double lr = transform.getGeoCoordinate(mapBounds.width, 0);
			Point2D.Double startMove = transform.getGeoCoordinate(pressPoint.x,
					pressPoint.y);
			Point2D.Double endMove = transform.getGeoCoordinate(e.getPanelX(),
			        e.getPanelY());
			double diffLon = -(endMove.x - startMove.x);
			double diffLat = -(endMove.y - startMove.y);
			Point2D.Double mp0 = new Point2D.Double(ul.x + diffLon, lr.y + diffLat);
			Point2D.Double mp1 = new Point2D.Double(lr.x + diffLon, ul.y + diffLat);
			mapPanel.setViewport(mp0.x, mp0.y, mp1.x, mp1.y);
			mapPanel.moveImage(0, 0);
			mapPanel.repaint();
			isDrag = false;
		}
	}
	

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseDragged(final MapMouseEvent e) {
        isDrag = true;
        mapPanel.moveImage(e.getPanelX() - pressPoint.x, e.getPanelY() - pressPoint.y);
    }

}
