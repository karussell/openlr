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
package openlr.otk.binview.data;

/**
 * The ByteValue represents a single byte of the binary data stream.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class ByteValue {
	

	/** The Constant MAX_BYTE_VALUE. */
	private static final int MAX_BYTE_VALUE = 256;

	/** The Constant BYTE_MASK. */
	private static final int[] BYTE_MASK = {128, 64, 32, 16, 8, 4, 2, 1 };
	
	/** The byte value. */
	private final byte data;

	/**
	 * Instantiates a new byte value.
	 * 
	 * @param b the byte value
	 */
	public ByteValue(final byte b) {
		data = b;
	}

	/**
	 * Returns the hex representation of this byte.
	 * 
	 * @return the hex representation of this byte
	 */
	public final String hexValue() {
		int tempData = data;
		if (tempData < 0) {
			tempData += MAX_BYTE_VALUE;
		}
		String hexval = Integer.toHexString(tempData);
		StringBuilder sb = new StringBuilder();
		sb.append("0x");
		if (hexval.length() == 1) {
			sb.append("0");
		}
		sb.append(hexval);
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public final String toString() {
		return Byte.toString(data);
	}
	
	/**
	 * Returns the bit representation of this byte value.
	 * 
	 * @return the bit representation of this byte value
	 */
	public final String bitValue() {
		StringBuilder sb = new StringBuilder();
		for (int k = 0; k < BYTE_MASK.length; ++k) {
			if ((data & BYTE_MASK[k]) == BYTE_MASK[k]) {
				sb.append("1");
			} else {
				sb.append("0");
			}
		}
		return sb.toString();
	}

}
