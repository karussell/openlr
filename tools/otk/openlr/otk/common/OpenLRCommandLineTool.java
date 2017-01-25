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
package openlr.otk.common;

import java.io.OutputStream;

import openlr.otk.options.CommandLineData;

/**
 * This interface defines the general interface of a tool inside the OpenLR
 * toolkit
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @param <T>
 *            The type of options handler this tool implementation provides, see
 *            {@link #getOptionsHandler()}.
 * @author TomTom International B.V.
 */
public abstract class OpenLRCommandLineTool<T extends CommandLineData> {

    /**
     * Delivers the tool identifier, a single word that defines the base command
     * of the tool
     * 
     * @return The identifier or the tool
     */
    public abstract String getToolIdentifier();
    
    /**
     * The output target the tool should write to
     */
    private OutputStream outStream = System.out;
    
    /**
     * Executes the tool with the given command line arguments.
     * 
     * @param args
     *            The effective command line arguments for this tool, assumed to
     *            be without the OTK base command and the tool identifier.
     * @throws OpenLRToolkitException
     *             If an error occurred running the actions of the tool
     * @throws CommandLineParseException
     *             In case of an error processing the input parameters
     */
    public final void execute(final String[] args)
            throws CommandLineParseException, OpenLRToolkitException {
        T optHandler = getOptionsHandler();
        optHandler.parse(args);
        executeImpl(optHandler);
    }

    /**
     * Executes the tool.
     * 
     * @param optionsHandler
     *            The options handler retrieved before the tool run via
     *            {@link #getOptionsHandler()}. In this stage it is fully
     *            initialized with the command line arguments.
     * @throws OpenLRToolkitException
     *             If an error occurred running the actions of the tool
     * @throws CommandLineParseException
     *             In case of an error processing the input parameters
     */
    protected abstract void executeImpl(T optionsHandler) throws OpenLRToolkitException,
            CommandLineParseException;
    
    /**
     * Prints out the usage information of the tool to the specified target
     * stream
     * 
     * @param target
     *            The target output stream
     */
    public final void printHelp(final OutputStream target) {
        getOptionsHandler().usage(target);
    }
    
    /**
     * This method shall return an option handler in form of an implementation
     * of {@link CommandLineData}. This option processor will be called for
     * initialization prior to the execution of the command and then passed to
     * {@link #executeImpl(CommandLineData)}.
     * 
     * @return A new instance of the options handler
     */
    protected abstract T getOptionsHandler();
    
    /**
     * Sets the target output stream this tool should write to.
     * 
     * @param out
     *            The output stream
     */
    public final void setOutput(final OutputStream out) {
        outStream = out;
    }

    /**
     * Delivers the output stream this tool should write to.
     * 
     * @return The output stream
     */
    protected final OutputStream getOutputStream() {
        return outStream;
    }
}
