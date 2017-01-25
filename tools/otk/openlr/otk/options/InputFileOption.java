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
package openlr.otk.options;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import openlr.otk.common.CommandLineParseException;

/**
 * This class implements general methods for input file options.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class InputFileOption extends CommandLineOption {

    /** The read buffer size */
    private static final int BUFFER_SIZE = 1024;

    /** The data. */
    private byte[] data;

    /**
     * @param shortOpt
     *            short representation of the option
     * @param longOpt
     *            the long representation of the option
     * @param desc
     *            describes the function of the option
     * @param mand
     *            A flag indication whether this option is mandatory
     */
    public InputFileOption(final String shortOpt, final String longOpt,
            final String desc, final boolean mand) {
        super(shortOpt, longOpt, true, desc, mand);
    }

    /**
     * Reads the data from the file specified by the given path into the
     * internal stream holding the content.
     * 
     * @param path
     *            The path to the specified input file
     * @return <code>true</code> if the given path could be resolved to a valid
     *         file and the data could be read successfully, <code>false</code>
     *         is returned if the file was not found.
     * @throws CommandLineParseException
     *             If an error occurred reading the file content
     */
    protected final byte[] readFileData(final String path)
            throws CommandLineParseException {

        File sf = new File(path);

        if (sf.exists() && sf.length() == 0) {
            throw new CommandLineParseException("input file "
                    + sf.getAbsolutePath() + " is empty!");
        }
        try {
            return readData(new FileInputStream(sf));

        } catch (FileNotFoundException e) {
            throw new CommandLineParseException("Specified input file "
                    + sf.getAbsolutePath() + " not found.", e);

        } catch (IOException e) {
            throw new CommandLineParseException("Cannot read input from file!",
                    e);

        }
    }

    /**
     * Prepare stream.
     * 
     * @param s
     *            the s
     * @return the byte data provided by the given stream
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    protected static final byte[] readData(final InputStream s)
            throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int len = 0;
        while ((len = s.read(buffer)) > 0) {
            baos.write(buffer, 0, len);
        }
        baos.flush();
        return baos.toByteArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void parse(final String arg, final Object... addArgs)
            throws CommandLineParseException {
        if (arg != null) {

            data = readFileData(arg);

        } else {
            if (getOption().isRequired()) {
                throw new CommandLineParseException(
                        "Missing argument for option " + getShortIdentifier());
            }
        }
    }

    /**
     * Delivers the read data of the input file
     * 
     * @return The read data
     */
    public final byte[] getData() {
        if (data != null) {
            return Arrays.copyOf(data, data.length);
        } else {
            return null;
        }
    }
}
