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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import openlr.LocationReference;
import openlr.PhysicalFormatException;
import openlr.otk.common.CommandLineParseException;
import openlr.otk.common.OpenLRCommandLineTool;
import openlr.otk.common.OpenLRToolkitException;
import openlr.otk.kml.ContentProvider;
import openlr.otk.kml.ContentProviderFactory;
import openlr.otk.kml.KmlGenerationException;
import openlr.otk.kml.KmlOutput;
import openlr.otk.kml.locationreference.LocRefStyles;
import openlr.otk.utils.DataResolver;
import openlr.otk.utils.DataResolverException;
import openlr.rawLocRef.RawLocationReference;
import openlr.xml.OpenLRXMLDecoder;
import openlr.xml.generated.OpenLR;
import openlr.xml.impl.LocationReferenceXmlImpl;

import org.apache.log4j.Logger;

/**
 * Creates KML representations of OpenLR location references in XML format found in a specified directory
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class Xml2Kml extends OpenLRCommandLineTool<Xml2KmlOptions> {

    /**
     * The tool description
     */
    private static final String TOOL_DESCRIPTION = "Creates KML representations of OpenLR location references in XML format. \n"
            + "The tool has two input modes. "
            + "If the specified input source matches a directory the tool will process every file ending with \".xml\" in it. "
            + "If the input source links to a file the tool processes only this single file. \n"
            + "The resulting KML files are placed into the current working directory or into a dedicated directory if specified. "
            + "The file names match the input file, but with extension \".kml\".";

    /**
     * The logger for this class
     */
    private static final Logger LOG = Logger.getLogger(Xml2Kml.class);

	/** The Constant TOOL_IDENTIFIER. */
	public static final String TOOL_IDENTIFIER = "xml2kml";
	

	/**
	 * The main method.
	 * 
	 * @param clData
	 *            the arguments
     * @throws OpenLRToolkitException
     *             If an error occurred while running this tool
	 * @throws CommandLineParseException 
	 */
	@Override
    public final void executeImpl(final Xml2KmlOptions clData) throws OpenLRToolkitException, CommandLineParseException {

		// check parameter and input and output files
		File inSource = clData.getInputSource();
		File outDir = clData.getOutputDirectory();
		if (outDir == null) {
		    outDir = new File(".");
		}

        Map<File, OpenLR> data;
        if (inSource.isDirectory()) {
            data = resolveXmlDataFromDirectory(inSource);
        } else {
            try {
                data = new HashMap<File, OpenLR>();
                data.put(inSource, resolveXmlDataFromFile(inSource));
            } catch (DataResolverException e) {
                throw new OpenLRToolkitException(
                        "File seems to contain no valid OpenLR location reference in XML format",
                        e);
            }
        }
        
		OpenLRXMLDecoder xmlDecoder = new OpenLRXMLDecoder();

		for (Map.Entry<File, OpenLR> entry : data.entrySet()) {
		    OpenLR xmlData = entry.getValue();
		    File file = entry.getKey();
		    LocationReference locRef = new LocationReferenceXmlImpl(xmlData.getLocationID() + ", file: " + file.getName(), xmlData, 1);

            RawLocationReference rawLocRef;
            try {
                rawLocRef = xmlDecoder.decodeData(locRef);
            } catch (PhysicalFormatException e) {
                throw new OpenLRToolkitException(
                        "Error decoding the data of location reference \""
                                + xmlData.getLocationID() + "\" from file "
                                + file.getName() + ", message "
                                + e.getErrorCode(), e);
            }
            
            String fileBaseName = file.getName().substring(0, file.getName().lastIndexOf('.'));

            File outputFile = new File(outDir, fileBaseName + ".kml");
		    writeKml(outputFile, rawLocRef);
		    
		      LOG.info("wrote: " + outputFile.getAbsolutePath());
		}

		LOG.info("finished: created " + data.size() + " kml files");
    }

    /**
     * Tries to identify all OpenLR XML location references in the given
     * directory and delivers them deserialized to {@link OpenLR} objects
     * 
     * @param inputDir
     *            The search directory
     * @return A map containing each found location reference with the related
     *         source file
     * @throws OpenLRToolkitException
     *             If an error occurs resolving OpenLR from the XML files in the
     *             given directory
     */
    private static Map<File, OpenLR> resolveXmlDataFromDirectory(
            final File inputDir) throws OpenLRToolkitException {
        Map<File, OpenLR> data = new HashMap<File, OpenLR>();
        for (File f : inputDir.listFiles()) {
            if (f.getName().endsWith(".xml")) {
                try {
                    data.put(f, resolveXmlDataFromFile(f));
                } catch (DataResolverException e) {
                    LOG.warn("Warning: " + f.getName()
                            + " is not an OpenLR location reference");
                }
            }
        }
        return data;
    }

    /**
     * Tries to create an instance of {@link OpenLR} from the content of the
     * given file.
     * 
     * @param file
     *            The file to process
     * @return The created OpenLR
     * @throws DataResolverException
     *             If it was not possible to create OpenLR from the file
     *             content.
     * @throws OpenLRToolkitException
     *             If an unexpected error occurred when trying to process the
     *             file
     */
    private static OpenLR resolveXmlDataFromFile(final File file)
            throws DataResolverException, OpenLRToolkitException {

        try {
            OpenLR xmlData = DataResolver.resolveXmlData(new FileInputStream(
                    file));
            return xmlData;
        } catch (FileNotFoundException e) {
            throw new OpenLRToolkitException("input file not found! "
                    + e.getMessage(), e);
        }
    }
	
    /**
     * Writes the output KML if this was requested by the user.
     * 
     * @param outputFile
     *            The target file
     * @param rawLocRef
     *            The location reference
     * @throws OpenLRToolkitException
     *             If an error occurred generating the KML
     */
    private void writeKml(final File outputFile, final RawLocationReference rawLocRef)
            throws OpenLRToolkitException {

        KmlOutput kmlHelper = new KmlOutput(outputFile);

        if (rawLocRef.isValid()) {
            ContentProvider<?> locRefWriter = ContentProviderFactory
                    .createLocationReferenceContentProvider(rawLocRef);

            locRefWriter.addStyles(new LocRefStyles());
            locRefWriter.setLabel("Location Reference");
            kmlHelper.addContentProvider(locRefWriter);

        }
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

    @Override
    public final String getToolIdentifier() {
        return TOOL_IDENTIFIER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final Xml2KmlOptions getOptionsHandler() {

        return new Xml2KmlOptions(TOOL_IDENTIFIER, TOOL_DESCRIPTION);

    }
}
