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
package openlr.otk.binview;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import openlr.otk.common.CommandLineParseException;
import openlr.otk.options.Argument;
import openlr.otk.options.CommandLineOption;
import openlr.otk.options.FileOption;
import openlr.otk.options.FlagOption;
import openlr.otk.options.InputFileOrDataOption;
import openlr.otk.options.OutputFileOrStdOutOption;
import openlr.otk.utils.IOUtils;
import openlr.otk.utils.StdInHandler.READ_MODE;

import org.apache.commons.cli.CommandLine;
import org.apache.log4j.Logger;

/**
 * This class processes the command line arguments and initializes the input
 * data.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
class BinaryViewerOptions extends openlr.otk.options.CommandLineData {

    /** The logger. */
    private static final Logger LOG = Logger
            .getLogger(BinaryViewerOptions.class);

    /**
     * The application option specifying base 64 input.
     */
    static final String B64_OPTION = "b64";


    /**
     * The input data.
     */
    private byte[] binData;

    /**
     * An ID for the generated location information derived from the input data.
     */
    private String locationID;

    /**
     * The input option, allows input via file, stdin or direct data
     */
    private final InputFileOrDataOption inputOption = new InputFileOrDataOption();

    /**
     * The output option 
     */
    private final OutputFileOrStdOutOption outputOption = new OutputFileOrStdOutOption(
            false);

    /**
     * The base 64 flag
     */
    private final FlagOption base64Option = new FlagOption(
            B64_OPTION,
            "base64",
            "data file contains Base64 encoded data [mandatory, if input data is Base64-encoded]",
            false);

    /** The target KML file options. */
    protected final FileOption kmlOutOption = new FileOption(
            "k",
            "kml-file",
            "KML output file. Writes KML data that visualizes the location reference information. The file is created if it not exists or overridden otherwise.",
            false, false);
    
    /**
     * Instantiates new binary viewer options.
     * 
     * @param toolIdentifier
     *            The identifier of the related OTK tool
     * @param description
     *            A description of the related OTK tool used for building the
     *            usage information to the user
     */
    public BinaryViewerOptions(final String toolIdentifier,
            final String description) {

        super(toolIdentifier, description);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected List<CommandLineOption> getOptions() {
        List<CommandLineOption> options = new ArrayList<CommandLineOption>();
        options.add(inputOption);
        options.add(outputOption);
        options.add(base64Option);
        options.add(kmlOutOption);
        return options;
    }

    /**
     * Creates a new instance.
     * 
     * @param cl
     *            The command line arguments.
     * @throws CommandLineParseException
     *             If an error occurs processing the command line arguments.
     */
    @Override
    protected void processInput(final CommandLine cl) throws CommandLineParseException {

        READ_MODE mode = READ_MODE.BYTE;
        if (cl.hasOption(B64_OPTION)) {
            mode = READ_MODE.CHAR;
        }
        inputOption.parse(cl.getOptionValue(inputOption.getShortIdentifier()),
                mode);

        outputOption
                .parse(cl.getOptionValue(outputOption.getShortIdentifier()));

        locationID = inputOption.getInputSource();
        binData = readContent(cl.hasOption(B64_OPTION),
                inputOption.getInputStream());
        
        kmlOutOption
                .parse(cl.getOptionValue(kmlOutOption.getShortIdentifier()));
    }

    /**
     * Reads the content of the input file.
     * 
     * @param isBase64
     *            Specifies if the content is of base-65-encoded data, or is yet
     *            the pure binary data.
     * @param inputData
     *            The input stream.
     * @return The binary location data.
     * @throws CommandLineParseException
     *             If an error occurs.
     */
    private byte[] readContent(final boolean isBase64,
            final InputStream inputData) throws CommandLineParseException {
        byte[] data;
        if (isBase64) {
            // read base64 encoded string and decode to binary data
            BufferedReader br = null;
            StringBuffer base64String = new StringBuffer();
            try {
                br = new BufferedReader(new InputStreamReader(inputData,
                        IOUtils.SYSTEM_DEFAULT_CHARSET));
                String line;
                while ((line = br.readLine()) != null) {
                    base64String.append(line);
                }
            } catch (FileNotFoundException e) {
                throw new CommandLineParseException("Error reading input from "
                        + inputOption.getInputSource(), e);
            } catch (IOException e) {
                throw new CommandLineParseException("Error reading input from "
                        + inputOption.getInputSource(), e);
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        LOG.error("cannot close input reader");
                    }
                }
            }
            data = BinaryDataViewer.transformBase64(base64String.toString()
                    .trim());
        } else {
            try {
                int b;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                while ((b = inputData.read()) != -1) {
                    bos.write(b);
                }
                bos.flush();
                data = bos.toByteArray();

            } catch (FileNotFoundException fnfe) {
                throw new CommandLineParseException(
                        "Error reading binary input from "
                                + inputOption.getInputSource(), fnfe);
            } catch (IOException e) {
                throw new CommandLineParseException("the binary stream is not valid! "
                        + e.getMessage(), e);
            } 
        }
        return data;
    }

    /**
     * Delivers the input data.
     * 
     * @return The input data.
     */
    byte[] getBinData() {
        if (binData != null) {
            return Arrays.copyOf(binData, binData.length);
        } else {
            return null;
        }
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

    /**
     * Delivers a setup output stream for the output. The output stream and thus
     * the target file is first created when this method is called!
     * 
     * @return A setup output stream for the output
     * @throws FileNotFoundException
     *             If an error occurred writing to the target stream.
     */
    OutputStream getOutputStream() throws FileNotFoundException {
        return outputOption.getOutputStream();
    }

    /**
     * Delivers an ID for the generated location information derived from the
     * input data.
     * 
     * @return An ID for the generated location information derived from the
     *         input data.
     */
    String getLocationID() {
        return locationID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<Argument<?>> getArguments() {
        return Collections.emptyList();
    }
}
