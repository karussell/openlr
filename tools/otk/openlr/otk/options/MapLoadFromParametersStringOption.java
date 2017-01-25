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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.StringTokenizer;

import openlr.map.MapDatabase;
import openlr.map.loader.MapLoadParameter;
import openlr.map.loader.OpenLRMapLoader;
import openlr.map.loader.OpenLRMapLoaderException;
import openlr.otk.common.CommandLineParseException;
import openlr.otk.utils.MapLoadersFinder;


/**
 * This class implements the functionality to load a map from a specific map
 * loader parameter string.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class MapLoadFromParametersStringOption extends CommandLineOption {

    /**
     * The escape character
     */
    private static final char ESCAPE_CHARACTER = '\\';

    /**
     * The map parameter key-value delimiter
     */
    private static final char VALUE_DELIMITER = ',';
    /**
     * The instantiated map database
     */
    private MapDatabase mapDB;

    /**
     * @param shortOpt
     *            short representation of the option
     * @param longOpt
     *            the long representation of the option
     * @param mand
     *            A flag indication whether this option is mandatory
     */
    public MapLoadFromParametersStringOption(final String shortOpt,
            final String longOpt, final boolean mand) {
        super(shortOpt, longOpt, true, DESCRIPTION, mand);
    }

    /**
     * A horizontal line used in the tool description
     */
    private static final String HORIZONTAL_LINE = "--------------------------------";
    
    /**
     * The description of the format of the parameter string.
     */
    private static final String DESCRIPTION = new StringBuilder(2500)
            .append("Map access string. This parameter expects a formatted string that consists of ")
            .append("the map loader identifier and a list of the loader's parameter values. All the parts are separated by commas:\n")
            .append(HORIZONTAL_LINE)
            .append("\n<loader ID> [,<loader param ID>,<value>]*\n")
            .append(HORIZONTAL_LINE)
            .append("\n")
            .append("The loader identifier either contains the path to an OpenLR map loader JAR file or the name of a map loader ")
            .append("instance in the class path. In the example below the map loader is expected to reside in file ")
            .append("\"tt-sqlite-with-dependencies.jar\" in the current working directory. ")
            .append("Accessing the loader by specification of the JAR depends on the class loading behavior in the loader library. ")
            .append("If there are any problems like ClassNotFoundExceptions the class path alternative should be chosen if possible. ")
            .append("This alternative, using the loader name, is useful for cases where there are several loader implementations ")
            .append("in the class path of otk at runtime. In both cases the loader library has to provide access to an ")
            .append("implementation of OpenLRMapLoader via Java ServiceLoader interface.\n")
            .append("The map loader identifier is followed by a list of key value pairs.")
            .append("\n")
            .append(HORIZONTAL_LINE)
            .append("\n\"(<path to map jar> | <loader name in the class path>) (,<loader param id>,<value>)*\"\n")
            .append(HORIZONTAL_LINE)
            .append("\nIf there is a need to have a comma itself in a parameter key or value it has to be escaped by backslash \"\\\". ")
            .append("All backslashes in keys and values therefore have to be escaped too, e.g.\n")
            .append(HORIZONTAL_LINE)
            .append("\n\"1,5.10007\\,52.10320\" -> key \"1\", value: \"5.10007,52.10320\"\n")
            .append(HORIZONTAL_LINE)
            .append("\nThe key of each parameter is expected to match the identifier delivered by the parameter implementation.\n")
            .append("To figure out the relevant parameter IDs the user can use the tool itself. If he just specifies the loader ")
            .append("identifier but the loader requires mandatory parameters it will explain which parameter IDs are available.")
            .append("\n\nExamples:\n")
            .append(HORIZONTAL_LINE)
            .append("\n\"tt-sqlite-with-dependencies.jar,1,tomtom_utrecht_2008_04.db3\" (points to a JAR)\n")
            .append(HORIZONTAL_LINE)
            .append("\n\"SQLite Map Loader (TomTom),1,tomtom_utrecht_2008_04.db3\" (refers to a loader in the classpath)")
            .append("\n")
            .append(HORIZONTAL_LINE)
            .append("\n")
            .append("\nPlease note that if there are any blanks in the access string, the entire string must be quoted!")
            .toString();

    @Override
    public final void parse(final String arg, final Object... addArgs)
            throws CommandLineParseException {
        mapDB = loadMap(arg);
    }

    /**
     * Loads a map from the given parameter string.
     * 
     * @param mapLoadString
     *            The parameter string.
     * @return The loaded map in case of success.
     * @throws CommandLineParseException
     *             If an error occurs parsing the parameter string or executing
     *             the OpenLR map loader.
     */
    private MapDatabase loadMap(final String mapLoadString)
            throws CommandLineParseException {

        StringTokenizer st = new StringTokenizer(mapLoadString,
                String.valueOf(VALUE_DELIMITER));
        String mapLoader = null;
        Map<String, String> parametersAndValues = new HashMap<String, String>();
        if (st.hasMoreElements()) {
            mapLoader = st.nextToken().trim();
        }

        String nextToken = null;
        if (st.hasMoreElements()) {
            nextToken = st.nextToken().trim();
        }
        while (nextToken != null) {
            String key = nextToken;

            if (st.hasMoreElements()) {

                nextToken = readAndPutValue(st, parametersAndValues, key);

            } else {
                throw new CommandLineParseException(
                        "Missing parameter value for map loader parameter "
                                + key);
            }

        }

        OpenLRMapLoader loader = identifyMapLoader(mapLoader);

        return executeMapLoader(parametersAndValues, loader);

    }

    /**
     * Uses the string tokenizers current position to read a value for the given
     * key. Handles possible escaped delimiters at the end of the tokens. The
     * value is assembled by the next tokens provided by the tokenizer as long
     * as there are escaped delimiters. If it reaches an unescaped (real
     * delimiter) or the end of the token list the value assembled so far is put
     * into the given map assigned to the given key. All further occurrences of
     * the escape character in key or value are unescaped. It is further checked
     * if the escape character always is properly escaped itself (\\)
     * <p>
     * If there was an unescaped (real delimiter) found the already read next
     * token is returned. The surrounding algorithm should use this string the
     * next token before continuing reading the string tokenizer.
     * <p>
     * Escape handling is the following:
     * <p>
     * The value tokens are parsed from start to end. If an escape character (
     * {@value #ESCAPE_CHARACTER}) is followed by another escape character one
     * is removed, if an escape character is followed by a token delimiter (
     * {@value #VALUE_DELIMITER}) the escape character is removed. If there is
     * an escape character found followed by a different character than the two
     * mentioned before an exception is throw indication improper escaping.
     * <p>
     * This process in repeated as long as there is a real separative delimiter
     * found. The already read next token from the tokenizer is then returned to
     * the surrounding algorithm for further processing.
     * <p>
     * token sequence: "foo\\\\,bar" -> value: "foo\\,", returned: "bar"
     * <p>
     * token sequence: "foo\\\,bar,oof" -> value: "foo\,bar", returned "oof"
     * <p>
     * token sequence: "fo\o" -> throws exception
     * 
     * @param st
     *            The string tokenizer to process
     * @param parametersAndValues
     *            The target map for key and value
     * @param key
     *            The key relevant for the read value
     * @return The next token already retrieved from the tokenizer that does not
     *         apply to the read value
     * @throws CommandLineParseException
     *             If an improper escaping is detected
     */
    private String readAndPutValue(final StringTokenizer st,
            final Map<String, String> parametersAndValues, final String key)
            throws CommandLineParseException {

        String nextToken;
        StringBuilder value = new StringBuilder();
        value.append(st.nextToken().trim());

        boolean valueContinued;
        do {
            if (st.hasMoreElements()) {
                nextToken = st.nextToken().trim();
            } else {
                nextToken = null;
            }

            int numberEscapeCharsAtEnd = 0;
            for (int i = value.length(); i > 0; i--) {
                if (value.charAt(i - 1) == ESCAPE_CHARACTER) {
                    numberEscapeCharsAtEnd++;
                } else {
                    break;
                }
            }

            if (numberEscapeCharsAtEnd > 0) {
                if (numberEscapeCharsAtEnd % 2 == 0) {
                    // escape characters escape only themselves, the value is
                    // really terminated here
                    valueContinued = false;
                } else {
                    // an escaped delimiter, remove the directly prepended
                    // escape character, append delimiter again and the next
                    // part of the value
                    value.append(VALUE_DELIMITER).append(nextToken);
                    valueContinued = true;
                }

            } else {
                valueContinued = false;
            }

        } while (valueContinued);

        parametersAndValues.put(key, unescape(value.toString()));
        return nextToken;
    }

    /**
     * Executes the map loader with the given parameter values.
     * 
     * @param parametersAndValues
     *            The parameters and values as String pairs
     * @param loader
     *            The loader to execute
     * @return The loaded map instance
     * @throws CommandLineParseException
     *             If an error occurs loading the loader with the given
     *             parameters
     */
    private MapDatabase executeMapLoader(
            final Map<String, String> parametersAndValues,
            final OpenLRMapLoader loader) throws CommandLineParseException {

        Collection<MapLoadParameter> parameters = loader.getParameter();

        for (MapLoadParameter mlp : parameters) {

            String value = parametersAndValues.get(String.valueOf(mlp
                    .getIdentifier()));

            if (value != null) {
                mlp.setValue(value);
            } else if (mlp.isRequired()) {
                StringBuilder allParameters = new StringBuilder();
                for (MapLoadParameter param : parameters) {
                    if (allParameters.length() > 0) {
                        allParameters.append(", ");
                    }
                    allParameters.append(param.getName()).append(" (ID ")
                            .append(param.getIdentifier()).append(")");
                }
                throw new CommandLineParseException(
                        "Missing value for mandatory parameter \""
                                + mlp.getName()
                                + "\" with ID "
                                + mlp.getIdentifier()
                                + " for map loader \""
                                + loader.getName()
                                + "\".\nPlease add a parameter specification of the format \""
                                + "<param ID>" + VALUE_DELIMITER + "<value>\" to the map loader string!\n"
                                + "All parameters available are:\n"
                                + allParameters);
            }
        }

        try {
            return loader.load(parameters);
        } catch (OpenLRMapLoaderException e) {
            throw new CommandLineParseException(
                    "Error while executing the map loader, message: "
                            + e.getMessage(), e);
        }
    }

    /**
     * Identifies the specified loader either in a library the identifier points
     * to or in the class-path
     * 
     * @param indentifier
     *            The loader identifier as provided in the parameter string
     * @return The found loader
     * @throws CommandLineParseException
     *             If an error occurs identifying the loader by the given
     *             parameters
     */
    private OpenLRMapLoader identifyMapLoader(final String indentifier)
            throws CommandLineParseException {

        List<OpenLRMapLoader> relevantLoaders;
        String loaderNotFoundInfo;
        ClassLoader contextCL = Thread.currentThread()
                .getContextClassLoader();
        MapLoadersFinder loadersFinder = new MapLoadersFinder();
        
        if (new File(indentifier).exists()) {

            loaderNotFoundInfo = indentifier;
            try {
                relevantLoaders = loadersFinder
                        .extractMapLoaders(loaderNotFoundInfo);
            } catch (FileNotFoundException e) {
                throw new IllegalStateException(
                        "Implementation error, existing file not found!", e);
            }

        } else {
            // try to find the loader in the classpath
            loaderNotFoundInfo = "the classpath with name \"" + indentifier + "\"";
            relevantLoaders = new ArrayList<OpenLRMapLoader>();
            List<OpenLRMapLoader> classpathLoaders = loadersFinder.extractMapLoaders(contextCL);

            for (OpenLRMapLoader lo : classpathLoaders) {
                if (lo.getName().equals(indentifier)) {
                    relevantLoaders.add(lo);
                    break;
                }
            }
        }

        if (relevantLoaders.isEmpty()) {
            throw new CommandLineParseException("No map loader found in "
                    + loaderNotFoundInfo
                    + ". There is a service configuration according to "
                    + ServiceLoader.class.getName()
                    + " required in the loader library!");
        } else if (relevantLoaders.size() > 1) {
            StringBuilder loadersList = new StringBuilder();
            for (OpenLRMapLoader loaderInstance : relevantLoaders) {
                if (loadersList.length() > 0) {
                    loadersList.append(", ");
                }
                loadersList.append(loaderInstance.getName()).append(" (")
                        .append(loaderInstance.getClass().getName())
                        .append(")");
            }

            throw new CommandLineParseException(
                    "Found multiple options for map loaders in the classpath: "
                            + loadersList);
        } else {
            return relevantLoaders.get(0);
        }
    }

    /**
     * Delivers the map database instantiated after a successful parsing of a
     * parameters string
     * 
     * @return The map database
     */
    public final MapDatabase getMapDB() {
        return mapDB;
    }

    /**
     * Removes an escape character from every pair of escape characters. Checks
     * for proper escaping.
     * 
     * @param string
     *            The string to unescape
     * @return The unescaped string
     * @throws CommandLineParseException
     *             If an improper escaping is detected (unescaped escape
     *             character)
     */
    private static String unescape(final String string) throws CommandLineParseException {

        StringBuffer buf = new StringBuffer(string);
        for (int i = 0; i < buf.length() - 1; i++) {
            char c1 = buf.charAt(i);
            char c2 = buf.charAt(i + 1);
            if (c1 == ESCAPE_CHARACTER) {

                if (c2 == ESCAPE_CHARACTER || c2 == VALUE_DELIMITER) {
                    // OK, valid escaped escaped character or single escape char
                    // before a delimiter
                    buf.deleteCharAt(i);
                } else {
                    throw new CommandLineParseException("Unescaped escape character \""
                            + ESCAPE_CHARACTER + "\" in map paramer string \""
                            + string + "\"");
                }
            }
        }
        return buf.toString();
    }

}
