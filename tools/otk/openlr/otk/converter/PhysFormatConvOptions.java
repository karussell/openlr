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
package openlr.otk.converter;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import openlr.otk.common.CommandLineParseException;
import openlr.otk.common.Format;
import openlr.otk.options.Argument;
import openlr.otk.options.CommandLineData;
import openlr.otk.options.CommandLineOption;
import openlr.otk.options.InputFileOrDataOption;
import openlr.otk.options.InputFormatOption;
import openlr.otk.options.OutputFileOrStdOutOption;
import openlr.otk.options.OutputFormatOption;
import openlr.otk.utils.StdInHandler.READ_MODE;

import org.apache.commons.cli.CommandLine;



/**
 * The Class PhysFormatConvOptions.
 */
class PhysFormatConvOptions extends CommandLineData {

    /**
     * 
     * @param toolIdentifier
     *            The identifier of the related OTK tool
     * @param description
     *            A description of the related OTK tool used for building the
     *            usage information to the user
     */
    public PhysFormatConvOptions(final String toolIdentifier, final String description) {
        super(toolIdentifier, description);
    }
	
	/** The Constant I_OPTION. */
	private static final InputFileOrDataOption I_OPTION = new InputFileOrDataOption();
	
	/** The Constant IF_OPTION. */
	private static final InputFormatOption IF_OPTION = new InputFormatOption(true);
	
	/** The Constant O_OPTION. */
	private static final OutputFileOrStdOutOption O_OPTION = new OutputFileOrStdOutOption(false);
	
	/** The Constant OF_OPTION. */
	private static final OutputFormatOption OF_OPTION = new OutputFormatOption(true);



	/**
	 * Instantiates a new phys format conv options.
	 * 
	 * @param cmdLine
	 *            the args
	 * @throws CommandLineParseException
	 *             the parse exception
	 */
	@Override
    protected
    final void processInput(final CommandLine cmdLine)
            throws CommandLineParseException {

		IF_OPTION.parse(cmdLine.getOptionValue(IF_OPTION.getShortIdentifier()));
		OF_OPTION.parse(cmdLine.getOptionValue(OF_OPTION.getShortIdentifier()));
		READ_MODE mode = READ_MODE.CHAR;
		if (IF_OPTION.getInputFormat() == Format.BINARY) {
			mode = READ_MODE.BYTE;
		}
		I_OPTION.parse(cmdLine.getOptionValue(I_OPTION.getShortIdentifier()), mode);
		O_OPTION.parse(cmdLine.getOptionValue(O_OPTION.getShortIdentifier()));
	}
	
	/**
	 * Gets the input stream.
	 *
	 * @return the input stream
	 */
	public final InputStream getInputStream() {
		return I_OPTION.getInputStream();
	}

	/**
	 * Gets the output stream.
	 *
	 * @return the output stream
     * @throws FileNotFoundException
     *             If there is a problem writing to the file
	 */
	public final OutputStream getOutputStream() throws FileNotFoundException {
		return O_OPTION.getOutputStream();
	}

	/**
	 * Gets the input format.
	 *
	 * @return the input format
	 */
	public final Format getInputFormat() {
		return IF_OPTION.getInputFormat();
	}

	/**
	 * Gets the output format.
	 *
	 * @return the output format
	 */
	public final Format getOutputFormat() {
		return OF_OPTION.getOutputFormat();
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    protected final List<CommandLineOption> getOptions() {
        List<CommandLineOption> options = new ArrayList<CommandLineOption>();
        options.add(I_OPTION);
        options.add(IF_OPTION);
        options.add(O_OPTION);
        options.add(OF_OPTION);
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
