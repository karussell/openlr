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
package openlr.otk.options;

import openlr.otk.common.CommandLineParseException;
import openlr.otk.common.Format;





/**
 * The Class OutputFormatOption.
 */
public class OutputFormatOption extends CommandLineOption {	
	
	/** The output format. */
	private Format outputFormat;
	
	/**
	 * Instantiates a new output format option.
	 *
	 * @param mand the mand
	 */
	public OutputFormatOption(final boolean mand) {
		super("of", "output-format", true, "identifies the format of the output data\n" + Format.availableFormatString(), mand);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public final void parse(final String arg, final Object... addArgs) throws CommandLineParseException {
		outputFormat = Format.parseString(arg);
	}

	
	/**
	 * Gets the output format.
	 *
	 * @return the output format
	 */
	public final Format getOutputFormat() {
		return outputFormat;
	}
	

}
