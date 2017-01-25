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
package openlr.otk.coding;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import openlr.location.Location;
import openlr.location.utils.LocationData;
import openlr.location.utils.LocationDataReader;
import openlr.map.MapDatabase;
import openlr.otk.common.CommandLineParseException;
import openlr.otk.common.Format;
import openlr.otk.options.Argument;
import openlr.otk.options.CommandLineOption;
import openlr.otk.options.FileOption;
import openlr.otk.options.OutputDirectoryOption;
import openlr.otk.options.OutputFormatOption;
import openlr.otk.utils.IOUtils;
import openlr.otk.utils.StdInHandler.READ_MODE;

import org.apache.commons.cli.CommandLine;

/**
 * This class implements processing of the input options of the encode tool.
 * After processing it provides access to the input data.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
class EncodeOptions extends BaseOptions {

    /** The Constant ENCODER_PROPERTIES_FILE. */
    private static final String ENCODER_PROPERTIES_FILE = "OpenLR-Encoder-Properties.xml";

    /** The Constant outputFormatOption. */
    protected final OutputFormatOption outputFormatOption = new OutputFormatOption(
            true);
    
    /** The output file option. */
    protected final OutputDirectoryOption outputOption = new OutputDirectoryOption(
            false);
    
    /** The target KML file options. */
    protected final FileOption kmlOutOption = new FileOption(
            "k",
            "kml-dir",
            "KML output directory. Writes KML data that visualizes the location information. The directory is created if it not exists.",
            false, false);

    /** The location. */
    private List<Location> locations;


    /**
     * Instantiates a new encode options.
     * @param toolIdentifier
     *            The identifier of the related OTK tool
     * @param description
     *            A description of the related OTK tool used for building the
     *            usage information to the user
     */
    public EncodeOptions(final String toolIdentifier,
            final String description) {
        
        super(toolIdentifier, description);
    }

    /**
     * Initializes this options data holder with the given command line
     * arguments.
     * 
     * @param cmdLine
     *            The command line arguments
     * @throws CommandLineParseException
     *             If an error occurred processing the provided arguments
     */
    @Override
    protected
    final void processInput(final CommandLine cmdLine)
            throws CommandLineParseException {

        outputFormatOption.parse(cmdLine.getOptionValue(outputFormatOption
                .getShortIdentifier()));
        outputOption.parse(cmdLine.getOptionValue(outputOption
                .getShortIdentifier()));
        kmlOutOption.parse(cmdLine.getOptionValue(kmlOutOption
                .getShortIdentifier()));
        mapAccessOption.parse(cmdLine.getOptionValue(mapAccessOption
                .getShortIdentifier()));
        propertiesOption.parse(cmdLine.getOptionValue(propertiesOption
                .getShortIdentifier()));

        inputOption.parse(
                cmdLine.getOptionValue(inputOption.getShortIdentifier()),
                READ_MODE.CHAR);

        setProperties(propertiesOption, ENCODER_PROPERTIES_FILE);

        locations = parseLocation(inputOption.getInputStream(),
                inputOption.getInputSource(), mapAccessOption.getMapDB());

    }

    /**
     * Performs that tasks necessary when running the application in encoding
     * mode.
     * 
     * @param inputData
     *            The stream of input data.
     * @param sourceIdentifier
     *            An identifier describing the input source
     * @param map
     *            The relevant map database to encode on
     * @throws CommandLineParseException
     *             If an error occurred
     * @return The read location
     */
    private List<Location> parseLocation(final InputStream inputData,
            final String sourceIdentifier, final MapDatabase map)
            throws CommandLineParseException {

        LocationData locData = new LocationData();
        BufferedReader reader = null;
        try {

            reader = new BufferedReader(new InputStreamReader(inputData,
                    IOUtils.SYSTEM_DEFAULT_CHARSET));
            locData = LocationDataReader.loadLocationData(reader, map);
            if (locData.hasErrors()) {
                StringBuilder error = new StringBuilder(256);
                
                error.append("Not all locations could be red from the input. Messages: ");
            	for (String s : locData.getErrors()) {
                    error.append("\"").append(s).append("\" ");
            	}
                throw new CommandLineParseException(error.toString());
            }
        } catch (IOException e) {

            throw new CommandLineParseException(
                    "Error reading the location input source, message: "
                            + e.getMessage(), e);
        } finally {
        	IOUtils.closeQuietly(reader);
        }
        if (locData.hasLocations()) {
            return locData.getLocations();
        } else {
            throw new CommandLineParseException(
                    "No location found in the input source " + sourceIdentifier);
        }
    }
    
    /**
     * Gets the output format.
     * 
     * @return the output format
     */
    public final Format getOutputFormat() {
        return outputFormatOption.getOutputFormat();
    }

    /**
     * Gets the location.
     * 
     * @return the location
     */
    public final List<Location> getLocations() {
        return locations;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected final List<CommandLineOption> getOptions() {
        List<CommandLineOption> options = new ArrayList<CommandLineOption>();

        options.add(inputOption);
        options.add(outputOption);
        options.add(outputFormatOption);
        options.add(kmlOutOption);
        options.add(mapAccessOption);
        options.add(propertiesOption);
        return options;
    }     
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected List<Argument<?>> getArguments() {
        return Collections.emptyList();
    }   
    
    /**
     * Gets the KML file if specified.
     * 
     * @return the KML output file
     */
    public final File getKmlOutputDir() {
        return kmlOutOption.getFile();
    }

    /**
     * Checks if is KML required.
     * 
     * @return true, if is KML required
     */
    public final boolean isKmlRequired() {
        return kmlOutOption.getFile() != null;
    }
    
    /**
     * Gets the output directory.
     *
     * @return the output directory
     */
    public final File getOutputDirectory() {
    	return outputOption.getOutputDirectory();
    }

	/**
	 * Checks for output directory.
	 *
	 * @return true, if successful
	 */
	public boolean hasOutputDirectory() {
		return outputOption.getOutputDirectory() != null;
	}
}
