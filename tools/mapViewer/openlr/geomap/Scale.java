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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import openlr.map.utils.GeometryUtils;

/**
 * The Class Scale.
 */
public final class Scale {
	
	/** The Constant SCALE_DRAW_OFFSET. */
	private static final int SCALE_DRAW_OFFSET = 10;
	
	/** The Constant FRACTIONAL_WIDTH. */
	private static final double FRACTIONAL_WIDTH = 0.25; 
	
	/** The Constant BASIS. */
	private static final int BASIS = 10;
	
	/** The Constant MAX_MULTIPLIER. */
	private static final int MAX_MULTIPLIER = 5;
	
	/** The Constant MIDDLE_MULTIPLIER. */
	private static final int MIDDLE_MULTIPLIER = 2;
	
	/** The Constant VERTICAL_BAR_MIDDLE. */
	private static final int VERTICAL_BAR_MIDDLE = 6;
	
	/** The Constant VERTICAL_BAR_OUTSIDE. */
	private static final int VERTICAL_BAR_OUTSIDE = 10;
	
	/** The Constant METER_PER_KM. */
	private static final int METER_PER_KM = 1000;
	
	/** The Constant STRING_OFFSET_X. */
	private static final int STRING_OFFSET_X = 6;
	
	/** The Constant STRING_OFFSET_Y. */
	private static final int STRING_OFFSET_Y = -2;
	
	/**
	 * Utility class shall not be instantiated.
	 */
	private Scale() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Draw current scale.
	 *
	 * @param g the graphics
	 * @param mapPane the map pane
	 * @param scaleColor The color to draw the scale in
	 */
	public static void drawScale(final Graphics2D g, final JMapPane mapPane, final Color scaleColor) {
		int scaleX = SCALE_DRAW_OFFSET;
		int scaleY = SCALE_DRAW_OFFSET;
		double widthInPixels = FRACTIONAL_WIDTH * mapPane.getWidth();
	
		double mapCentreY = mapPane.getCenterY();
		double widthInMetres = FRACTIONAL_WIDTH
				* GeometryUtils.distance(mapPane.getMinX(), mapCentreY,
						mapPane.getMaxX(), mapCentreY);

		// get the nearest power of 10
		double e = Math.log(widthInMetres) / Math.log(BASIS);
		e = Math.pow(BASIS, Math.floor(e));
		 //only multipliers of 1, 2 and 5 are permitted
		int multiplier = (int) (widthInMetres / e);
		if (multiplier > MAX_MULTIPLIER) {
			multiplier = MAX_MULTIPLIER;
		} else if (multiplier > MIDDLE_MULTIPLIER) {
			multiplier = MIDDLE_MULTIPLIER;
		}
		// the new width in pixels is snapped to the nearest
		// convenient meters unit (being d * 10^n, where d = 1, 2 or 5)
		int lineWidth = (int) (multiplier * e / widthInMetres * widthInPixels);
		g.setStroke(new BasicStroke(1));
		g.setColor(scaleColor);
		// draw the base line
		g.drawLine(scaleX, scaleY, scaleX + lineWidth, scaleY);
		// draw the vertical bars.
		for (int i = 0; i <= multiplier; i++) {
			int cx = scaleX + (int) ((double) lineWidth * i / multiplier);
			// end bars are higher
			int ht = VERTICAL_BAR_MIDDLE;
			if (i == 0 || i == multiplier) {
				ht = VERTICAL_BAR_OUTSIDE;
			}
			g.drawLine(cx, scaleY + ht, cx, scaleY);
		}
		// label it
		String s;
		if (e < METER_PER_KM) {
			s = Integer.toString((int) (multiplier * e)) + "m";
		} else {
			s = Integer.toString((int) (multiplier * e / METER_PER_KM)) + "km";
		}
        
		AffineTransform orig = g.getTransform();
		// the drawString method relies on the 
		g.setTransform(new AffineTransform());
		g.drawString(s, scaleX + lineWidth + STRING_OFFSET_X,  mapPane.getHeight() - scaleY + STRING_OFFSET_Y);
		g.setTransform(orig);
	}

}
