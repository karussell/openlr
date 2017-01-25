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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import openlr.otk.common.CommandLineParseException;

import org.apache.commons.cli.Option;



/**
 * This option implements the processing of an output file option. It provides
 * an output stream to the specified file. If an empty argument is provided it
 * delivers a reference to {@link System#out}.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class OutputFileOrStdOutOption extends CommandLineOption {
    
    /**
     * The identifier of the data target standard output
     */
    private static final String IDENTIFIER_STDOUT_TARGET = "standard output";


    /** The input file. */
    private File outputFile;

    /**
     * An instance of {@link FileOption} that handles the case of an
     * specified target file
     */
    private final FileOption delegatedPureFileOption;

    /**
     * Instantiates a new output file option.
     * @param failIfFileAlreadyExists
     *            A flag that determines whether this option processor should
     *            fail during {@link #parse(String, Object...)} if the specified
     *            target file already exists.
     */
    public OutputFileOrStdOutOption(final boolean failIfFileAlreadyExists) {
        super(
                "o",
                "output-file",
                true,
                "path to the the output file [if missing output is written to standard out]",
                false);
        Option option = getOption();
        delegatedPureFileOption = new FileOption(option.getOpt(),
                option.getLongOpt(), option.getDescription(), false,
                failIfFileAlreadyExists);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void parse(final String arg, final Object... addArgs)
            throws CommandLineParseException {
        if (arg != null) {
            delegatedPureFileOption.parse(arg, addArgs);
            outputFile = delegatedPureFileOption.getFile();
        } 
    }

    /**
     * Delivers a setup output stream to write the output to. The output stream
     * and thus the target file is first created when this method is called!
     * This enables the caller to wait for the file creation until all prior
     * steps in the toll processing were successful. In case of an error the
     * output file maybe should not be created.
     * 
     * @return A setup output stream to write the output to.
     * @throws FileNotFoundException
     *             If there is a problem writing to the file
     */
    public final OutputStream getOutputStream()
            throws FileNotFoundException {
        OutputStream os;
        if (outputFile != null) {
            os = new FileOutputStream(outputFile);
        } else {
            os = System.out;
        }

        return os;
    }

    /**
     * Delivers an identifier/ name of the output target. In case of a file the
     * absolute path is returned, otherwise "standard output".
     * 
     * @return The identifier of the data target
     */
    public final String getOutputTarget() {

        if (outputFile != null) {
            return outputFile.getAbsolutePath();
        } else {
            return IDENTIFIER_STDOUT_TARGET;
        }
    }
}
