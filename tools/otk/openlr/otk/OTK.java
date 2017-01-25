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
package openlr.otk;

import static openlr.otk.utils.IOUtils.LINE_SEPARATOR;

import java.io.PrintStream;
import java.util.Arrays;

import openlr.otk.common.CommandLineParseException;
import openlr.otk.common.OpenLRCommandLineTool;
import openlr.otk.common.OpenLRToolkitException;


/**
 * The main class of the OTK.
 */
public final class OTK {

    /** The Constant MAJOR_VERSION. */
    private static final int MAJOR_VERSION = 1;

    /** The Constant MINOR_VERSION. */
    private static final int MINOR_VERSION = 4;

    /** The Constant PATCH_VERSION. */
    private static final int PATCH_VERSION = 0;
    
    /**
     * The tool selector
     */
    private static final ToolSelector TOOL_SELECTOR = new ToolSelector();
    
    /**
     * The output target. So far just out.
     */
    private static final PrintStream OUT = System.out;
    
    /**
     * The error output target. So far just err.
     */    
    private static final PrintStream ERROR_OUT = System.err;


    /**
     * Utility class shall not be instantiated.
     */
    private OTK() {
        throw new UnsupportedOperationException();
    }

    /**
     * The main method.
     * 
     * @param args
     *            the arguments
     */
    public static void main(final String[] args) {

        try {

            boolean wasInfoRequest = handleInfoParameters(args);
            if (!wasInfoRequest) {

                String toolIdent = args[0];
                String[] toolArgs = Arrays.copyOfRange(args, 1, args.length);

                OpenLRCommandLineTool<?> tool = TOOL_SELECTOR.getToolForCommand(toolIdent);

                try {

                    tool.execute(toolArgs);

                } catch (CommandLineParseException e) {
                    ERROR_OUT.println("\nError processing the input parameters: "
                                    + e.getMessage());
                    tool.printHelp(OUT);
                    System.exit(2);
                }
            }
        } catch (OpenLRToolkitException e) {
            ERROR_OUT.println("\nError! " + e.getMessage());
            System.exit(2);
        }

    }

    /**
     * Usage.
     */
    private static void usage() {

        StringBuilder usage = new StringBuilder();
        usage.append("OpenLR Toolkit").append(LINE_SEPARATOR);
        usage.append(
                "The OpenLR toolkit (OTK) is a bundle of useful command line tools regarding OpenLR")
                .append(LINE_SEPARATOR).append(LINE_SEPARATOR);
        usage.append(
                "Usage: java -jar otk-<version>-with-dependencies.jar [--help] [--version] COMMAND [ARGS]")
                .append(LINE_SEPARATOR).append(LINE_SEPARATOR);
        usage.append("-h, --help")
                .append("\t")
                .append("Displayes this general help if it is the sole argument. Displays the help for a specific command when it is followed by the command.")
                .append(LINE_SEPARATOR);
        usage.append("-v, --version").append("\t")
                .append("Displayes the version of this OpenLR Toolkit.")
                .append(LINE_SEPARATOR).append(LINE_SEPARATOR);
        usage.append("Available commands are:").append(LINE_SEPARATOR);
        for (String command : TOOL_SELECTOR.getAllCommands()) {
            usage.append("\t").append(command).append(LINE_SEPARATOR);
        }

        OUT.println(usage);
    }


    /**
     * Handles the two info parameters "help" and "version" which just deliver
     * their output and terminates the application afterwards.
     * 
     * @param args
     *            The program arguments
     * @return Whether the arguments defined an info request
     * @throws OpenLRToolkitException
     *             If an error occurs processing the arguments
     */
    private static boolean handleInfoParameters(final String[] args)
            throws OpenLRToolkitException {

        boolean wasInfoRequest;
        if (args.length > 0) {
            // check if help is required
            if (args[0].equals("-?") || args[0].equals("-h")
                    || args[0].equals("--help") || args[0].equals("help")) {
                if (args.length > 1) {
                    OpenLRCommandLineTool<?> tool = TOOL_SELECTOR
                            .getToolForCommand(args[1]);

                    tool.printHelp(OUT);
                    wasInfoRequest = true;
                } else {

                    usage();
                    wasInfoRequest = true;
                }
            } else if (args[0].equals("-v") || args[0].equals("--version")) {
                OUT.println("otk version: " + getVersion());
                wasInfoRequest = true;
            } else {
                wasInfoRequest = false;
            }
        } else {
            usage();
            wasInfoRequest = true;
        }

        return wasInfoRequest;
    }

    /**
     * Gets the version string.
     * 
     * @return the version
     */
    public static String getVersion() {
        StringBuilder sb = new StringBuilder();
        sb.append(MAJOR_VERSION).append(".").append(MINOR_VERSION).append(".")
                .append(PATCH_VERSION);
        return sb.toString();
    }

}
