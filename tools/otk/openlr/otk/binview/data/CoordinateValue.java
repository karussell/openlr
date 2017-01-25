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
package openlr.otk.binview.data;

import openlr.binary.OpenLRBinaryConstants;
import openlr.otk.utils.Formatter;

/**
 * The Class CoordinateValue represents a coordinate pair. The raw data will be converted 
 * into degree data and both values are accessible.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class CoordinateValue {
	
	/** The Constant ROUNDING_FACTOR. */
	private static final double ROUNDING_FACTOR = 0.5;
	
	/** The longitude [degree]. */
	private final double lonDeg;
	
	/** The latitude [degree]. */
	private final double latDeg;
	
	/** The longitude value [raw data]. */
	private final int lon;
	
	/** The latitude value [raw data]. */
	private final int lat;
	
	/**
	 * Instantiates a new coordinate value from raw coordinates.
	 * 
	 * @param lonValue the raw longitude value
	 * @param latValue the raw latitude value
	 */
	public CoordinateValue(final int lonValue, final int latValue) {
		lonDeg = get32BitRepresentation(lonValue);
		latDeg = get32BitRepresentation(latValue);
		lon = lonValue;
		lat = latValue;
	}
	
	/**
	 * Instantiates a new coordinate value with raw and previously resolved degree data.
	 * 
	 * @param lonValue the raw longitude value
	 * @param latValue the raw latitude value
	 * @param lonDegValue the longitude value in degree
	 * @param latDegValue the latitude value in degree
	 */
	public CoordinateValue(final double lonDegValue, final double latDegValue, final int lonValue, final int latValue) {
		lonDeg = lonDegValue;
		latDeg = latDegValue;
		lon = lonValue;
		lat = latValue;
	}
	
	
	/**
	 * Gets the longitude value in degree.
	 * 
	 * @return the longitude value in degree
	 */
	public final String getLongitudeDeg() {
		return Formatter.formatCoord(lonDeg);
	}
	
	/**
	 * Gets the latitude value in degree.
	 * 
	 * @return the latitude value in degree
	 */
	final double getLatitudeDegValue() {
		return latDeg;
	}
	
	/**
	 * Gets the longitude value in degree.
	 * 
	 * @return the longitude value in degree
	 */
	final double getLongitudeDegValue() {
		return lonDeg;
	}
	
	/**
	 * Gets the latitude value in degree.
	 * 
	 * @return the latitude value in degree
	 */
	public final String getLatitudeDeg() {
		return Formatter.formatCoord(latDeg);
	}
	
	/**
	 * Gets the raw longitude.
	 * 
	 * @return the raw longitude
	 */
	public final String getLongitude() {
		return Integer.toString(lon);
	}
	
	/**
	 * Gets the raw latitude.
	 * 
	 * @return the raw latitude
	 */
	public final String getLatitude() {
		return Integer.toString(lat);
	}
	
	/**
	 * Converts the raw data into a 32 bit representation.
	 * 
	 * @param val the raw value
	 * 
	 * @return the 32 bit representation of the raw data
	 */
	public static double get32BitRepresentation(final int val) {
		int sgn = (int) Math.signum(val);
		double retVal = (val - (sgn * ROUNDING_FACTOR))
				* OpenLRBinaryConstants.BIT24FACTOR_REVERSED;
		return retVal;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("lon: ").append(lonDeg);
		sb.append(" lat: ").append(latDeg);
		sb.append(" lon(raw): ").append(lon);
		sb.append(" lat(raw): ").append(lat);
		return sb.toString();
	}

}
