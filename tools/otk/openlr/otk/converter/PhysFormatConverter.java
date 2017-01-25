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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import openlr.LocationReference;
import openlr.OpenLRProcessingException;
import openlr.PhysicalDecoder;
import openlr.PhysicalEncoder;
import openlr.PhysicalFormatException;
import openlr.binary.ByteArray;
import openlr.binary.impl.LocationReferenceBinaryImpl;
import openlr.datex2.Datex2Location;
import openlr.datex2.impl.LocationReferenceImpl;
import openlr.otk.common.CommandLineParseException;
import openlr.otk.common.Format;
import openlr.otk.common.OpenLRCommandLineTool;
import openlr.otk.common.OpenLRToolkitException;
import openlr.otk.converter.PhysFormatConverterException.ConverterErrorCode;
import openlr.otk.utils.DataResolver;
import openlr.otk.utils.DataResolverException;
import openlr.otk.utils.IOUtils;
import openlr.rawLocRef.RawLocationReference;
import openlr.xml.generated.OpenLR;
import openlr.xml.impl.LocationReferenceXmlImpl;

/**
 * The class PhysFormatConverter converts OpenLR physical formats.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public final class PhysFormatConverter extends OpenLRCommandLineTool<PhysFormatConvOptions> {

	/** The Constant TOOL_IDENTIFIER. */
	public static final String TOOL_IDENTIFIER = "convert";
	
    /**
     * The tool description
     */
    private static final String TOOL_DESCRIPTION = "Converts location references between the physical data formats. "
            + "Please note that conversion from non-binary into the binary format incorporates a compression of the parameters. "
            + "There will be a loss in accuracy.";


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
    public void executeImpl(final PhysFormatConvOptions clData)
            throws OpenLRToolkitException, CommandLineParseException {

		PhysicalEncoder enc = null;
		PhysicalDecoder dec = null;
		LocationReference data = null;

		Format inputFormat = clData.getInputFormat();
		InputStream inStream = clData.getInputStream();
		dec = inputFormat.getDecoder();
		try {
			switch (inputFormat) {
			case BINARY:
				data = new LocationReferenceBinaryImpl("",
						DataResolver.resolveBinaryData(inStream, false));
				break;
			case BINARY64:
				data = new LocationReferenceBinaryImpl("",
						DataResolver.resolveBinaryData(inStream, true));
				break;
			case XML:
				OpenLR xmlData = DataResolver.resolveXmlData(inStream);
				data = new LocationReferenceXmlImpl("", xmlData, 1);
				break;
			case DATEX2:
				Datex2Location d = DataResolver.resolveDatex2Data(inStream);
				data = new LocationReferenceImpl("", d, 1);
				break;
			default:
                throw new OpenLRToolkitException("invalid type of input data: "
                        + inputFormat);
            }
        } catch (DataResolverException e) {
            throwConversionException(e);
        } catch (PhysicalFormatException e) {
            throw new OpenLRToolkitException("Conversion failed ! Error code: "
                    + e.getErrorCode(), e);
        } finally {
            IOUtils.closeQuietly(inStream);
        }

		Format outputFormat = clData.getOutputFormat();
		enc = outputFormat.getEncoder();

		// do conversion
        OutputStream outStream = null;
        try {
            outStream = clData.getOutputStream();
			doConversion(inputFormat, outputFormat, enc, dec, data, outStream);
            outStream.close();
		} catch (PhysFormatConverterException e1) {
			throwConversionException(e1);
		} catch (OpenLRProcessingException e1) {
			throwConversionException(e1);
        } catch (IOException e) {
            throw new OpenLRToolkitException(
                    "Error writing to output target, message: "
                            + e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(outStream);
        }
    }

	/**
	 * Do conversion.
	 * 
	 * @param inputFormat
	 *            the input format
	 * @param outputFormat
	 *            the output format
	 * @param enc
	 *            the encoder
	 * @param dec
	 *            the decoder
	 * @param data
	 *            the location reference data
	 * @param os
	 *            the output stream
	 * @throws OpenLRProcessingException
	 *             the open lr processing exception
	 */
	private static void doConversion(final Format inputFormat,
			final Format outputFormat, final PhysicalEncoder enc,
			final PhysicalDecoder dec, final LocationReference data,
			final OutputStream os) throws OpenLRProcessingException {
		// binary conversions are treated separately
		if (inputFormat == Format.BINARY && outputFormat == Format.BINARY64) {
			String b64 = ((ByteArray) data.getLocationReferenceData())
					.getBase64Data();
            OutputStreamWriter fw = new OutputStreamWriter(os,
                    IOUtils.SYSTEM_DEFAULT_CHARSET);
			try {
				fw.write(b64);
				fw.flush();
			} catch (IOException e) {
				throw new PhysFormatConverterException(
						ConverterErrorCode.IO_ERROR, e);
			}
		} else if (inputFormat == Format.BINARY64
				&& outputFormat == Format.BINARY) {
			byte[] bytes = ((ByteArray) data.getLocationReferenceData())
					.getData();
			try {
				os.write(bytes);
				os.flush();
			} catch (IOException e) {
				throw new PhysFormatConverterException(
						ConverterErrorCode.IO_ERROR, e);
			}
		} else {
			// no binary to binary conversion
			try {
				LocationReference locRef = convert(data, enc, dec);
				if (outputFormat == Format.BINARY64) {
					String b64 = ((ByteArray) locRef.getLocationReferenceData())
							.getBase64Data();
                    OutputStreamWriter fw = new OutputStreamWriter(os,
                            IOUtils.SYSTEM_DEFAULT_CHARSET);
					fw.write(b64);
					fw.flush();
				} else {
					locRef.toStream(os);
				}
			} catch (IOException e) {
				throw new PhysFormatConverterException(
						ConverterErrorCode.IO_ERROR, e);
			}
		}
	}

    /**
     * Throws an {@link OpenLRToolkitException} build from the given
     * {@link OpenLRProcessingException}
     * 
     * @param ex
     *            the exception
     * @throws OpenLRToolkitException
     *             The exception containing the data of the given
     *             {@link OpenLRProcessingException}.
     */
    private static void throwConversionException(
            final OpenLRProcessingException ex) throws OpenLRToolkitException {

        StringBuffer sb = new StringBuffer();
        sb.append("Conversion failed!  Error code: ")
                .append(ex.getErrorCode().getName())
                .append(",  explanation:  ")
                .append(ex.getErrorCode().getExplanation());

        String message = ex.getLocalizedMessage();
        if (message != null) {
            sb.append(", ").append(message);
        }

        throw new OpenLRToolkitException(sb.toString(), ex);
    }

	/**
	 * Convert one location reference data into another.
	 * 
	 * @param data
	 *            the data
	 * @param encoder
	 *            the encoder
	 * @param decoder
	 *            the decoder
	 * @return the location reference
	 * @throws PhysFormatConverterException
	 *             the phys format converter exception
	 */
	public static LocationReference convert(final LocationReference data,
			final PhysicalEncoder encoder, final PhysicalDecoder decoder)
			throws PhysFormatConverterException {
		if (decoder.getDataClass() != data.getLocationReferenceData().getClass()) {
			throw new PhysFormatConverterException(
					ConverterErrorCode.DATA_DOES_NOT_MATCH_DECODER);
		}
		RawLocationReference rawLocRef = null;
		try {
			rawLocRef = decoder.decodeData(data);
		} catch (PhysicalFormatException e) {
			throw new PhysFormatConverterException(
					ConverterErrorCode.CANNOT_DECODE_DATA, e);
		}
		if (!rawLocRef.isValid()) {
			throw new PhysFormatConverterException(
					ConverterErrorCode.CANNOT_DECODE_DATA, rawLocRef
							.getReturnCode().toString());
		}
		LocationReference locRef = encoder.encodeData(rawLocRef);

		if (!locRef.isValid()) {
			throw new PhysFormatConverterException(
					ConverterErrorCode.CANNOT_ENCODE_DATA, locRef
							.getReturnCode().toString());
		}
		return locRef;
	}

    @Override
    public String getToolIdentifier() {
        return TOOL_IDENTIFIER;
    } 
    
    /**
     * {@inheritDoc}
     */
    @Override
    public PhysFormatConvOptions getOptionsHandler() {
        return new PhysFormatConvOptions(TOOL_IDENTIFIER, TOOL_DESCRIPTION);
    }  
}
