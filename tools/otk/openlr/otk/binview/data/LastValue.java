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

import openlr.binary.data.LastLRP;

/**
 * The Class LastValue represents the last location reference point.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class LastValue {
	
	/** The Constant DECA_MICRO_DEG_FACTOR. */
	private static final double DECA_MICRO_DEG_FACTOR = 100000.0;
	
	/** The coordinates. */
	private final CoordinateValue coords;	
	
	/** The functional road class. */
	private final FRCValue frc;
	
	/** The form of way. */
	private final FOWValue fow;
	
	/** The bearing. */
	private final BearValue bear;

	/** The positive offset flag. */
	private final int posOff;
	
	/** The negative offset flag. */
	private final int negOff;
	
	/** The id. */
	private final int id;
	
	/**
	 * Instantiates a new last value. The coordinate depends on the values of the
	 * previous location reference point.
	 * 
	 * @param idValue the id value
	 * @param lrp the last location reference point
	 * @param prevLon the previous longitude value
	 * @param prevLat the previous latitude value
	 */
	public LastValue(final int idValue, final LastLRP lrp, final double prevLon, final double prevLat) {
		double lon = prevLon + (lrp.getLon() / DECA_MICRO_DEG_FACTOR);
		double lat = prevLat + (lrp.getLat() / DECA_MICRO_DEG_FACTOR);
		coords = new CoordinateValue(lon, lat, lrp.getLon(), lrp.getLat());
		frc = new FRCValue(lrp.getAttrib1().getFrc());
		fow = new FOWValue(lrp.getAttrib1().getFow());
		bear = new BearValue(lrp.getAttrib4().getBear());
		posOff = lrp.getAttrib4().getPOffsetf();
		negOff = lrp.getAttrib4().getNOffsetf();
		id = idValue;
	}
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public final String getId() {
		return Integer.toString(id);
	}
	
	/**
	 * Gets the raw longitude.
	 * 
	 * @return the longitude
	 */
	public final String getLongitude() {
		return coords.getLongitude();
	}
	
	/**
	 * Gets the raw latitude.
	 * 
	 * @return the latitude
	 */
	public final String getLatitude() {
		return coords.getLatitude();
	}
	
	/**
	 * Gets the longitude in degree.
	 * 
	 * @return the longitude in degree
	 */
	public final String getLongitudeDeg() {
		return coords.getLongitudeDeg();
	}
	
	/**
	 * Gets the latitude in degree.
	 * 
	 * @return the latitude in degree
	 */
	public final String getLatitudeDeg() {
		return coords.getLatitudeDeg();
	}
	
	/**
	 * Gets the longitude in degree.
	 * 
	 * @return the longitude in degree
	 */
	final double getLongitudeDegValue() {
		return coords.getLongitudeDegValue();
	}
	
	/**
	 * Gets the latitude in degree.
	 * 
	 * @return the latitude in degree
	 */
	final double getLatitudeDegValue() {
		return coords.getLatitudeDegValue();
	}
	
	/**
	 * Gets the functional road class.
	 * 
	 * @return the frc
	 */
	public final FRCValue getFrc() {
		return frc;
	}
	
	/**
	 * Gets the form of way.
	 * 
	 * @return the fow
	 */
	public final FOWValue getFow() {
		return fow;
	}
	
	/**
	 * Gets the bearing.
	 * 
	 * @return the bear
	 */
	public final BearValue getBear() {
		return bear;
	}
	

	/**
	 * Gets the positive offset flag.
	 * 
	 * @return the posOffF
	 */
	public final Integer getPosOff() {
		return Integer.valueOf(posOff);
	}

	/**
	 * Gets the negative offset flag.
	 * 
	 * @return the negOffF
	 */
	public final Integer getNegOff() {
		return Integer.valueOf(negOff);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id: ").append(id);
		sb.append(" coordinates: ").append(coords);	
		sb.append(" frc: ").append(frc);
		sb.append(" fow: ").append(fow);
		sb.append(" bear: ").append(bear);
		return sb.toString();
	}

}
