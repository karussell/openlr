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
 * The DNPValue represents a distance to next point value.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class DNPValue {
	
	
	/** The Constant MAX_DNP_VALUE. */
	private static final int MAX_DNP_VALUE = 255;
	
	/** The dnp value. */
	private final int dnp;

	/**
	 * Instantiates a new dnp value..
	 * 
	 * @param dnpValue the dnp value
	 */
	public DNPValue(final int dnpValue) {
		dnp = dnpValue;
	}

	/**
	 * {@inheritDoc}
	 */
	public final String toString() {
		return Integer.toString(dnp);
	}


	/**
	 * Returns the lower bound of the length interval.
	 * 
	 * @return the lower bound
	 */
	public final String lowerBound() {
		if (dnp >= 0 && dnp <= MAX_DNP_VALUE) {
			return Formatter.formatLength(dnp * OpenLRBinaryConstants.LENGTH_INTERVAL);
		} else {
			return null;
		}
	}
	
	/**
	 * Returns the upper bound of the length interval.
	 * 
	 * @return the upper bound
	 */
	public final String upperBound() {
		if (dnp >= 0 && dnp <= MAX_DNP_VALUE) {
			return Formatter.formatLength((dnp + 1) * OpenLRBinaryConstants.LENGTH_INTERVAL);
		} else {
			return null;
		}
	}

}
