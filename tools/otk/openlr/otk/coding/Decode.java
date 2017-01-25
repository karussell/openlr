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
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import openlr.LocationReference;
import openlr.OpenLRProcessingException;
import openlr.decoder.OpenLRDecoder;
import openlr.decoder.OpenLRDecoderParameter;
import openlr.location.Location;
import openlr.location.utils.LocationDataWriter;
import openlr.map.MapDatabase;
import openlr.otk.common.CommandLineParseException;
import openlr.otk.common.OpenLRCommandLineTool;
import openlr.otk.common.OpenLRToolkitException;
import openlr.otk.kml.ContentProvider;
import openlr.otk.kml.ContentProviderFactory;
import openlr.otk.kml.KmlGenerationException;
import openlr.otk.kml.KmlOutput;
import openlr.otk.kml.location.DecodedLocationStyles;
import openlr.otk.utils.IOUtils;




/**
 * This class implements the tool that allows to decode location references on a
 * dedicated map.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class Decode extends OpenLRCommandLineTool<DecodeOptions> {

    /** The identifier of this tool. */
    public static final String TOOL_IDENTIFIER = "decode";

    /**
     * The tool description
     */
    private static final String TOOL_DESCRIPTION = "Decodes location references on an OpenLR map instance. "
            + "The result can be stored in a file or visualized via KML representation.";

    /**
     * Do decoding.
     * 
     * @param options
     *            The setup options and input data holder
     * @throws OpenLRToolkitException
     *             If the decode location was not valid
     */
    private void runAction(final DecodeOptions options)
            throws OpenLRToolkitException {

        MapDatabase mdb = options.getMap();
        LocationReference locRef = options.getLocationReference();
        OpenLRDecoder decoder = new OpenLRDecoder();
        OpenLRDecoderParameter params = new OpenLRDecoderParameter.Builder()
                .with(mdb).with(options.getProperties()).buildParameter();
        Location dLocation;
        try {
            dLocation = decoder.decode(params, locRef);
        } catch (OpenLRProcessingException e) {
            throw new OpenLRToolkitException(
                    "Location cannot be decoded: " + e, e);
        }
        if (dLocation.isValid()) {
            String locString = null;
            locString = LocationDataWriter.createLocationString(dLocation);
            writeResult(options, locString);

        } else {
            throw new OpenLRToolkitException(
                    "Location reference cannot be decoded: "
                            + dLocation.getReturnCode().name());
        }

        if (options.isKmlRequired()) {
            writeKml(options.getKmlOutputFile(), options.getMap(), dLocation);
        }
    }

    /**
     * OTK method.
     * 
     * @param optionsHandler
     *            the command line arguments.
     * @throws OpenLRToolkitException
     *             If an error occurred while running this tool
     * @throws CommandLineParseException
     *             In case of an error processing the input parameters
     */
    @Override
    public final void executeImpl(final DecodeOptions optionsHandler)
            throws OpenLRToolkitException, CommandLineParseException {

        runAction(optionsHandler);
    }

    /**
     * Writes the result of the performed action to the configured output target
     * 
     * @param options
     *            The setup options and input data holder
     * @param data
     *            The data to write
     * @throws OpenLRToolkitException
     *             If an error occurred during writing
     */
    private void writeResult(final DecodeOptions options,
            final String data) throws OpenLRToolkitException {
        Writer fw = null;
        try {
            fw = new OutputStreamWriter(options.getOutputStream(),
                    IOUtils.SYSTEM_DEFAULT_CHARSET);
            fw.write(data);
            // add line break
            fw.write(System.getProperty("line.separator", "\n"));            
            fw.flush();
            fw.close();
        } catch (IOException ioe) {
            throw new OpenLRToolkitException(
                    "Error while saving the encoding result, message: "
                            + ioe.getMessage(), ioe);
        } finally {
            IOUtils.closeQuietly(fw);
        }
    }

    @Override
    public final String getToolIdentifier() {
        return TOOL_IDENTIFIER;
    }

    /**
     * Writes the output KML if this was requested by the user.
     * 
     * @param outputFile
     *            The target file
     * @param location
     *            The location to write
     * @param mdb
     *            The map database
     * @throws OpenLRToolkitException
     *             If an error occurred generating the KML
     */
    private void writeKml(final File outputFile, final MapDatabase mdb,
            final Location location) throws OpenLRToolkitException {

        KmlOutput kmlHelper = new KmlOutput(outputFile);
        ContentProvider<?> locPrint = ContentProviderFactory
                .createLocationContentProvider(location);
        locPrint.addStyles(new DecodedLocationStyles());
        locPrint.setLabel("Decoded location of map " + mdb);
        
        kmlHelper.addContentProvider(locPrint);

        try {
            if (!kmlHelper.write()) {
                throw new OpenLRToolkitException(
                        "Error while writing the kml output file.");
            }
        } catch (IOException e) {
            throw new OpenLRToolkitException(
                    "Error while writing the kml output file "
                            + outputFile.getAbsolutePath(), e);
        } catch (KmlGenerationException e) {
            throw new OpenLRToolkitException(
                    "Error while generating the kml output, message: "
                            + e.getMessage(), e);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public final DecodeOptions getOptionsHandler() {
        return new DecodeOptions(TOOL_IDENTIFIER, TOOL_DESCRIPTION);
    }
}
