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

import java.io.File;


import openlr.otk.common.CommandLineParseException;



/**
 * An option that evaluates the given value as a path to a file or directory
 */
public class InputFileOrDirectoryOption extends CommandLineOption {

	/** The input directory. */
	private File inputSoure;

	/**
	 * Instantiates a new input directory option.
	 * 
	 * @param mand
	 *            the mand
	 */
	public InputFileOrDirectoryOption(final boolean mand) {
		super("i", "input-source", true,
				"Path to the single input file or the input directory", mand);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void parse(final String arg, final Object... addArgs)
			throws CommandLineParseException {
		if (arg != null) {
			File inDir = new File(arg);
			if (!inDir.exists()) {
				throw new CommandLineParseException("Specified input source does not exist: "
						+ inDir.getAbsolutePath());
			}
			inputSoure = inDir;
		} else {
			throw new CommandLineParseException("No input source found!");
		}
	}

	
	/**
	 * Gets the input file or directory.
	 *
	 * @return the input file or directory
	 */
	public final File getInputSource() {
		return inputSoure;
	}

}
