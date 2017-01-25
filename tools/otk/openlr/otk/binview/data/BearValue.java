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
 * The BearValue represents a bearing value.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class BearValue {
	
	/** The Constant MAX_BEAR_VALUE. */
	private static final int MAX_BEAR_VALUE = 31;
	
	/** The bear value. */
	private final int bear;

	/**
	 * Instantiates a new bear value with value bearValue.
	 * 
	 * @param bearValue the bear value
	 */
	public BearValue(final int bearValue) {
		bear = bearValue;
	}

	/**
	 * {@inheritDoc}
	 */
	public final String toString() {
		return Integer.toString(bear);
	}


	/**
	 * Returns the lower bound of the bearing sector.
	 * 
	 * @return the lower bound of the bearing sector
	 */
	public final String lowerBound() {
		if (bear >= 0 && bear <= MAX_BEAR_VALUE) {
			return Formatter.formatAngle(bear * OpenLRBinaryConstants.BEARING_SECTOR);
		} else {
			return null;
		}
	}
	
	/**
	 * Returns the upper bound of the bearing sector.
	 * 
	 * @return the upper bound of the bearing sector
	 */
	public final String upperBound() {
		if (bear >= 0 && bear <= MAX_BEAR_VALUE) {
			return Formatter.formatAngle((bear + 1) * OpenLRBinaryConstants.BEARING_SECTOR);
		} else {
			return null;
		}
	}

}
