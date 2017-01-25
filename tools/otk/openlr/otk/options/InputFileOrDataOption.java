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
import java.io.InputStream;

import openlr.otk.common.CommandLineParseException;
import openlr.otk.utils.IOUtils;




/**
 * This option interprets the provided value as an input file. If a file with
 * that name could not be found it interprets the value string directly as input
 * data.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class InputFileOrDataOption extends CommandLineOption {

    /**
     * A delegated instance of {@link InputFileOrStdInOption} that is used to do
     * the first processing try to intepret the value as file path.
     */
    private final InputFileOrStdInOption inputFileOptionProcessor;

    /** The data. */
    private byte[] data;

    /**
     * Instantiates a new input file option.
     */
    public InputFileOrDataOption() {
        super(
                "i",
                "input",
                true,
                "Input data (path to data file or data string). The application first checks if a file with the given name exists. "
                        + "If not it uses the given string itself as the input data. "
                        + "If this option is missing the tool blocks listening for data from standard input.",
                false);
        inputFileOptionProcessor = new InputFileOrStdInOption();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void parse(final String arg, final Object... addArgs)
            throws CommandLineParseException {

        if ((arg == null) || new File(arg).exists()) {
            inputFileOptionProcessor.parse(arg, addArgs);
            // data stays null, delegated processor could successfully read
            // something
            // delegated processor holds the data
        } else {
            // try to interpret the input string itself as the target data
            data = arg.trim().getBytes(IOUtils.SYSTEM_DEFAULT_CHARSET);
        }
    }

    /**
     * Delivers an input stream delivering the input data. The methods returns a
     * new stream every time it is called.
     * 
     * @return An input stream delivering the input data.
     */
    public final InputStream getInputStream() {
        if (data != null) {
            return new ByteArrayInputStream(data);
        } else {
            return inputFileOptionProcessor.getInputStream();
        }
    }

    /**
     * Delivers an identifier/ name of the input source. In case of a data taken
     * directly from the command line the data string itself is returned,
     * otherwise the value of {@link InputFileOrStdInOption#getInputSource()}.
     * 
     * @return The identifier of the data source
     */
    public final String getInputSource() {

        if (data != null) {
            return new String(data, IOUtils.SYSTEM_DEFAULT_CHARSET);
        } else {
            return inputFileOptionProcessor.getInputSource();
        }
    }

}
