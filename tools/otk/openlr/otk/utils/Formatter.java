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
**/

/**
 *  Copyright (C) 2009-12 TomTom International B.V.
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
package openlr.otk.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import openlr.map.GeoCoordinates;

/**
 * The Class Formatter provides method for formatting length, coordinate and 
 * degree values.
 * 
 *  * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public final class Formatter {
	
	/** The Constant ANGLE_FORMAT. */
	private static final DecimalFormat ANGLE_FORMAT;
	
	/** The Constant LENGTH_FORMAT. */
	private static final DecimalFormat LENGTH_FORMAT;
	
	/** The Constant COORD_FORMAT. */
	private static final DecimalFormat COORD_FORMAT;
	
	/** The Constant PERCENTAGE_FORMAT. */
	private static final DecimalFormat PERCENTAGE_FORMAT;


	//prepare formatters
	static {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		ANGLE_FORMAT = new DecimalFormat("#0.00", dfs);
		LENGTH_FORMAT = new DecimalFormat("#0.00", dfs);
		PERCENTAGE_FORMAT = new DecimalFormat("#0.00%", dfs);
		COORD_FORMAT = new DecimalFormat("#0.000000", dfs);
	}
	
	/**
	 * Helper class cannot be instantiated.
	 */
	private Formatter() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Formats angle values.
	 * 
	 * @param val the angel value
	 * 
	 * @return the formatted string
	 */
	public static String formatAngle(final double val) {
		return ANGLE_FORMAT.format(val);
	}
	
	/**
	 * Formats length values.
	 * 
	 * @param val the length value
	 * 
	 * @return the formatted string
	 */
	public static String formatLength(final double val) {
		return LENGTH_FORMAT.format(val);
	}
	
	/**
	 * Formats coordinate values.
	 * 
	 * @param val the coordinate value
	 * 
	 * @return the formatted string
	 */
	public static String formatCoord(final double val) {
		return COORD_FORMAT.format(val);
	}
	
    /**
     * Formats a complete geo-coordinate in a way to use it for logging or
     * display
     * 
     * @param coordinate
     *            The coordinate
     * @return The formatted coordinate pair
     */
    public static String formatCoord(final GeoCoordinates coordinate) {
        return COORD_FORMAT.format(coordinate.getLongitudeDeg()) + ", "
                + COORD_FORMAT.format(coordinate.getLatitudeDeg());
    }

	/**
	 * Format percentage values.
	 *
	 * @param val the value
	 * @return the formatted string
	 */
	public static String formatPercentage(final double val) {
		return PERCENTAGE_FORMAT.format(val);
	}

}
