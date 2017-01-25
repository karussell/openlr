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
 */
/**
 *  Copyright (C) 2009-2012 TomTom International B.V.
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
package openlr.otk.loaderinfo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import openlr.map.loader.OpenLRMapLoader;
import openlr.otk.common.CommandLineParseException;
import openlr.otk.options.Argument;
import openlr.otk.options.CommandLineData;
import openlr.otk.options.CommandLineOption;

import org.apache.commons.cli.CommandLine;

/**
 * This class handles the possible command line argument for the
 * {@link LoaderInfo} tool.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
class LoaderInfoOptions extends CommandLineData {


    /**
     * The options argument specifying the path to the loader JAR
     */
    private final LoaderJarArgument loaderJarArg = new LoaderJarArgument(
            "loader-jar",
            false,
            "The path to the library containing an OpenLR map loader implementation. This has to be a JAR file that provides an implementation of interface OpenLRMapLoader published via Java ServiceLoader interface.");

    /**
     * Creates a new instance
     * 
     * @param toolIdentifier
     *            The identifier of the related OTK tool
     * @param description
     *            A description of the related OTK tool used for building the
     *            usage information to the user
     */
    public LoaderInfoOptions(final String toolIdentifier,
            final String description) {
        super(toolIdentifier, description);
    }

    @Override
    protected final void processInput(final CommandLine args)
            throws CommandLineParseException {

        if (args.getArgs().length > 0) {
            loaderJarArg.parse(args.getArgs()[0]);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<CommandLineOption> getOptions() {
        return Collections.<CommandLineOption> emptyList();
    }

    /**
     * Delivers all the found map loaders in the specified JAR, returns an empty
     * list of none was found.
     * 
     * @return The list of found loaders
     */
    final List<OpenLRMapLoader> getLoaders() {
        return loaderJarArg.getData();
    }

    /**
     * Delivers the path to the JAR specified by the user or {@code null} if no
     * argument was provided.
     * 
     * @return The path to the JAR or {@code null}
     */
    String getSpecifiedPathToJar() {
        return loaderJarArg.getSpecifiedPathToJar();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected List<Argument<?>> getArguments() {
        return Arrays.<Argument<?>>asList(loaderJarArg);
    }
}
