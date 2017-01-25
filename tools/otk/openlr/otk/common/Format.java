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
package openlr.otk.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import openlr.PhysicalDecoder;
import openlr.PhysicalEncoder;

/**
 * Defines supported formats for input and output.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * 
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public enum Format {

	/** The DATE x2. */
	DATEX2("datex2", new openlr.datex2.OpenLRDatex2Encoder(),
			new openlr.datex2.OpenLRDatex2Decoder()),

	/** The XML. */
	XML("xml", new openlr.xml.OpenLRXMLEncoder(),
			new openlr.xml.OpenLRXMLDecoder()),

	/** The BINARY. */
	BINARY("binary", new openlr.binary.OpenLRBinaryEncoder(),
			new openlr.binary.OpenLRBinaryDecoder()),

	/**
	 * Base64-encoded OpenLR physical format.
	 */
	BINARY64("binary64", new openlr.binary.OpenLRBinaryEncoder(),
			new openlr.binary.OpenLRBinaryDecoder());

	/** The identifier. */
	private final String identifier;

	/** The encoder. */
	private final PhysicalEncoder encoder;

	/** The decoder. */
	private final PhysicalDecoder decoder;

	/**
	 * Instantiates a new format.
	 * 
	 * @param ident
	 *            the ident
	 * @param enc
	 *            the enc
	 * @param dec
	 *            the dec
	 */
	private Format(final String ident, final PhysicalEncoder enc,
			final PhysicalDecoder dec) {
		identifier = ident;
		encoder = enc;
		decoder = dec;
	}

	/** All types of formats. */
	private static final List<Format> VALUES = Collections
			.unmodifiableList(Arrays.asList(values()));

	/**
	 * Gets all types of formats.
	 * 
	 * @return the location types
	 */
	public static List<Format> getFormats() {
		return VALUES;
	}

	/**
	 * Gets the identifier.
	 * 
	 * @return the identifier
	 */
	public final String getIdentifier() {
		return identifier;
	}

	/**
	 * Gets the encoder.
	 * 
	 * @return the encoder
	 */
	public final PhysicalEncoder getEncoder() {
		return encoder;
	}

	/**
	 * Gets the decoder.
	 * 
	 * @return the decoder
	 */
	public final PhysicalDecoder getDecoder() {
		return decoder;
	}

	/**
     * Checks validity of an OpenLR format option.
     * 
     * @param formatString
     *            The format string.
     * @return The {@link Format} determined from the <code>formatString</code>.
     * @throws CommandLineParseException
     *             If the given string is does not match an OpenLR data format
     *             identifier
     */
	public static Format parseString(final String formatString) throws CommandLineParseException {
		Format format = null;
		if (formatString != null) {
			for (Format current : Format.getFormats()) {
				if (current.name().equalsIgnoreCase(formatString)) {
					format = current;
					break;
				}
			}
		}
        if (format == null) {
            throw new CommandLineParseException(
                    "Unknown data format specified: " + formatString);
        }
        return format;
    }

	/**
	 * Available format string.
	 *
	 * @return the string
	 */
	public static String availableFormatString() {
		StringBuilder sb = new StringBuilder();		
        sb.append(" valid format identifiers:\n");
        sb.append("   binary    --  binary format\n");
        sb.append("   binary64  --  binary and Base64 encoded\n");
        sb.append("   xml       --  XML data\n");
        sb.append("   datex2    --  Datex2 data\n");
        return sb.toString();
	}
}
