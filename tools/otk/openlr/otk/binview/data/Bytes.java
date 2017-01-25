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

import java.util.ArrayList;
import java.util.List;

/**
 * Bytes holds the binary representation as an ordered list of bytes.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class Bytes {

	
	/** The byte list. */
	private final List<ByteValue> byteValues = new ArrayList<ByteValue>();

	/**
	 * Instantiates a new byte list.
	 * 
	 * @param dataValue the data values
	 */
	public Bytes(final byte[] dataValue) {
		for (byte b : dataValue) {
			byteValues.add(new ByteValue(b));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (ByteValue bv : byteValues) {
			sb.append(bv).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Gets the byte at index.
	 * 
	 * @param index the index
	 * 
	 * @return the byte at index
	 */
	public final ByteValue getByte(final int index) {
		if (index >= 0 && index < byteValues.size()) {
			return byteValues.get(index);
		} else {
			return null;
		}
	}

	/**
	 * Gets the ordered list of bytes.
	 * 
	 * @return the ordered list of bytes
	 */
	public final List<ByteValue> getBytes() {
		return byteValues;
	}

}
