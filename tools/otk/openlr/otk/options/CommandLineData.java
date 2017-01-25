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

import java.io.OutputStream;
import java.util.List;

import openlr.otk.common.CommandLineParseException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

/**
 * This class processes the command line arguments and initializes the input
 * data.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public abstract class CommandLineData {

    /** The logger. */
    private static final Logger LOG = Logger.getLogger(CommandLineData.class);

    /**
     * The identifier of the related OTK tool
     */
    private final String toolID;

    /**
     * A description of the related OTK tool used for building the usage
     * information to the user
     */
    private final String toolDescription;

    /**
     * Creates an instance of this command line data processor.
     * 
     * @param toolIdentifier
     *            The identifier of the related OTK tool
     * @param description
     *            A description of the related OTK tool used for building the
     *            usage information to the user
     */
    public CommandLineData(final String toolIdentifier, final String description) {
        toolID = toolIdentifier;
        toolDescription = description;
    }

    /**
     * Parses the command line and executes the evaluation of the parameters. 
     * 
     * @param args
     *            The raw command line args for the related tool
     * @throws CommandLineParseException
     *             If an error occurred processing the input options
     */
    public final void parse(final String[] args) throws CommandLineParseException {
        CommandLine cmdLine = parseCommandLine(args);
        checkRequiredOptions(cmdLine);

        processInput(cmdLine);
    }
    
    /**
     * This method has to be implemented by concrete sub-classes. It gets the
     * prepared command line that was build from the raw command line options
     * and additional arguments. The implementation class should evaluate the
     * data from the parameters and decided whether everything is specified
     * correctly.
     * 
     * @param cmdLine
     *            The command line data container
     * @throws CommandLineParseException
     *             If an error in the given command line options is detected.
     */
    protected abstract void processInput(final CommandLine cmdLine)
            throws CommandLineParseException;
    
    /**
     * Check required options.
     * 
     * @param cmdLine
     *            the cmd line
     * @throws CommandLineParseException
     *             the parse exception
     */
    private void checkRequiredOptions(final CommandLine cmdLine) throws CommandLineParseException {

        for (Option opt : cmdLine.getOptions()) {
            if (opt.isRequired() && !cmdLine.hasOption(opt.getOpt())) {
                throw new CommandLineParseException("Missing mandatory parameter: "
                        + opt.getOpt());
            }
        }
    }

    /**
     * Parses the command line arguments and creates an {@link CommandLine}
     * object of it.
     * 
     * @param args
     *            The arguments.
     * @return The {@link CommandLine} object.
     * @throws CommandLineParseException
     *             If an error occurs.
     */
    private CommandLine parseCommandLine(final String[] args) throws CommandLineParseException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("parse command line options");
        }
        CommandLineParser parser = new PosixParser();
        Options options = buildCliOptions(getOptions());
        CommandLine cmdLine;
        try {
            cmdLine = parser.parse(options, args);
        } catch (ParseException e) {
            throw new CommandLineParseException(e.getMessage(), e);
        }

        return cmdLine;
    }
  
    /**
     * Delivers the options of the tool that relates to the concrete
     * implementation of this class
     * 
     * @return The list of options of the related tool
     */
    protected abstract List<CommandLineOption> getOptions();
    
    /**
     * Delivers names of additional arguments the tool requires that are not
     * considered by an option. This list is used to print the tool usage.
     * 
     * @return The name of additional arguments, shall never be {@code null}
     */
    protected abstract List<Argument<?>> getArguments();
   
    /**
     * Builds an {@link org.apache.commons.cli.Options} object from the 
     * OTK options list of the tool
     * 
     * @param otkOptions
     *            the OTK options
     * @return the command line options
     */
    private Options buildCliOptions(final List<CommandLineOption> otkOptions) {

        Options options = new Options();
        for (CommandLineOption opt : otkOptions) {
            options.addOption(opt.getOption());
        }

        return options;
    }
    
    /**
     * Prints the command line syntax.
     * @param target The target stream to write to
     * 
     */
    public final void usage(final OutputStream target) {

        UsageBuilder.usage(target, buildCliOptions(getOptions()),
                getArguments(), toolID, toolDescription);
    }

}
