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

import openlr.binary.data.LastClosedLineLRP;

/**
 * The Class LastClosedLineValue represents the last closed line location reference point.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class LastClosedLineValue {
	
	/** The functional road class. */
	private final FRCValue frc;
	
	/** The form of way. */
	private final FOWValue fow;
	
	/** The bearing. */
	private final BearValue bear;
	
	/** The id. */
	private final int id;
	
	/**
	 * Instantiates a new last value. The coordinate depends on the values of the
	 * previous location reference point.
	 * 
	 * @param idValue the id value
	 * @param lrp the last closed line location reference point
	 */
	public LastClosedLineValue(final int idValue, final LastClosedLineLRP lrp) {
		frc = new FRCValue(lrp.getAttrib5().getFrc());
		fow = new FOWValue(lrp.getAttrib5().getFow());
		bear = new BearValue(lrp.getAttrib6().getBear());
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
	 * {@inheritDoc}
	 */
	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id: ").append(id);
		sb.append(" frc: ").append(frc);
		sb.append(" fow: ").append(fow);
		sb.append(" bear: ").append(bear);
		return sb.toString();
	}

}
