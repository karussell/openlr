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
package openlr.otk.xml2kml;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import openlr.otk.common.CommandLineParseException;
import openlr.otk.options.Argument;
import openlr.otk.options.CommandLineData;
import openlr.otk.options.CommandLineOption;
import openlr.otk.options.InputFileOrDirectoryOption;
import openlr.otk.options.OutputDirectoryOption;

import org.apache.commons.cli.CommandLine;



/**
 * Command line options and data for the xml2kml tool
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
class Xml2KmlOptions extends CommandLineData {

	/** The input source option */
	private final InputFileOrDirectoryOption inputOption = new InputFileOrDirectoryOption(true);

	/** The output directory option. */
	private final OutputDirectoryOption outputDirOption = new OutputDirectoryOption(
			false);
	
    /**
     * 
     * @param toolIdentifier
     *            The identifier of the related OTK tool
     * @param description
     *            A description of the related OTK tool used for building the
     *            usage information to the user
     */
    public Xml2KmlOptions(final String toolIdentifier, final String description) {
        super(toolIdentifier, description);
    }
    
	/**
	 * Instantiates a new extract options.
	 *
	 * @param cmdLine the args
	 * @throws CommandLineParseException the parse exception
	 */
	@Override
    protected
    final void processInput(final CommandLine cmdLine) throws CommandLineParseException {

		outputDirOption.parse(cmdLine.getOptionValue(outputDirOption.getShortIdentifier()));
		inputOption.parse(cmdLine.getOptionValue(inputOption.getShortIdentifier()));
	}

	/**
	 * Gets the output dir.
	 *
	 * @return the output dir
	 */
	public final File getOutputDirectory() {
		return outputDirOption.getOutputDirectory();
	}

	/**
	 * Gets the input file.
	 *
	 * @return the input file
	 */
	public final File getInputSource() {
		return inputOption.getInputSource();
	}

    /**
     * {@inheritDoc}
     */
    @Override
    protected final List<CommandLineOption> getOptions() {
        List<CommandLineOption> options = new ArrayList<CommandLineOption>();

        options.add(inputOption);
        options.add(outputDirOption);

        return options;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<Argument<?>> getArguments() {
        return Collections.emptyList();
    }   
    
}
