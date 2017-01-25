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

import openlr.map.FunctionalRoadClass;

/**
 * The FRCValue represents a functional road class value.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class FRCValue {

	/** The internal frc id. */
	private final int frc;

	/**
	 * Instantiates a new functional road class value with id frcValue.
	 * 
	 * @param frcValue the frc id
	 */
	public FRCValue(final int frcValue) {
		frc = frcValue;
	}

	/**
	 * {@inheritDoc}
	 */
	public final String toString() {
		return Integer.toString(frc);
	}

	/**
	 * Return the name of this functional road class.
	 * 
	 * @return the frc name
	 */
	public final String name() {
		if (frc >= 0 && frc < FunctionalRoadClass.getFRCs().size()) {
			return FunctionalRoadClass.getFRCs().get(frc).name();
		} else {
			return null;
		}
	}

}
