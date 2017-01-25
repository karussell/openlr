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
 * The Class OutputDirectoryOption.
 */
public class OutputDirectoryOption extends CommandLineOption {

	/** The output directory. */
	private File outputDir;

	/**
	 * Instantiates a new output directory option.
	 * 
	 * @param mand
	 *            the mand
	 */
	public OutputDirectoryOption(final boolean mand) {
		super("d", "output-directory", true,
				"Path to the the output directory, will be created if missing", mand);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void parse(final String arg, final Object... addArgs)
			throws CommandLineParseException {
		if (arg != null) {
			File outDir = new File(arg);
			if (!outDir.exists() && !outDir.mkdirs()) {
				throw new CommandLineParseException("Cannot create output directory: "
						+ outDir.getAbsolutePath());
			}
			outputDir = outDir;
		} else if (getOption().isRequired()) {
			throw new CommandLineParseException("No output directory found!");
		}
	}

	
	/**
	 * Gets the output directory.
	 *
	 * @return the output directory
	 */
	public final File getOutputDirectory() {
		return outputDir;
	}


}
