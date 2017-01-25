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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import openlr.LocationReference;
import openlr.PhysicalFormatException;
import openlr.binary.ByteArray;
import openlr.binary.impl.LocationReferenceBinaryImpl;
import openlr.datex2.Datex2Location;
import openlr.datex2.impl.LocationReferenceImpl;
import openlr.otk.common.CommandLineParseException;
import openlr.otk.common.Format;
import openlr.otk.options.Argument;
import openlr.otk.options.CommandLineOption;
import openlr.otk.options.FileOption;
import openlr.otk.options.InputFormatOption;
import openlr.otk.options.OutputFileOrStdOutOption;
import openlr.otk.options.StringOption;
import openlr.otk.utils.DataResolver;
import openlr.otk.utils.DataResolverException;
import openlr.otk.utils.StdInHandler.READ_MODE;
import openlr.xml.generated.OpenLR;
import openlr.xml.impl.LocationReferenceXmlImpl;

import org.apache.commons.cli.CommandLine;



/**
 * This class implements processing of the input options of the decode tool.
 * After processing it provides access to the input data.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
class DecodeOptions extends BaseOptions {

    /** The Constant DECODER_PROPERTIES_FILE. */
    private static final String DECODER_PROPERTIES_FILE = "OpenLR-Decoder-Properties.xml";

    /** The loc ref. */
    private LocationReference locRef;

    /** The input format option. */
    private final InputFormatOption inputFormatOption = new InputFormatOption(
            true);
    
    /** The output file option. */
    protected final OutputFileOrStdOutOption outputOption = new OutputFileOrStdOutOption(
            false);
    
    /** The target KML file options. */
    protected final FileOption kmlOutOption = new FileOption(
            "k",
            "kml-file",
            "KML output file. Writes KML data that visualizes the location information. The file is created if it not exists or overridden otherwise.",
            false, false);

    /** The location id option. */
    private final StringOption idOption = new StringOption(
            "id",
            "location-id",
            "specifies an identifier that is set to the created output location if the location reference not provides an identifier itself as it is the case for binary encoded location references",
            false);

    /**
     * Instantiates a new decode options.
     * 
     * @param toolIdentifier
     *            The identifier of the related OTK tool
     * @param description
     *            A description of the related OTK tool used for building the
     *            usage information to the user
     */
    public DecodeOptions(final String toolIdentifier, final String description) {

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

        inputFormatOption.parse(cmdLine.getOptionValue(inputFormatOption
                .getShortIdentifier()));
        outputOption.parse(cmdLine.getOptionValue(outputOption
                .getShortIdentifier()));
        kmlOutOption.parse(cmdLine.getOptionValue(kmlOutOption
                .getShortIdentifier()));
        mapAccessOption.parse(cmdLine.getOptionValue(mapAccessOption
                .getShortIdentifier()));
        propertiesOption.parse(cmdLine.getOptionValue(propertiesOption
                .getShortIdentifier()));
        idOption.parse(cmdLine.getOptionValue(idOption.getShortIdentifier()));

        READ_MODE mode = READ_MODE.CHAR;
        if (inputFormatOption.getInputFormat() == Format.BINARY) {
            mode = READ_MODE.BYTE;
        }
        inputOption.parse(
                cmdLine.getOptionValue(inputOption.getShortIdentifier()), mode);

        setProperties(propertiesOption, DECODER_PROPERTIES_FILE);

        String locationID = idOption.getValue();
        if (locationID == null) {
            locationID = "";
        }
        handleDecoding(locationID, inputOption.getInputStream(),
                inputFormatOption.getInputFormat(),
                inputOption.getInputSource());
    }

    /**
     * Decodes the location data provided by <code>is</code> according to the
     * specified input format.
     * 
     * @param locationID
     *            The identifier to set to the created location reference
     * @param is
     *            An input stream delivering the location data.
     * @param inputFormat
     *            The {@link Format} of the input data.
     * @param sourceIdentifier
     *            An identifier describing the input source
     * @return An error message if an error occurred or null if everything
     *         worked fine.
     */
    private String handleDecoding(final String locationID, final InputStream is,
            final Format inputFormat, final String sourceIdentifier) {

        String error = null;
        String resourceErrorInfo = "";
        if (sourceIdentifier != null) {
            resourceErrorInfo = ": \"" + sourceIdentifier + "\"";
        }

        try {
            if (inputFormat == Format.BINARY) {
                ByteArray ba = DataResolver.resolveBinaryData(is, false);
                locRef = new LocationReferenceBinaryImpl(locationID,
                        ba);
            } else if (inputFormat == Format.BINARY64) {
                ByteArray ba = DataResolver.resolveBinaryData(is, true);
                locRef = new LocationReferenceBinaryImpl(locationID,
                        ba);
            } else if (inputFormat == Format.DATEX2) {
                Datex2Location datex2 = DataResolver.resolveDatex2Data(is);
                locRef = new LocationReferenceImpl(locationID,
                        datex2, 1);
            } else if (inputFormat == Format.XML) {
                OpenLR xml = DataResolver.resolveXmlData(is);
                locRef = new LocationReferenceXmlImpl(locationID, xml,
                        1);
                if (!locRef.isValid()) {
                    error = "Cannot determine location type in xml data!";
                }
            }
        } catch (PhysicalFormatException e) {
            error = "Cannot read data input (" + e.getErrorCode() + ")"
                    + resourceErrorInfo + " message: " + e.getMessage();
        } catch (DataResolverException e) {
            error = "Cannot read data input (" + e.getErrorCode() + ")"
                    + resourceErrorInfo + " message: " + e.getMessage();
        }

        return error;
    }

    /**
     * Gets the input format.
     * 
     * @return the input format
     */
    public final Format getInputFormat() {
        return inputFormatOption.getInputFormat();
    }

    /**
     * Gets the location reference.
     * 
     * @return the location reference
     */
    public final LocationReference getLocationReference() {
        return locRef;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected final List<CommandLineOption> getOptions() {
        List<CommandLineOption> options = new ArrayList<CommandLineOption>();
        options.add(inputOption);
        options.add(inputFormatOption);
        options.add(outputOption);
        options.add(kmlOutOption);
        options.add(mapAccessOption);
        options.add(propertiesOption);
        options.add(idOption);
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
     * Gets the output file stream.
     * 
     * @return the output stream
     * @throws FileNotFoundException
     *             If there is a problem writing to the file
     */
    public final OutputStream getOutputStream() throws FileNotFoundException {
        return outputOption.getOutputStream();
    }

    /**
     * Delivers an identifier/ name of the output target.
     * 
     * @return The target identifier
     */
    public final String getOutputTargetIdentifier() {

        return outputOption.getOutputTarget();
    }
    
    /**
     * Gets the KML file if specified.
     * 
     * @return the KML output file
     */
    public final File getKmlOutputFile() {
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
}
