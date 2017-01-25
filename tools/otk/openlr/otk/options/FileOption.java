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

import openlr.otk.common.CommandLineParseException;


/**
 * This option implements the processing of an file option. It delivers the file
 * object according to a specified path
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class FileOption extends CommandLineOption {

    /** The output file. */
    private File outputFile;

    /**
     * A flag that determines whether this option processor should fail if the
     * specified target file already exists.
     */
    private final boolean failIfFileExists;

    /**
     * Creates file option that fails if the specified target file already
     * exists.
     * 
     * @param shortOpt
     *            short representation of the option
     * @param longOpt
     *            the long representation of the option
     * @param desc
     *            describes the function of the option
     * @param mand
     *            A flag indication whether this option is mandatory
     */
    public FileOption(final String shortOpt, final String longOpt,
            final String desc, final boolean mand) {
        super(shortOpt, longOpt, true, desc, mand);
        failIfFileExists = false;
    }

    /**
     * Creates the file option instance
     * 
     * @param shortOpt
     *            short representation of the option
     * @param longOpt
     *            the long representation of the option
     * @param desc
     *            describes the function of the option
     * @param mand
     *            A flag indication whether this option is mandatory
     * @param failIfFileAlreadyExists
     *            A flag that determines whether this option processor should
     *            fail during {@link #parse(String, Object...)} if the specified
     *            target file already exists.
     */
    public FileOption(final String shortOpt, final String longOpt,
            final String desc, final boolean mand,
            final boolean failIfFileAlreadyExists) {
        super(shortOpt, longOpt, true, desc, mand);
        failIfFileExists = failIfFileAlreadyExists;
    }

    /**
     * Tries to detect a file interpreting the given argument as a path.
     * 
     * @param path
     *            The path to the file
     * @param addArgs
     *            Is ignored
     * @throws CommandLineParseException
     *             If the file does not exist and constructor parameter
     *             failIfNotExists was set, see:
     *             {@link #FileOption(String, String, String, boolean, boolean)}
     */
    @Override
    public final void parse(final String path, final Object... addArgs)
            throws CommandLineParseException {
        if (path != null) {
            File file = new File(path);
            if (file.exists() && failIfFileExists) {
                throw new CommandLineParseException("File already exists: "
                        + outputFile.getAbsolutePath());
            }

            outputFile = file;
        }
    }

    /**
     * Delivers the file object.
     * 
     * @return A the file object.
     */
    public final File getFile() {
        return outputFile;
    }

}
