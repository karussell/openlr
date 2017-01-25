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

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import openlr.LocationType;
import openlr.PhysicalFormatException;
import openlr.binary.ByteArray;
import openlr.binary.OpenLRBinaryDecoder;
import openlr.binary.data.FirstLRP;
import openlr.binary.data.Header;
import openlr.binary.data.IntermediateLRP;
import openlr.binary.data.LastClosedLineLRP;
import openlr.binary.data.LastLRP;
import openlr.binary.data.Offset;
import openlr.binary.data.RawBinaryData;
import openlr.binary.data.RelativeCoordinates;
import openlr.binary.impl.LocationReferenceBinaryImpl;
import openlr.location.data.Orientation;
import openlr.location.data.SideOfRoad;
import openlr.map.GeoCoordinates;
import openlr.otk.binview.data.ByteValue;
import openlr.otk.binview.data.Bytes;
import openlr.otk.binview.data.CoordinateValue;
import openlr.otk.binview.data.FirstValue;
import openlr.otk.binview.data.IntermediateValue;
import openlr.otk.binview.data.LastClosedLineValue;
import openlr.otk.binview.data.LastValue;
import openlr.otk.binview.data.OffsetValue;
import openlr.otk.common.CommandLineParseException;
import openlr.otk.common.OpenLRCommandLineTool;
import openlr.otk.common.OpenLRToolkitException;
import openlr.otk.kml.ContentProvider;
import openlr.otk.kml.ContentProviderFactory;
import openlr.otk.kml.KmlGenerationException;
import openlr.otk.kml.KmlOutput;
import openlr.otk.kml.locationreference.LocRefStyles;
import openlr.otk.utils.Formatter;
import openlr.otk.utils.IOUtils;
import openlr.rawLocRef.RawLocationReference;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 * The Class BinaryDataViewer is the main class of the OpenLR binary data viewer
 * tool. This tool can be started from command line and it can also be used by
 * other applications. It transforms a binary location reference (either
 * Base64-encoded or as a binary stream) into a human-readable format. The
 * output is configurable using Velocity templates.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * 
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public final class BinaryDataViewer extends OpenLRCommandLineTool<BinaryViewerOptions> {

    /** The Constant VERSION_WITH_POINT_LOCATION. */
    private static final int VERSION_WITH_POINT_LOCATION = 3;

    /** The Constant TEN. */
    private static final int TEN = 10;

    /** The Constant THOUSAND_METERS. */
    private static final double THOUSAND_METERS = 1000.0;

    /** The Constant DECA_MICRO_DEG_FACTOR. */
    private static final double DECA_MICRO_DEG_FACTOR = 100000.0;

    /** The logger. */
    private static final Logger LOG = Logger.getLogger(BinaryDataViewer.class);

    /** The identifier of this tool. */
    public static final String TOOL_IDENTIFIER = "binview";

    /** The Constant MAJOR_VERSION. */
    private static final int MAJOR_VERSION = 1;

    /** The Constant MINOR_VERSION. */
    private static final int MINOR_VERSION = 4;

    /** The Constant PATCH_VERSION. */
    private static final int PATCH_VERSION = 0;

    /**
     * A reused reference to the binary decoder
     */
    private static final OpenLRBinaryDecoder BIN_DECODER = new OpenLRBinaryDecoder();

    /**
     * The tool description
     */
    private static final String TOOL_DESCRIPTION = "Prints the data contained in a binary location reference. "
            + "The location reference can consist of either pure binary or base 64 encoded binary data provided via file, "
            + "direct specification in the command line or piped in. The content is printed in a readable way and/ or visualized via KML.";

    /**
     * The main method. It parses the command line options and prepares the
     * input and output files. If all options and files are valid the data
     * string generation will be started.
     * 
     * @param clData
     *            the command line options.
     * @throws OpenLRToolkitException
     *             If an error occurred while running this tool
     * @throws CommandLineParseException
     *             In case of an error processing the input parameters
     */
    @Override
    public void executeImpl(final BinaryViewerOptions clData)
            throws OpenLRToolkitException, CommandLineParseException {


        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("command line options are parsed, create data string now ...");
            }
            String output = createDataString(clData.getBinData(),
                    clData.getLocationID());

            writeResult(clData, output);

            if (clData.isKmlRequired()) {

                try {
                    writeKml(clData.getKmlOutputFile(),
                            decodeBinaryData(clData.getBinData()));
                } catch (PhysicalFormatException e) {
                    throw new ViewerException(e.getErrorCode().toString()
                            + "Error during KML creation", e);
                }
            }

        } catch (ViewerException e) {
            throw new OpenLRToolkitException(e.getMessage(), e);
        }
    }

    /**
     * Transforms a base64 encoded string into binary data.
     * 
     * @param base64String
     *            the base64 string
     * 
     * @return the binary data
     */
    static byte[] transformBase64(final String base64String) {
        Base64 converter = new Base64();
        return converter.decode(base64String);
    }

    /**
     * Creates the human readable data string from the binary data.
     * 
     * @param data
     *            the binary data
     * @param id
     *            the unique id
     * 
     * @return the human readable data string
     * 
     * @throws ViewerException
     *             if converting the data failed
     */
    static synchronized String createDataString(final byte[] data,
            final String id) throws ViewerException {
        StringBuilder textBuilder = new StringBuilder();
        textBuilder.append("\n\nOTK binview, version ").append(getVersion())
                .append("\n\n");
        textBuilder.append("Data id: ").append(id).append("\n\n");

        // read in binary data
        RawLocationReference rawLocRef;
        RawBinaryData rawBinData;
        try {
            rawLocRef = decodeBinaryData(data);
            rawBinData = resolveBinaryData(data);

        } catch (PhysicalFormatException e) {
            throw new ViewerException(e.getErrorCode().toString()
                    + " Maybe you forgot the " + BinaryViewerOptions.B64_OPTION
                    + " option but provided base 64 input?", e);
        }
        Bytes bytes = new Bytes(data);
        processBinaryData(rawLocRef, rawBinData, textBuilder, bytes);
        textBuilder.append("\n");
        return textBuilder.toString();
    }

    /**
     * Writes the result of the performed action to the configured output target
     * 
     * @param options
     *            the tool options to get the output stream from
     * @param data
     *            The data to write
     * @throws ViewerException
     *             If an error occurred during writing
     */
    private static void writeResult(final BinaryViewerOptions options,
            final String data) throws ViewerException {
        Writer fw = null;
        try {
            fw = new OutputStreamWriter(options.getOutputStream(),
                    IOUtils.SYSTEM_DEFAULT_CHARSET);
            fw.write(data);
            fw.flush();
            fw.close();
        } catch (IOException ioe) {
            throw new ViewerException(
                    "Error while saving the encoding result, message: "
                            + ioe.getMessage(), ioe);
        } finally {
            IOUtils.closeQuietly(fw);
        }
    }

    /**
     * Read in binary data and fill the Velocity context.
     * 
     * @param rawLocRef
     *            the raw location reference data
     * @param binData
     *            the raw binary location reference data
     * @param textBuilder
     *            the text builder
     * @param bytes
     *            The raw location reference bytes
     * 
     * @throws ViewerException
     *             if conversion failed
     */
    private static void processBinaryData(final RawLocationReference rawLocRef,
            final RawBinaryData binData, final StringBuilder textBuilder,
            final Bytes bytes) throws ViewerException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read in data and fill context");
        }

        // process byte values
        processBytes(textBuilder, bytes);
        textBuilder.append("\n\n\n");
        // read in data
        Header header = binData.getBinaryHeader();
        LocationType locType = rawLocRef.getLocationType();
        if (header != null) { // can be null in case of invalid location
            processHeader(textBuilder, header, locType);
        }

        processLocType(textBuilder, locType);

        switch (locType) {
        case LINE_LOCATION:
            handleLineLocation(textBuilder, binData, header);
            break;
        case GEO_COORDINATES:
            handleGeoCoordLocation(textBuilder, rawLocRef, binData);
            break;
        case POINT_ALONG_LINE:
            handlePointAlongLocation(textBuilder, rawLocRef, binData, header);
            break;
        case POI_WITH_ACCESS_POINT:
            handlePoiAccessLocation(textBuilder, rawLocRef, binData, header);
            break;
        case CIRCLE:
            handleCircleLocation(textBuilder, rawLocRef, binData);
            break;
        case POLYGON:
            handlePolygonLocation(textBuilder, rawLocRef);
            break;
        case CLOSED_LINE:
            handleClosedLineLocation(textBuilder, binData);
            break;
        case RECTANGLE:
            handleRectangleLocation(textBuilder, rawLocRef, binData);
            break;
        case GRID:
            handleGridLocation(textBuilder, rawLocRef, binData);
            break;
        case UNKNOWN:
            LOG.warn("Decoded location reference is invalid! Error: "
                    + rawLocRef.getReturnCode());
            break;
        default:
            throw new ViewerException("Unexpected location type: " + locType);
        }
        // context is filled up with data
        if (LOG.isDebugEnabled()) {
            LOG.debug("data is read, context is being filled");
        }
    }

    /**
     * Process header information.
     * 
     * @param textBuilder
     *            the text builder
     * @param header
     *            the header
     * @param locType
     *            the loc type
     */
    private static void processHeader(final StringBuilder textBuilder,
            final Header header, final LocationType locType) {
        textBuilder.append(" Header\n");
        textBuilder.append(" ======\n");
        textBuilder.append("  Version:            ").append(header.getVer())
                .append("\n");
        if (header.getVer() >= VERSION_WITH_POINT_LOCATION) {
            textBuilder.append("  Point flag:                  ")
                    .append(header.getPf()).append(" [");
            if (header.getPf() == 1) {
                textBuilder.append("true]\n");
            } else {
                textBuilder.append("false]\n");
            }
        }

        textBuilder.append("  Area flag:                   ")
                .append(header.getArf()).append(" [");
        if (LocationType.AREA_LOCATIONS.contains(locType)) {
            textBuilder.append("true]\n");
        } else {
            textBuilder.append("false]\n");
        }
        textBuilder.append("  Attribute flag:              ")
                .append(header.getAf()).append(" [");
        if (header.getAf() == 1) {
            textBuilder.append("true]\n");
        } else {
            textBuilder.append("false]\n");
        }
        textBuilder.append("\n\n");
    }

    /**
     * Process raw bytes information.
     * 
     * @param textBuilder
     *            the text builder
     * @param bytes
     *            the bytes
     */
    private static void processBytes(final StringBuilder textBuilder,
            final Bytes bytes) {
        textBuilder.append(" Bytes\n").append(" =====\n\n");
        textBuilder.append("  total number of bytes: ")
                .append(bytes.getBytes().size()).append("\n\n");
        int count = 0;
        for (ByteValue b : bytes.getBytes()) {
            textBuilder.append("  byte: ");
            textBuilder.append(formatNumber(count)).append(" [")
                    .append(b.bitValue()).append("]  ").append(b.hexValue())
                    .append("\n");
            count++;
        }
    }

    /**
     * Format (positive) numbers with leading 0.
     * 
     * @param value
     *            the integer value
     * @return the formatted string
     */
    private static String formatNumber(final int value) {
        if (value >= TEN) {
            return Integer.toString(value);
        } else {
            return "0" + Integer.toString(value);
        }
    }

    /**
     * Decodes the binary data of the location reference
     * 
     * @param data
     *            The location reference binary data
     * @return The location reference object
     * @throws PhysicalFormatException
     *             If an error occurred during binary decoding
     */
    private static RawLocationReference decodeBinaryData(final byte[] data)
            throws PhysicalFormatException {
        ByteArray ba = new ByteArray(data);
        return BIN_DECODER.decodeData(new LocationReferenceBinaryImpl("", ba));
    }

    /**
     * Extracts the binary data of the location reference
     * 
     * @param data
     *            The location reference binary data
     * @return The location reference object
     * @throws PhysicalFormatException
     *             If an error occurred during binary decoding
     */
    private static RawBinaryData resolveBinaryData(final byte[] data)
            throws PhysicalFormatException {
        ByteArray ba = new ByteArray(data);
        return BIN_DECODER.resolveBinaryData("", ba);
    }

    /**
     * Handle poi access location.
     * 
     * @param textBuilder
     *            the text builder
     * @param rawLocRef
     *            the raw loc ref
     * @param binData
     *            The binary data
     * @param header
     *            the header
     */
    private static void handlePoiAccessLocation(
            final StringBuilder textBuilder,
            final RawLocationReference rawLocRef, final RawBinaryData binData,
            final Header header) {
        FirstLRP first = binData.getBinaryFirstLRP();
        double firstLat = CoordinateValue
                .get32BitRepresentation(first.getLat());
        double firstLon = CoordinateValue
                .get32BitRepresentation(first.getLon());
        processFirstLRP(textBuilder, first);
        textBuilder.append("\n");
        processLastLRP(textBuilder, binData.getBinaryLastLRP(), firstLon,
                firstLat, 2);
        textBuilder.append("\n");
        processOffsets(textBuilder, binData.getBinaryPosOffset(), null,
                header.getVer());
        textBuilder.append("\n");
        RelativeCoordinates relCoord = binData.getBinaryRelativeCoordinates();
        double lon = firstLon + (relCoord.getLon() / DECA_MICRO_DEG_FACTOR);
        double lat = firstLat + (relCoord.getLat() / DECA_MICRO_DEG_FACTOR);

        textBuilder.append(" Geo-Coordinate\n");
        textBuilder.append(" ==============\n");
        textBuilder.append(" Longitude: ").append(Formatter.formatCoord(lon))
                .append("\n");
        textBuilder.append(" Latitude: ").append(Formatter.formatCoord(lat))
                .append("\n");
        textBuilder.append("\n");
        processAdditionalInfo(textBuilder, rawLocRef.getSideOfRoad(),
                rawLocRef.getOrientation());
    }

    /**
     * Process last lrp.
     * 
     * @param textBuilder
     *            the text builder
     * @param binaryLastLRP
     *            the binary last lrp
     * @param prevLon
     *            the prev lon
     * @param prevLat
     *            the prev lat
     * @param id
     *            the id
     */
    private static void processLastLRP(final StringBuilder textBuilder,
            final LastLRP binaryLastLRP, final double prevLon,
            final double prevLat, final int id) {
        textBuilder.append(" Last LRP\n");
        textBuilder.append(" ========\n");
        textBuilder.append("  ID: ").append(id).append("\n");
        LastValue lv = new LastValue(id, binaryLastLRP, prevLon, prevLat);
        textBuilder.append("  Longitude: ").append(lv.getLongitudeDeg())
                .append("\n");
        textBuilder.append("  Latitude:  ").append(lv.getLatitudeDeg())
                .append("\n");
        textBuilder.append("  Functional road class: ").append(lv.getFrc())
                .append(" [").append(lv.getFrc().name()).append("]\n");
        textBuilder.append("  Form of way: ").append(lv.getFow()).append(" [")
                .append(lv.getFow().name()).append("]\n");
        textBuilder.append("  Bearing sector: ").append(lv.getBear())
                .append(" [").append(lv.getBear().lowerBound()).append(" - ")
                .append(lv.getBear().upperBound()).append("]\n");

        textBuilder.append("  Positive offset flag: ").append(lv.getPosOff());
        if (lv.getPosOff() == 1) {
            textBuilder.append(" [true]\n");
        } else {
            textBuilder.append(" [false]\n");
        }
        textBuilder.append("  Negative offset flag: ").append(lv.getNegOff());
        if (lv.getNegOff() == 1) {
            textBuilder.append(" [true]\n");
        } else {
            textBuilder.append(" [false]\n");
        }
    }

    /**
     * Handle point along location.
     * 
     * @param textBuilder
     *            the text builder
     * @param rawLocRef
     *            the raw loc ref
     * @param binData
     *            The binary data
     * @param header
     *            the header
     */
    private static void handlePointAlongLocation(
            final StringBuilder textBuilder,
            final RawLocationReference rawLocRef, final RawBinaryData binData,
            final Header header) {
        FirstLRP first = binData.getBinaryFirstLRP();
        processFirstLRP(textBuilder, first);
        textBuilder.append("\n");
        processLastLRP(textBuilder, binData.getBinaryLastLRP(),
                CoordinateValue.get32BitRepresentation(first.getLon()),
                CoordinateValue.get32BitRepresentation(first.getLat()), 2);
        textBuilder.append("\n");
        processOffsets(textBuilder, binData.getBinaryPosOffset(), null,
                header.getVer());
        textBuilder.append("\n");
        processAdditionalInfo(textBuilder, rawLocRef.getSideOfRoad(),
                rawLocRef.getOrientation());
    }

    /**
     * Process additional info.
     * 
     * @param textBuilder
     *            the text builder
     * @param sor
     *            the sor
     * @param or
     *            the or
     */
    private static void processAdditionalInfo(final StringBuilder textBuilder,
            final SideOfRoad sor, final Orientation or) {
        textBuilder.append(" Additional information\n");
        textBuilder.append(" ======================\n");
        textBuilder.append(" Side of road: ").append(sor.name()).append("\n");
        textBuilder.append(" Orientation: ").append(or.name()).append("\n");
    }

    /**
     * Process offsets.
     * 
     * @param textBuilder
     *            the text builder
     * @param posOffset
     *            the pos offset
     * @param negOffset
     *            the neg offset
     * @param version
     *            the version
     */
    private static void processOffsets(final StringBuilder textBuilder,
            final Offset posOffset, final Offset negOffset, final int version) {
        textBuilder.append(" Offsets\n");
        textBuilder.append(" =========\n");
        textBuilder.append("  Positive offset: ");
        if (posOffset != null) {
            processOffset(textBuilder, posOffset, version);
        } else {
            textBuilder.append("no offset\n");
        }
        textBuilder.append("  Negative offset: ");
        if (negOffset != null) {
            processOffset(textBuilder, negOffset, version);
        } else {
            textBuilder.append("no offset\n");
        }
    }

    /**
     * Process offset.
     * 
     * @param textBuilder
     *            the text builder
     * @param o
     *            the o
     * @param version
     *            the version
     */
    private static void processOffset(final StringBuilder textBuilder,
            final Offset o, final int version) {
        OffsetValue ov = new OffsetValue(o);
        if (version == 2) {
            textBuilder.append(ov).append(" [").append(ov.lowerBound())
                    .append("m - ").append(ov.upperBound()).append("m]\n");
        } else {
            textBuilder.append(ov).append(" [")
                    .append(ov.getRelativeOffsetLowerBound()).append(" - ")
                    .append(ov.getRelativeOffsetUpperBound()).append("]\n");
        }
    }

    /**
     * Handle geo coord location.
     * 
     * @param textBuilder
     *            the text builder
     * @param rawLocRef
     *            the raw loc ref
     * @param binData
     *            The binary data
     */
    private static void handleGeoCoordLocation(final StringBuilder textBuilder,
            final RawLocationReference rawLocRef, final RawBinaryData binData) {
        GeoCoordinates gCoord = rawLocRef.getGeoCoordinates();
        textBuilder.append(" Geo-Coordinate\n");
        textBuilder.append(" ==============\n");
        textBuilder.append(" Longitude: ")
                .append(Formatter.formatCoord(gCoord.getLongitudeDeg()))
                .append("\n");
        textBuilder.append(" Latitude:  ")
                .append(Formatter.formatCoord(gCoord.getLatitudeDeg()))
                .append("\n");
    }

    /**
     * Process loc type.
     *
     * @param textBuilder the text builder
     * @param locType the loc type
     */
    private static void processLocType(final StringBuilder textBuilder,
            final LocationType locType) {
        textBuilder.append(" Type\n");
        textBuilder.append(" ====\n");
        textBuilder.append("  Location type: ").append(locType.ordinal())
                .append(" [").append(locType.name()).append("]\n");
        textBuilder.append("\n\n");
    }

    /**
     * Handle line location.
     * 
     * @param textBuilder
     *            the text builder
     * @param binData
     *            The binary data
     * @param header
     *            the header
     */
    private static void handleLineLocation(final StringBuilder textBuilder,
            final RawBinaryData binData, final Header header) {
        FirstLRP first = binData.getBinaryFirstLRP();
        processFirstLRP(textBuilder, first);
        textBuilder.append("\n\n");
        List<IntermediateLRP> intermediates = binData.getBinaryIntermediates();
        double[] absCoordsLastIntermediate = processIntermediates(textBuilder, intermediates,
                CoordinateValue.get32BitRepresentation(first.getLon()),
                CoordinateValue.get32BitRepresentation(first.getLat()));
        LastLRP last = binData.getBinaryLastLRP();
        processLastLRP(textBuilder, last, absCoordsLastIntermediate[0], absCoordsLastIntermediate[1],
                intermediates.size() + 2);
        textBuilder.append("\n\n");
        processOffsets(textBuilder, binData.getBinaryPosOffset(),
                binData.getBinaryNegOffset(), header.getVer());
    }

    /**
     * Handle circle location.
     * 
     * @param textBuilder
     *            the text builder
     * @param rawLocRef
     *            the raw loc ref
     * @param binData
     *            The binary data
     */
    private static void handleCircleLocation(final StringBuilder textBuilder,
            final RawLocationReference rawLocRef, final RawBinaryData binData) {
        textBuilder.append(" Geo-Coordinate center point\n");
        textBuilder.append(" ===========================\n");
        GeoCoordinates cp = rawLocRef.getCenterPoint();
        textBuilder.append("  Longitude: ")
                .append(Formatter.formatCoord(cp.getLongitudeDeg()))
                .append("\n");
        textBuilder.append("  Latitude:  ")
                .append(Formatter.formatCoord(cp.getLatitudeDeg()))
                .append("\n");
        textBuilder.append("\n");

        textBuilder.append(" Radius\n");
        textBuilder.append(" ======\n");
        long radius = rawLocRef.getRadius();
        textBuilder.append("  Radius: ");
        if (radius >= THOUSAND_METERS) {
            textBuilder
                    .append(Formatter.formatLength(radius / THOUSAND_METERS));
            textBuilder.append(" Km");
        } else {
            textBuilder.append(Long.toString(radius));
            textBuilder.append(" Meter");
        }
    }

    /**
     * Handle rectangle location.
     * 
     * @param textBuilder
     *            the text builder
     * @param rawLocRef
     *            the raw loc ref
     * @param binData
     *            The binary data
     */
    private static void handleRectangleLocation(
            final StringBuilder textBuilder,
            final RawLocationReference rawLocRef, final RawBinaryData binData) {
        GeoCoordinates leftPoint = rawLocRef.getLowerLeftPoint();
        GeoCoordinates rightPoint = rawLocRef.getUpperRightPoint();
        processRectangleBounds(textBuilder, leftPoint, rightPoint);
    }

    /**
     * Process rectangle bounds.
     *
     * @param textBuilder the text builder
     * @param leftPoint the left point
     * @param rightPoint the right point
     */
    private static void processRectangleBounds(final StringBuilder textBuilder,
            final GeoCoordinates leftPoint, final GeoCoordinates rightPoint) {

        textBuilder.append(" Geo-Coordinate Left Point\n");
        textBuilder.append(" =========================\n");
        textBuilder.append("  Longitude: ")
                .append(Formatter.formatCoord(leftPoint.getLongitudeDeg()))
                .append("\n");
        textBuilder.append("  Latitude:  ")
                .append(Formatter.formatCoord(leftPoint.getLatitudeDeg()))
                .append("\n");

        textBuilder.append(" Geo-Coordinate Right Point\n");
        textBuilder.append(" ==========================\n");
        textBuilder.append("  Longitude: ")
                .append(Formatter.formatCoord(rightPoint.getLongitudeDeg()))
                .append("\n");
        textBuilder.append("  Latitude:  ")
                .append(Formatter.formatCoord(rightPoint.getLatitudeDeg()))
                .append("\n");
    }

    /**
     * Handle grid location.
     * 
     * @param textBuilder
     *            the text builder
     * @param rawLocRef
     *            the raw loc ref
     * @param binData
     *            The binary data
     */
    private static void handleGridLocation(final StringBuilder textBuilder,
            final RawLocationReference rawLocRef, final RawBinaryData binData) {
        GeoCoordinates leftPoint = rawLocRef.getLowerLeftPoint();
        GeoCoordinates rightPoint = rawLocRef.getUpperRightPoint();
        processRectangleBounds(textBuilder, leftPoint, rightPoint);

        int nColumns = rawLocRef.getNumberOfColumns();
        int nRows = rawLocRef.getNumberOfRows();
        textBuilder.append(" Number of Columns\n");
        textBuilder.append(" =================\n");
        textBuilder.append("  NColums: ").append(nColumns).append("\n");

        textBuilder.append(" Number of Rows\n");
        textBuilder.append(" =================\n");
        textBuilder.append("  NRows: ").append(nRows).append("\n");
    }

    /**
     * Handle polygon location.
     * 
     * @param textBuilder
     *            the text builder
     * @param rawLocRef
     *            the raw loc ref
     */
    private static void handlePolygonLocation(final StringBuilder textBuilder,
            final RawLocationReference rawLocRef) {
        int index = 1;
        for (GeoCoordinates gc : rawLocRef.getCornerPoints()) {
            textBuilder.append(" ").append(index)
                    .append(") Corner-Point Geo-Coordinate\n");
            textBuilder.append(" ==========================\n");
            textBuilder.append("  Longitude: ")
                    .append(Formatter.formatCoord(gc.getLongitudeDeg()))
                    .append("\n");
            textBuilder.append("  Latitude:  ")
                    .append(Formatter.formatCoord(gc.getLatitudeDeg()))
                    .append("\n");
            textBuilder.append("\n");
            index++;
        }
    }

    /**
     * Handle closed line location.
     * 
     * @param textBuilder
     *            the text builder
     * @param binData
     *            The binary data
     */
    private static void handleClosedLineLocation(
            final StringBuilder textBuilder, final RawBinaryData binData) {
        FirstLRP first = binData.getBinaryFirstLRP();
        processFirstLRP(textBuilder, first);
        textBuilder.append("\n");
        List<IntermediateLRP> intermediates = binData.getBinaryIntermediates();
        processIntermediates(textBuilder, intermediates,
                CoordinateValue.get32BitRepresentation(first.getLon()),
                CoordinateValue.get32BitRepresentation(first.getLat()));
        LastClosedLineLRP last = binData.getBinaryLastClosedLineLRP();
        processLastClosedLineLRP(textBuilder, intermediates.size() + 2, last);
    }

    /**
     * Process last closed line lrp.
     * 
     * @param textBuilder
     *            the text builder
     * @param id
     *            the id
     * @param last
     *            the last
     */
    private static void processLastClosedLineLRP(
            final StringBuilder textBuilder, final int id,
            final LastClosedLineLRP last) {
        textBuilder
                .append(" Last LRP (bases on the first LRP with the following delta)\n");
        textBuilder.append(" ========\n");
        textBuilder.append("  ID: ").append(id).append("\n");
        LastClosedLineValue lclv = new LastClosedLineValue(id, last);
        textBuilder.append("  Functional road class: ").append(lclv.getFrc())
                .append(" [").append(lclv.getFrc().name()).append("]\n");
        textBuilder.append("  Form of way: ").append(lclv.getFow())
                .append(" [").append(lclv.getFow().name()).append("]\n");
        textBuilder.append("  Bearing sector: ").append(lclv.getBear())
                .append(" [").append(lclv.getBear().lowerBound()).append(" - ")
                .append(lclv.getBear().upperBound()).append("]\n");

    }

    /**
     * Process intermediates.
     * 
     * @param textBuilder
     *            the text builder
     * @param intermediates
     *            the intermediates
     * @param firstLon
     *            the first lon
     * @param firstLat
     *            the first lat
     * @return the geo coordinates of the last intermediate
     */
    private static double[] processIntermediates(
            final StringBuilder textBuilder,
            final List<IntermediateLRP> intermediates, final double firstLon,
            final double firstLat) {
        int count = 2;
        double lon = firstLon;
        double lat = firstLat;
        for (IntermediateLRP iLRP : intermediates) {
            textBuilder.append(" Intermediate LRP\n");
            textBuilder.append(" ================\n");
            textBuilder.append("  ID: ").append(count).append("\n");
            IntermediateValue iv = new IntermediateValue(count, iLRP, lon, lat);
            textBuilder.append("  Longitude: ").append(iv.getLongitudeDeg())
                    .append("\n");
            textBuilder.append("  Latitude:  ").append(iv.getLatitudeDeg())
                    .append("\n");
            textBuilder.append("  Functional road class: ").append(iv.getFrc())
                    .append(" [").append(iv.getFrc().name()).append("]\n");
            textBuilder.append("  Form of way: ").append(iv.getFow())
                    .append(" [").append(iv.getFow().name()).append("]\n");
            textBuilder.append("  Bearing sector: ").append(iv.getBear())
                    .append(" [").append(iv.getBear().lowerBound())
                    .append(" - ").append(iv.getBear().upperBound())
                    .append("]\n");
            textBuilder
                    .append("  Lowest functional road class to next point: ")
                    .append(iv.getLfrcnp()).append(" [")
                    .append(iv.getLfrcnp().name()).append("]\n");
            textBuilder.append("  Distance to next point interval: ")
                    .append(iv.getDnp()).append(" [")
                    .append(iv.getDnp().lowerBound()).append("m - ")
                    .append(iv.getDnp().upperBound()).append("m]\n");
            count++;
            lon = Double.valueOf(iv.getLongitudeDeg());
            lat = Double.valueOf(iv.getLatitudeDeg());
            textBuilder.append("\n");
        }
        return new double[] {lon, lat};
    }

    /**
     * Process first lrp.
     * 
     * @param textBuilder
     *            the text builder
     * @param first
     *            the first
     */
    private static void processFirstLRP(final StringBuilder textBuilder,
            final FirstLRP first) {
        textBuilder.append(" First LRP\n");
        textBuilder.append(" =========\n");
        textBuilder.append("  ID: 1\n");
        FirstValue fv = new FirstValue(1, first);
        textBuilder.append("  Longitude: ").append(fv.getLongitudeDeg())
                .append("\n");
        textBuilder.append("  Latitude:  ").append(fv.getLatitudeDeg())
                .append("\n");
        textBuilder.append("  Functional road class: ").append(fv.getFrc())
                .append(" [").append(fv.getFrc().name()).append("]\n");
        textBuilder.append("  Form of way: ").append(fv.getFow()).append(" [")
                .append(fv.getFow().name()).append("]\n");
        textBuilder.append("  Bearing sector: ").append(fv.getBear())
                .append(" [").append(fv.getBear().lowerBound()).append(" - ")
                .append(fv.getBear().upperBound()).append("]\n");
        textBuilder.append("  Lowest functional road class to next point: ")
                .append(fv.getLfrcnp()).append(" [")
                .append(fv.getLfrcnp().name()).append("]\n");
        textBuilder.append("  Distance to next point interval: ")
                .append(fv.getDnp()).append(" [")
                .append(fv.getDnp().lowerBound()).append("m - ")
                .append(fv.getDnp().upperBound()).append("m]\n");
    }

    /**
     * Generates a human readable data string from the binary data.
     * 
     * @param binaryData
     *            the binary data
     * 
     * @return the human readable data string
     * 
     * @throws ViewerException
     *             if conversion failed
     */
    public static String generateHumanReadable(final byte[] binaryData)
            throws ViewerException {
        return generateHumanReadable("", binaryData);
    }

    /**
     * Generates a human readable data string from the base64 encoded binary
     * data.
     * 
     * @param base64Data
     *            the base64 encoded binary data
     * 
     * @return the human readable data string
     * 
     * @throws ViewerException
     *             if conversion failed
     */
    public static String generateHumanReadable(final String base64Data)
            throws ViewerException {
        return generateHumanReadable("", transformBase64(base64Data));
    }

    /**
     * Generates a human readable data string from the binary data. The id shall
     * identify the generated data if more than one binary data set shall be
     * converted.
     * 
     * @param binaryData
     *            the binary data
     * @param id
     *            the unique id
     * 
     * @return the human readable data string
     * 
     * @throws ViewerException
     *             if conversion failed
     */
    public static String generateHumanReadable(final String id,
            final byte[] binaryData) throws ViewerException {
        if (binaryData.length == 0) {
            throw new ViewerException("no binary data found!");
        }
        return createDataString(binaryData, id);
    }

    /**
     * Generates a human readable data string from the base64 encoded binary
     * data. The id shall identify the generated data if more than one binary
     * data set shall be converted.
     * 
     * @param base64Data
     *            the base64 encoded binary data
     * @param id
     *            the unique id
     * 
     * @return the human readable data string
     * 
     * @throws ViewerException
     *             if conversion failed
     */
    public static String generateHumanReadable(final String id,
            final String base64Data) throws ViewerException {
        return generateHumanReadable(id, transformBase64(base64Data));
    }

    /**
     * Gets the version string.
     * 
     * @return the version
     */
    static String getVersion() {
        StringBuilder sb = new StringBuilder();
        sb.append(MAJOR_VERSION).append(".").append(MINOR_VERSION).append(".")
                .append(PATCH_VERSION);
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getToolIdentifier() {
        return TOOL_IDENTIFIER;
    }

    /**
     * Writes the output KML if this was requested by the user.
     * 
     * @param outputFile
     *            The target file
     * @param locRef
     *            The location reference to write
     * @throws OpenLRToolkitException
     *             If an error occurred generating the KML
     */
    private static void writeKml(final File outputFile,
            final RawLocationReference locRef) throws OpenLRToolkitException {

        KmlOutput kmlHelper = new KmlOutput(outputFile);

        if (locRef.isValid()) {
            ContentProvider<?> locRefWriter = ContentProviderFactory
                    .createLocationReferenceContentProvider(locRef);

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
    protected BinaryViewerOptions getOptionsHandler() {
        return new BinaryViewerOptions(TOOL_IDENTIFIER, TOOL_DESCRIPTION);

    }

}
