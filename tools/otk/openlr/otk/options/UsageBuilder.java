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
package openlr.otk.options;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import openlr.otk.utils.IOUtils;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * This class writes a formatted command help to a given stream.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
final class UsageBuilder {

    /**
     * Disabled constructor
     */
    private UsageBuilder() {
    }

    /**
     * The footer string of the tool usage
     */
    private static final String USAGE_FOOTER = "\n(c) 2009 - 2013 TomTom Internation B.V.";

    /**
     * Prints the command line syntax.
     * 
     * @param target
     *            The target stream to write to
     * @param options
     *            The tool options
     * @param argsList
     *            The tool arguments
     * @param toolID
     *            The tool short name
     * @param toolDescription
     *            The description of the tool
     */
    public static void usage(final OutputStream target, final Options options,
            final List<Argument<?>> argsList, final String toolID,
            final String toolDescription) {

        HelpFormatter formatter = new HelpFormatter();
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(target,
                IOUtils.SYSTEM_DEFAULT_CHARSET));

        String optionsListStr = buildOptionsList(options);
        String argsListStr = buildArgumentsList(argsList);
        formatter.printUsage(pw, HelpFormatter.DEFAULT_WIDTH,
                "java -jar otk-<version>.jar " + toolID + " " + optionsListStr
                        + " " + argsListStr);
        formatter
                .printWrapped(pw, HelpFormatter.DEFAULT_WIDTH, toolDescription);

        if (options.getOptions().size() > 0) {

            formatter.printOptions(pw, HelpFormatter.DEFAULT_WIDTH, options,
                    HelpFormatter.DEFAULT_LEFT_PAD,
                    HelpFormatter.DEFAULT_DESC_PAD);
        }

        printArgumentDescriptions(pw, argsList);

        formatter.printWrapped(pw, HelpFormatter.DEFAULT_WIDTH, USAGE_FOOTER);

        pw.flush();
    }

    /**
     * Prints the help for the tool arguments.
     * 
     * @param pw
     *            The writer to use
     * @param args
     *            The tool arguments
     */
    private static void printArgumentDescriptions(final PrintWriter pw,
            final List<Argument<?>> args) {

        if (args.size() > 0) {

            pw.write(IOUtils.LINE_SEPARATOR);

            // We use the HelpFormatter to format the argument help for us
            // by providing fake options containing the arguments
            Options options = new Options();
            for (Argument<?> argument : args) {
                Option opt = new Option(null, argument.getName(), false,
                        argument.getDescription());
                options.addOption(opt);
            }

            HelpFormatter formatter = new HelpFormatter();
            formatter.setLongOptPrefix("");

            formatter.printOptions(pw, HelpFormatter.DEFAULT_WIDTH, options,
                    HelpFormatter.DEFAULT_LEFT_PAD,
                    HelpFormatter.DEFAULT_DESC_PAD);
        }
    }

    /**
     * Builds the string displaying the list of available options in one row.
     * 
     * @param options
     *            The tool options
     * @return The string listing all available options.
     */
    private static String buildOptionsList(final Options options) {

        Collection<?> optionsColl = options.getOptions();
        List<String> required = new ArrayList<String>(optionsColl.size());
        List<String> optional = new ArrayList<String>(optionsColl.size());

        for (Object option : optionsColl) {
            Option opt = (Option) option;
            if (opt.isRequired()) {
                required.add(opt.getOpt());

            } else {
                optional.add(opt.getOpt());
            }
        }
        return buildFormattedLists(required, optional, "-%s");
    }

    /**
     * Builds the string displaying the list of available arguments in one row.
     * 
     * @param arguments
     *            The tool arguments
     * @return The string listing all available arguments.
     */
    private static String buildArgumentsList(final List<Argument<?>> arguments) {

        List<String> required = new ArrayList<String>(arguments.size());
        List<String> optional = new ArrayList<String>(arguments.size());

        for (Argument<?> arg : arguments) {
            if (arg.isRequired()) {
                required.add(arg.getName());
            } else {
                optional.add(arg.getName());
            }
        }

        return buildFormattedLists(required, optional, "[%s]");
    }

    /**
     * A helper method that builds a list of strings separated by blanks. Each
     * token is formatted using the given {@code entryFormat} (see
     * {@link String#format(String, Object...)}). The result string will first
     * list the entries of {@code plain} and the all entries of list
     * {@code inBrackets}. The list of {@code inBrackets} entries is surrounded
     * by brackets.
     * 
     * @param plain
     *            The tokens that will be listed plain at the beginning
     * @param inBrackets
     *            The tokens that will be listed in brackets at the end
     * @param entryFormat
     *            The string format applied to each entry
     * @return The final string containing all formatted entries
     */
    private static String buildFormattedLists(final List<String> plain,
            final List<String> inBrackets, final String entryFormat) {

        String requiredStr = buildFormattedList(plain, entryFormat);

        String optionalStr = buildFormattedList(inBrackets, entryFormat);

        StringBuilder result = new StringBuilder(requiredStr.length()
                + optionalStr.length() + 5);
        result.append(requiredStr);

        // optional options in brackets
        if (optionalStr.length() > 0) {
            if (result.length() > 0) {
                result.append(" ");
            }
            result.append("(").append(optionalStr).append(")");
        }

        return result.toString();
    }

    /**
     * A helper method that builds a string listing formatted entries separated
     * by blanks.
     * 
     * @param tokens
     *            The tokens to list
     * @param entryFormat
     *            The format to apply to each entry
     * @return The string listing the formatted entries
     */
    private static String buildFormattedList(final List<String> tokens,
            final String entryFormat) {
        StringBuilder result = new StringBuilder();
        for (String string : tokens) {

            if (result.length() > 0) {
                result.append(" ");
            }
            result.append(String.format(entryFormat, string));
        }

        return result.toString();
    }
}
