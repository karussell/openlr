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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import openlr.LocationReference;
import openlr.OpenLRProcessingException;
import openlr.binary.ByteArray;
import openlr.binary.OpenLRBinaryConstants;
import openlr.datex2.OpenLRDatex2Constants;
import openlr.encoder.LocationReferenceHolder;
import openlr.encoder.OpenLREncoder;
import openlr.encoder.OpenLREncoderParameter;
import openlr.location.Location;
import openlr.map.MapDatabase;
import openlr.otk.common.CommandLineParseException;
import openlr.otk.common.Format;
import openlr.otk.common.OpenLRCommandLineTool;
import openlr.otk.common.OpenLRToolkitException;
import openlr.otk.kml.ContentProvider;
import openlr.otk.kml.ContentProviderFactory;
import openlr.otk.kml.KmlGenerationException;
import openlr.otk.kml.KmlOutput;
import openlr.otk.kml.location.EncodedLocationStyles;
import openlr.otk.kml.locationreference.LocRefStyles;
import openlr.otk.utils.IOUtils;
import openlr.xml.OpenLRXMLConstants;

import org.apache.log4j.Logger;

/**
 * This class implements the tool that allows to encode locations on a dedicated
 * map.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class Encode extends OpenLRCommandLineTool<EncodeOptions> {
    
    /**
     * The logger
     */
    private static final Logger LOG = Logger.getLogger(Encode.class);

	/** The identifier of this tool. */
	public static final String TOOL_IDENTIFIER = "encode";

	/**
	 * The tool description
	 */
	private static final String TOOL_DESCRIPTION = "Encodes locations on an OpenLR map instance. "
			+ "The result can be stored in a file or visualized via KML representation. If option -d is missing "
	        + " all output is written to the console.";

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
	public final void executeImpl(final EncodeOptions optionsHandler)
			throws OpenLRToolkitException, CommandLineParseException {
		runAction(optionsHandler);
	}

	/**
	 * Do encoding.
	 * 
	 * @param options
	 *            The setup options and input data holder
	 * @throws OpenLRToolkitException
	 *             If an error occurred during writing
	 */
	private void runAction(final EncodeOptions options)
			throws OpenLRToolkitException {

		List<Location> locations = options.getLocations();
		prepareDirectories(options);

		OpenLREncoder encoder = new OpenLREncoder();
		OpenLREncoderParameter params = new OpenLREncoderParameter.Builder()
				.with(options.getMap()).with(options.getProperties())
				.buildParameter();
		int counter = 0;
		Map<String, Integer> locationIDs = new HashMap<String, Integer>();
		
		for (Location location : locations) {
			counter++;
			String locID = location.getID();
			if (locID == null || locID.isEmpty()) {
			    locID = Integer.toString(counter);
			}
			
			String fileNameBase = generateUniqueFileName(locationIDs, locID);
			
			try {
				LocationReferenceHolder locRefHolder = encoder.encodeLocation(
						params, location);
				handleLocationReference(locRefHolder, options, location, fileNameBase);
			} catch (OpenLRProcessingException e) {
				throw new OpenLRToolkitException(
						"Error while handling binary data " + e, e);
			}

		}
	}

    /**
     * Creates a unique file for this encode run. The file name is build from
     * locationID. If there was already a file name created for this id a
     * counter is appended.
     * 
     * @param locationIDs
     *            The location IDs processed so far.
     * @param locID
     *            The location ID to process.
     * @return The created name
     */
    private String generateUniqueFileName(
            final Map<String, Integer> locationIDs, final String locID) {

        String fileNameBase = locID;
        Integer count = locationIDs.get(locID);
        if (count == null) {
            count = Integer.valueOf(1);
        } else {
            fileNameBase = fileNameBase + "_" + count;
        }
        locationIDs.put(locID, count);
        return fileNameBase;
    }

	/**
	 * Handles a single location reference.
	 *
	 * @param locRefHolder the location reference holder
	 * @param options the encode options
	 * @param location the original location
	 * @param fileNameBase the base of the file name to write, the suffix will be appended
	 * @throws OpenLRToolkitException if processing failed
	 */
	private void handleLocationReference(final LocationReferenceHolder locRefHolder,
			final EncodeOptions options, final Location location, final String fileNameBase)
			throws OpenLRToolkitException {
		if (locRefHolder.isValid()) {
			Format of = options.getOutputFormat();
			if (of == Format.BINARY || of == Format.BINARY64) {
				handleBinaryLocRef(options, locRefHolder, of, fileNameBase);
			} else if (of == Format.DATEX2) {
				handleXmlLocRef(options, locRefHolder,
						OpenLRDatex2Constants.IDENTIFIER, fileNameBase);
			} else if (of == Format.XML) {
				handleXmlLocRef(options, locRefHolder,
						OpenLRXMLConstants.IDENTIFIER, fileNameBase);
			}
		} else {
			throw new OpenLRToolkitException("Location cannot be encoded: "
					+ locRefHolder.getReturnCode().name());
		}

		if (options.isKmlRequired()) {
			writeKml(options.getKmlOutputDir(), location, options.getMap(),
					locRefHolder, fileNameBase);
		}
	}
	
	/**
	 * Prepares the output directories.
	 *
	 * @param options the encode options
	 */
	private void prepareDirectories(final EncodeOptions options) {
		if (options.isKmlRequired()) {
			File kmlDir = options.getKmlOutputDir();
			if (!kmlDir.exists()) {
				kmlDir.mkdirs();
			}
		}

		File outDir = options.getOutputDirectory();
		if (outDir != null && !outDir.exists()) {
			outDir.mkdirs();
		}
	}

	/**
	 * Handle xml loc ref.
	 * 
	 * @param options
	 *            The setup options and input data holder
	 * @param locRefHolder
	 *            the loc ref holder
	 * @param specialType
	 *            The special type of XML format (expected:
	 * @param locID
	 *            the location id
	 * @throws OpenLRToolkitException
	 *             If an error occurs writing the data
	 *             {@link OpenLRXMLConstants.IDENTIFIER} or
	 *             {@link OpenLRDatex2Constants.IDENTIFIER})
	 */
	private void handleXmlLocRef(final EncodeOptions options,
			final LocationReferenceHolder locRefHolder,
			final String specialType, final String locID)
			throws OpenLRToolkitException {

		LocationReference locRef = getPhysicalLocationReference(locRefHolder,
				specialType);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			locRef.toStream(baos);
			String locString = baos.toString(IOUtils.SYSTEM_DEFAULT_CHARSET
					.name());
			baos.close();
			writeResult(options, locString, locID + ".xml");
		} catch (IOException e) {
			throw new OpenLRToolkitException("Error while writing "
					+ specialType + " data " + e.getMessage(), e);
		} catch (OpenLRProcessingException e) {
			throw new OpenLRToolkitException("Error while writing "
					+ specialType + " data " + e.getMessage(), e);
		}
	}

	/**
	 * Delivers the location reference of the given format from the holder and
	 * handles error cases.
	 * 
	 * @param locRefHolder
	 *            The location reference holder
	 * @param specialType
	 *            The identifier of the physical format
	 * @return The location reference, never {@code null}.
	 * @throws OpenLRToolkitException
	 *             If the physical location reference is not valid or
	 *             {@code null}
	 */
	private LocationReference getPhysicalLocationReference(
			final LocationReferenceHolder locRefHolder, final String specialType)
			throws OpenLRToolkitException {
		LocationReference locRef = locRefHolder
				.getLocationReference(specialType);
		if (locRef == null) {
			throw new OpenLRToolkitException(
					"No "
							+ specialType
							+ " location reference found in encoding result. Maybe the OpenLR "
							+ specialType
							+ " package is not in the class-path.");
		}
		if (!locRef.isValid()) {
			throw new OpenLRToolkitException(
					"Encoded location reference is not valid, message: "
							+ locRef.getReturnCode());
		}
		return locRef;
	}

	/**
	 * Handle binary loc ref.
	 * 
	 * @param options
	 *            The setup options and input data holder
	 * @param locRefHolder
	 *            the loc ref holder
	 * @param of
	 *            the of
	 * @param locID
	 *            the location id
	 * @throws OpenLRToolkitException
	 *             If an error occurs writing the data
	 */
	private void handleBinaryLocRef(final EncodeOptions options,
			final LocationReferenceHolder locRefHolder, final Format of,
			final String locID) throws OpenLRToolkitException {
		LocationReference locRef = getPhysicalLocationReference(locRefHolder,
				OpenLRBinaryConstants.IDENTIFIER);

		if (of == Format.BINARY) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			String locString;
			try {
				locRef.toStream(baos);
				locString = baos
						.toString(IOUtils.SYSTEM_DEFAULT_CHARSET.name());
				baos.close();
			} catch (IOException e) {
				throw new OpenLRToolkitException(
						"Error while printing binary data " + e.getMessage(), e);
			} catch (OpenLRProcessingException e) {
				throw new OpenLRToolkitException(
						"Error while writing binary data " + e.getMessage(), e);
			}
			writeResult(options, locString, locID + ".bin");
		} else {
			ByteArray ba = (ByteArray) locRef.getLocationReferenceData();
			String locString = ba.getBase64Data();
			writeResult(options, locString, locID + ".b64");
		}
	}

	/**
	 * Writes the result of the performed action to the configured output
	 * target.
	 * 
	 * @param options
	 *            The setup options and input data holder
	 * @param data
	 *            The data to write
	 * @param fileName
	 *            the file name
	 * @throws OpenLRToolkitException
	 *             If an error occurred during writing
	 */
	private void writeResult(final EncodeOptions options, final String data,
			final String fileName) throws OpenLRToolkitException {
		FileOutputStream fos = null;
		Writer writer = null;
		try {
			OutputStream os;
			if (options.hasOutputDirectory()) {
				File outFile = new File(options.getOutputDirectory(), fileName);
                if (outFile.exists()) {
                    LOG.warn("Output file already exists, will be overwritten: "
                            + outFile.getAbsolutePath());
                }
				fos = new FileOutputStream(outFile);
				os = fos;
			} else {
			    // we will reuse this default stream for subsequent writes, so don't close!
				os = getOutputStream();
			}

			writer = new OutputStreamWriter(os, IOUtils.SYSTEM_DEFAULT_CHARSET);
			writer.write(data);
			// add line break
			writer.write(IOUtils.LINE_SEPARATOR);
			writer.flush();

		} catch (IOException ioe) {
			throw new OpenLRToolkitException(
					"Error while saving the encoding result, message: "
							+ ioe.getMessage(), ioe);
		} finally {
		    // close only the file stream
		    if (fos != null) {
		        IOUtils.closeQuietly(fos);
		        IOUtils.closeQuietly(writer);
		    }
		}
	}

	@Override
	public final String getToolIdentifier() {
		return TOOL_IDENTIFIER;
	}

	/**
	 * Writes the output KML if this was requested by the user.
	 * 
	 * @param outputDir
	 *            The target directory
	 * @param location
	 *            The location to write
	 * @param mdb
	 *            The map database
	 * @param locRefHolder
	 *            The location reference holder
	 * @param locID
	 *            the location id
	 * @throws OpenLRToolkitException
	 *             If an error occurred generating the KML
	 */
	private void writeKml(final File outputDir, final Location location,
			final MapDatabase mdb, final LocationReferenceHolder locRefHolder,
			final String locID) throws OpenLRToolkitException {
		File outputFile = new File(outputDir, locID + ".kml");
        if (outputFile.exists()) {
            LOG.warn("Kml output file already exists, will be overwritten: "
                    + outputFile.getAbsolutePath());
        }
		KmlOutput kmlHelper = new KmlOutput(outputFile);
		ContentProvider<?> locPrint = ContentProviderFactory
				.createLocationContentProvider(location);
		locPrint.addStyles(new EncodedLocationStyles());
		locPrint.setLabel("Encoded location of map " + mdb);

		kmlHelper.addContentProvider(locPrint);

		if (locRefHolder.isValid()) {
			ContentProvider<?> locRefWriter = ContentProviderFactory
					.createLocationReferenceContentProvider(locRefHolder
							.getRawLocationReferenceData());

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

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final EncodeOptions getOptionsHandler() {
		return new EncodeOptions(TOOL_IDENTIFIER, TOOL_DESCRIPTION);
	}
}
