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

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

import openlr.map.loader.OpenLRMapLoader;
import openlr.otk.common.CommandLineParseException;
import openlr.otk.options.Argument;
import openlr.otk.utils.MapLoadersFinder;

/**
 * This class implements an command line argument that extracts OpenLR map
 * loaders from the given value assumed to be a path to a JAR file.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
final class LoaderJarArgument extends Argument<List<OpenLRMapLoader>> {
    /**
     * The list of found loaders
     */
    private final List<OpenLRMapLoader> loaders = new ArrayList<OpenLRMapLoader>();
    
    /**
     * The given argument value
     */
    private String loaderLibraryArgument;


    /**
     * Creates a new instance
     * 
     * @param name
     *            The name of the argument
     * @param mandatory
     *            Whether it is mandatory
     * @param description
     *            The argument description for the usage
     */
    public LoaderJarArgument(final String name, final boolean mandatory,
            final String description) {
        super(name, mandatory, description);
    }

    /**
     * Handle the given value as a path to a JAR archive and tries to detect
     * OpenLR map loaders in it.
     * 
     * @param pathToJar
     *            The path to the JAR
     * @throws CommandLineParseException
     *             If the specified JAR is not found or if no loaders are found
     *             in it
     */
    @Override
    public void parse(final String pathToJar) throws CommandLineParseException {
        loaderLibraryArgument = pathToJar;
        loaders.clear();
        loaders.addAll(identifyMapLoaders(pathToJar));

    }

    /**
     * Delivers the list of loaders found in the prior parsed JAR (see
     * {@link #parse(String)}.
     * 
     * @return The list of found loaders, never {@code null} but maybe empty
     */
    @Override
    public List<OpenLRMapLoader> getData() {
        return Collections.unmodifiableList(loaders);
    }
    
    /**
     * Delivers the path to the JAR specified by the user or {@code null} if no
     * argument was provided (yet).
     * 
     * @return The path to the JAR or {@code null}
     */
    String getSpecifiedPathToJar() {
        return loaderLibraryArgument;
    }    

    /**
     * Identifies the specified loader either in a library the identifier points
     * to or in the class-path
     * 
     * @param indentifier
     *            The loader identifier as provided in the parameter string
     * @return The found loader
     * @throws CommandLineParseException
     *             If the specified JAR is not found or if no loaders are found
     *             in it
     */
    private List<OpenLRMapLoader> identifyMapLoaders(final String indentifier)
            throws CommandLineParseException {

        List<OpenLRMapLoader> relevantLoaders;
        final MapLoadersFinder loadersFinder = new MapLoadersFinder();

        try {
            relevantLoaders = loadersFinder.extractMapLoaders(indentifier);
        } catch (final FileNotFoundException e) {
            throw new CommandLineParseException(
                    "Map loader library file not found: " + indentifier, e);
        }

        if (relevantLoaders.isEmpty()) {
            throw new CommandLineParseException("No map loader found in "
                    + indentifier
                    + ". There is a service configuration according to "
                    + ServiceLoader.class.getName()
                    + " required in the loader library.");
        }

        return relevantLoaders;
    }

}
