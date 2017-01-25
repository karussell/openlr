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
package openlr.mapviewer.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * The Class Formatter.
 */
public final class Formatter {
	
	/**
	 * Instantiates a new formatter.
	 */
	private Formatter() {
		throw new UnsupportedOperationException();
	}
	
	/** The coordinate formatter. */
	public static final DecimalFormat COORD_FORMATTER;
	
	/** The length formatter. */
	public static final DecimalFormat DIST_FORMATTER;
	
	/** The length formatter. */
	public static final DecimalFormat BEARING_FORMATTER;

	/** initialize formatter */
	static {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		COORD_FORMATTER = new DecimalFormat("0.00000", dfs);
		DIST_FORMATTER = new DecimalFormat("0.00m", dfs);
		BEARING_FORMATTER = new DecimalFormat("0.0°", dfs);
	}

}
