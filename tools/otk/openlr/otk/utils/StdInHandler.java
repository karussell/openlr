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
package openlr.otk.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class StdInHandler.
 */
public final class StdInHandler {
	
	/**
	 * The Enum READ_MODE.
	 */
	public enum READ_MODE {
		
		/** The CHAR. */
		CHAR,
		
		/** The BYTE. */
		BYTE
	}
	
	/**
	 * Utility class shall not be instantiated.
	 */
	private StdInHandler() {
		throw new UnsupportedOperationException();
	}
	
    /**
     * Reads input data from standard in.
     *
     * @param mode the mode
     * @return The binary data of the the input read from standard in.
     * @throws IOException If an error occurred reading from standard in.
     */
    public static byte[] readStdin(final READ_MODE mode) throws IOException {
        byte[] data;

        if (mode == READ_MODE.CHAR) {
            String stdin = readCharsFromStdin();
            data = stdin.getBytes(IOUtils.SYSTEM_DEFAULT_CHARSET);
        } else {
            data = readBytesFromStdin();
        }

        return data;
    }

    /**
     * Reads character data from standard in.
     * 
     * @return The string value parsed from standard in.
     * @throws IOException
     *             If an error occurred.
     */
    private static String readCharsFromStdin() throws IOException {

        BufferedReader in = null;
        StringBuilder input = new StringBuilder();
        try {
            in = new BufferedReader(new InputStreamReader(System.in,
                    IOUtils.SYSTEM_DEFAULT_CHARSET));
            String line;
            while ((line = in.readLine()) != null) {
                input.append(line);
            }

        } finally {
            IOUtils.closeQuietly(in);
        }
        return input.toString();
    }

    /**
     * Reads pure binary data from standard in.
     * 
     * @return The data parsed from standard in.
     * @throws IOException
     *             If an error occurred.
     */
    private static byte[] readBytesFromStdin() throws IOException {
        int read;
        List<Byte> bytes = new ArrayList<Byte>();
        while ((read = System.in.read()) != -1) {
            bytes.add((byte) read);
        }

        byte[] result = new byte[bytes.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = bytes.get(i).byteValue();
        }

        return result;
    }

}
