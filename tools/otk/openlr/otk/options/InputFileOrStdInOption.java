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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


import openlr.otk.common.CommandLineParseException;

import openlr.otk.utils.StdInHandler;
import openlr.otk.utils.StdInHandler.READ_MODE;

import org.apache.commons.cli.Option;

/**
 * Interprets the value as a path to an input file. If the value is
 * <code>null</code> it awaits input from standard input.
 * 
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class InputFileOrStdInOption extends CommandLineOption {

    /**
     * The identifier of the data source standard input
     */
    private static final String IDENTIFIER_STDIN_SOURCE = "standard input";

    /** The data. */
    private byte[] data;

    /** The input file. */
    private File inputFile;

    /**
     * An instance of {@link InputFileOption} that handles the case of an
     * specified target file
     */
    private final InputFileOption delegatedPureFileOption;

    /**
     * Instantiates a new input file option.
     */
    public InputFileOrStdInOption() {
        super(
                "i",
                "input-file",
                true,
                "The path to the input file, if this option is missing the tool awaits input from standard input",
                false);
        Option option = getOption();
        delegatedPureFileOption = new InputFileOption(option.getOpt(),
                option.getLongOpt(), option.getDescription(), false);
    }

    /**
     * Resolve read mode.
     * 
     * @param addArgs
     *            the add args
     * @return the rEA d_ mode
     */
    private READ_MODE resolveReadMode(final Object... addArgs) {
        READ_MODE m = READ_MODE.CHAR;
        if (addArgs != null && addArgs.length > 0) {
            for (Object o : addArgs) {
                if (o instanceof READ_MODE) {
                    m = (READ_MODE) o;
                    break;
                }
            }
        }
        return m;
    }

    /**
     * Expects a valid file path or <code>null</code> as argument.
     * 
     * @param arg
     *            If <code>arg</code> is a non-empty string its value is
     *            processed as file path, if <code>arg</code> is null or empty
     *            string this handler awayts input from standard in.
     * @param addArgs
     *            ignored here
     * @throws CommandLineParseException
     *             If reading the file or standard input failed
     */
    @Override
    public final void parse(final String arg, final Object... addArgs)
            throws CommandLineParseException {
        if (arg == null || arg.isEmpty()) {
            try {
                READ_MODE mode = resolveReadMode(addArgs);
                byte[] bin = StdInHandler.readStdin(mode);
                data = InputFileOption.readData(new ByteArrayInputStream(bin));
            } catch (IOException e) {
                throw new CommandLineParseException(
                        "Cannot read input from std-in!", e);
            }
        } else {

            delegatedPureFileOption.parse(arg, addArgs);
            data = delegatedPureFileOption.getData();

            inputFile = new File(arg);
        }
    }

    /**
     * Delivers an input stream containing the input data. The methods returns a
     * new stream every time it is called.
     * 
     * @return An input stream delivering the input data.
     */
    public final InputStream getInputStream() {
        return new ByteArrayInputStream(data);
    }

    /**
     * Gets the input file. The returned value can be <code>null</code> if the
     * this option was not successful during {@link #parse(String, Object...)}
     * or if the data were read from standard input instead of a file.
     * 
     * @return the input file
     */
    public final File getInputFile() {
        return inputFile;
    }

    /**
     * Delivers an identifier/ name of the input source. In case of a file the
     * absolute path is returned, otherwise "standard input".
     * 
     * @return The identifier of the data source
     */
    public final String getInputSource() {

        if (getInputFile() != null) {
            return getInputFile().getAbsolutePath();
        } else {
            return IDENTIFIER_STDIN_SOURCE;
        }
    }
}
