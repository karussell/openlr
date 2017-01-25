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
package openlr.otk.locrefboundary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import openlr.LocationReference;
import openlr.PhysicalFormatException;
import openlr.binary.ByteArray;
import openlr.binary.OpenLRBinaryDecoder;
import openlr.map.InvalidMapDataException;
import openlr.map.RectangleCorners;
import openlr.otk.common.CommandLineParseException;
import openlr.otk.common.OpenLRCommandLineTool;
import openlr.otk.common.OpenLRToolkitException;
import openlr.otk.kml.KmlGenerationException;
import openlr.otk.kml.KmlOutput;
import openlr.otk.kml.StyleIdentifier;
import openlr.otk.utils.IOUtils;
import openlr.rawLocRef.RawLocationReference;
import openlr.utils.locref.boundary.LocRefBoundary;
import openlr.xml.OpenLRXMLDecoder;
import openlr.xml.OpenLRXmlReader;
import openlr.xml.generated.OpenLR;
import openlr.xml.impl.LocationReferenceXmlImpl;

import org.xml.sax.SAXException;

import de.micromata.opengis.kml.v_2_2_0.Style;

/**
 * Calculates a bounding box for a location reference of sufficient size to
 * decode the location reference on this part of the target map. This enables to
 * limit loading of map data for only this portion.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class LocRefBoundaryTool extends OpenLRCommandLineTool<LocRefBoundaryOptions> {

    /**
     * The suffix attached to each created output file
     */
    private static final String OUTFILE_SUFFIX = "-boundary.kml";

    /**
     * The tool description
     */
    private static final String TOOL_DESCRIPTION = "Calculates bounding boxes from location references and draws them via KML. "
            + "The created bounding box describes the sufficient part of the target map to decode the location reference. \n"
            + "The tool has two input modes. "
            + "If the input source links to a file the tool processes only this single file. "
            + "If the specified input source matches a directory the tool will process every file in it matching the following naming schema. \n"
            + "The tool assumes to find location reference in the contained files by the following conditions. Files ending with \".stream\""
            + "are processed as pure binary OpenLR location references. Files ending with \".b64\" are assumed to contain Base 64 encoded "
            + "OpenLR binary location references. Files with suffix \".xml\" are tried to process as OpenLR location references in XML format.\n"
            + "The resulting KML files are placed into the current working directory or into a dedicated directory if specified. "
            + "The file names match the input file, but with suffix \""
            + OUTFILE_SUFFIX + "\".";

    /** The Constant TOOL_IDENTIFIER. */
    public static final String TOOL_IDENTIFIER = "bbox";  

    /**
     * The single style identifier used in this tool.
     */
    private static final StyleIdentifier THE_STYLE = new StyleIdentifier() {

        @Override
        public String getID() {
            return "styleID";
        }
    };

    /**
     * The main method.
     * 
     * @param clData
     *            the arguments
     * @throws OpenLRToolkitException
     *             If an error occurred while running this tool
     * @throws CommandLineParseException
     *             If an error occurred processing the command line arguments
     */
    @Override
    public final void executeImpl(final LocRefBoundaryOptions clData)
            throws OpenLRToolkitException, CommandLineParseException {

        // check parameter and input and output files
        File inSource = clData.getInputSource();
        File outDir = clData.getOutputDirectory();
        if (outDir == null) {
            outDir = new File(".");
        }      

        Map<String, RawLocationReference> data = new HashMap<String, RawLocationReference>();
        if (inSource.isDirectory()) {
            for (File f : inSource.listFiles()) {
                RawLocationReference rlr = readLocationReferencesFromFile(f);
                if (rlr != null) {
                    data.put(f.getName(), rlr);
                }
            }
        } else {
            RawLocationReference rlr = readLocationReferencesFromFile(inSource);
            if (rlr != null) {
                data.put(inSource.getName(), rlr);
            }
        }
        for (Map.Entry<String, RawLocationReference> entry : data.entrySet()) {
            RawLocationReference rawLocRef = entry.getValue();
            String fileName = entry.getKey();

            File out = new File(outDir, fileName + OUTFILE_SUFFIX);
            KmlOutput kmlOut = new KmlOutput(out);

            try {
                RectangleCorners rcMax = calculateBoundingBox(rawLocRef);

                BoundingBoxKmlCreator kmlWriterMax = createKmlWriter(rcMax,
                        "bounding box",
                        "Bounding box defining a sufficiant boundig box for location reference \""
                                + rawLocRef.getID() + "\" from file "
                                + fileName);
                kmlOut.addContentProvider(kmlWriterMax);

                kmlOut.write();
            } catch (KmlGenerationException e) {
                throw new OpenLRToolkitException(
                        "Error generating the KML for location reference \""
                                + rawLocRef.getID() + "\" from file "
                                + fileName, e);
            } catch (IOException e) {
                throw new OpenLRToolkitException(
                        "Error writing the KML for location reference \""
                                + rawLocRef.getID() + "\" from file "
                                + fileName, e);
            } catch (InvalidMapDataException e) {
                throw new OpenLRToolkitException(
                        "Error calculating the coordinates of the bounding box for location reference \""
                                + rawLocRef.getID()
                                + "\" from file "
                                + fileName, e);
            }
        }

        try {
            getOutputStream().write(
                    ("finished: created " + data.size() + " kml files")
                            .getBytes(IOUtils.SYSTEM_DEFAULT_CHARSET));
        } catch (IOException e) {
            throw new OpenLRToolkitException(
                    "Error writing the summary output", e);
        }
    }

    /**
     * Determines the proper calculator for the given location reference and
     * executes the calculation of the bounding box.
     * 
     * @param rawLocRef
     *            The processed location reference
     * @return The bounding box
     * @throws InvalidMapDataException
     *             If an error occurred calculation geo-coordinates of the
     *             bounding box
     */
    private RectangleCorners calculateBoundingBox(
            final RawLocationReference rawLocRef)
            throws InvalidMapDataException {

        return LocRefBoundary.calculateLocRefBoundary(rawLocRef);
    }

    /**
     * Creates and sets up a {@link BoundingBoxKmlCreator} to write the given
     * box to KML.
     * 
     * @param bbox
     *            The box to write
     * @param name
     *            The name of the KML element
     * @param description
     *            A description of the box
     * @return The setup bounding box writer
     */
    private BoundingBoxKmlCreator createKmlWriter(final RectangleCorners bbox,
            final String name, final String description) {
        BoundingBoxKmlCreator bboxWriter = new BoundingBoxKmlCreator(bbox,
                name, description, THE_STYLE);
        bboxWriter.setLabel(name);
        Style minStyle = new Style().withId(THE_STYLE.getID());
        minStyle.createAndSetLineStyle().withColor("ff0000ff").withWidth(2.0);
        bboxWriter.addStyles(Arrays.asList(minStyle));
        return bboxWriter;
    }

    /**
     * Read binaries from file.
     * 
     * @param f
     *            the f
     * @return the list< map loc binary>
     * @throws OpenLRToolkitException
     *             If an error occurred while reading the location references
     */
    private RawLocationReference readLocationReferencesFromFile(final File f)
            throws OpenLRToolkitException {
        String fname = f.getName();
        if (fname.endsWith(".stream")) {
            try {
                InputStream fr = new FileInputStream(f);
                ByteArray ba = new ByteArray(fr);
                LocationReference lr = new openlr.binary.impl.LocationReferenceBinaryImpl(
                        fname, ba);
                fr.close();
                OpenLRBinaryDecoder decoder = new OpenLRBinaryDecoder();
                return decoder.decodeData(lr);
            } catch (IOException e) {
                throw new OpenLRToolkitException(
                        "Error reading input file " + fname, e);
            } catch (PhysicalFormatException e) {
                throw new OpenLRToolkitException(
                        "Error decoding input file " + fname, e);
            }
        } else if (fname.endsWith(".b64")) {
            BufferedReader fr = null;
            try {
                fr = new BufferedReader(new InputStreamReader(
                        new FileInputStream(f),
                        IOUtils.SYSTEM_DEFAULT_CHARSET));
                String b64 = fr.readLine();
                ByteArray ba = new ByteArray(b64);
                LocationReference lr = new openlr.binary.impl.LocationReferenceBinaryImpl(
                        fname, ba);
                fr.close();
                OpenLRBinaryDecoder decoder = new OpenLRBinaryDecoder();
                return decoder.decodeData(lr);
            } catch (IOException e) {
                throw new OpenLRToolkitException(
                        "Error reading input file " + fname, e);
            } catch (PhysicalFormatException e) {
                throw new OpenLRToolkitException(
                        "Error decoding input file " + fname, e);            
            } finally {
                IOUtils.closeQuietly(fr);
            }
        } else if (fname.endsWith(".xml")) {
            try {
                OpenLRXmlReader reader = new OpenLRXmlReader();
                OpenLR xmlData = reader.readOpenLRXML(f, true);
                LocationReference lr = new LocationReferenceXmlImpl(fname,
                        xmlData, 1);
                OpenLRXMLDecoder decoder = new OpenLRXMLDecoder();
                return decoder.decodeData(lr);
            } catch (IOException e) {
                throw new OpenLRToolkitException(
                        "Error reading input file " + fname, e);
            } catch (PhysicalFormatException e) {
                throw new OpenLRToolkitException(
                        "Error decoding input file " + fname, e);
            } catch (JAXBException e) {
                throw new OpenLRToolkitException(
                        "invalid xml file " + fname, e);
            } catch (SAXException e) {
                throw new OpenLRToolkitException(
                        "invalid xml file " + fname, e);
            }
        }
        return null;
    }

    @Override
    public final String getToolIdentifier() {
        return TOOL_IDENTIFIER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final LocRefBoundaryOptions getOptionsHandler() {
        return new LocRefBoundaryOptions(TOOL_IDENTIFIER, TOOL_DESCRIPTION);
    }

}
