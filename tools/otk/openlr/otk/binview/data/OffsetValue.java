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
import openlr.binary.data.Offset;
import openlr.otk.utils.Formatter;

/**
 * The OffsetValue represents an offset value.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class OffsetValue {

	/** The Constant PERCENTAGE. */
	private static final int PERCENTAGE = 100;

	/** The Constant MAX_OFFSET_VALUE. */
	private static final int MAX_OFFSET_VALUE = 255;

	/** The offset value. */
	private final int offset;

	/**
	 * Instantiates a new offset value with offset o.
	 * 
	 * @param o
	 *            the offset
	 */
	public OffsetValue(final Offset o) {
		offset = o.getOffset();
	}

	/**
	 * {@inheritDoc}
	 */
	public final String toString() {
		return Integer.toString(offset);
	}

	/**
	 * Returns the lower bound of the length interval.
	 * 
	 * @return the lower bound
	 */
	public final String lowerBound() {
		if (offset > 0 && offset <= MAX_OFFSET_VALUE) {
			return Formatter.formatLength(offset
					* OpenLRBinaryConstants.LENGTH_INTERVAL);
		} else {
			return "";
		}
	}

	/**
	 * Returns the upper bound of the length interval.
	 * 
	 * @return the upper bound
	 */
	public final String upperBound() {
		if (offset > 0 && offset <= MAX_OFFSET_VALUE) {
			return Formatter.formatLength((offset + 1)
					* OpenLRBinaryConstants.LENGTH_INTERVAL);
		} else {
			return "";
		}
	}

	/**
	 * Gets the relative offset lower bound.
	 *
	 * @return the relative offset lower bound
	 */
	public final String getRelativeOffsetLowerBound() {
		return Formatter.formatPercentage((offset
				* OpenLRBinaryConstants.RELATIVE_OFFSET_LENGTH) / PERCENTAGE);
	}

	/**
	 * Gets the relative offset upper bound.
	 *
	 * @return the relative offset upper bound
	 */
	public final String getRelativeOffsetUpperBound() {
		return Formatter.formatPercentage(((offset + 1)
				* OpenLRBinaryConstants.RELATIVE_OFFSET_LENGTH) / PERCENTAGE);
	}

}
