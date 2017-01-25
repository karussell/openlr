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

import openlr.otk.common.CommandLineParseException;

import org.apache.commons.cli.Option;

/**
 * The Interface Option.
 */
public abstract class CommandLineOption {
	
	/** The option. */
	private final Option option;
	
    /**
     * Instantiates a new loc ref tool option.
     * 
     * @param shortOpt
     *            short representation of the option
     * @param longOpt
     *            the long representation of the option
     * @param hasArg
     *            specifies whether the Option takes an argument or not
     * @param desc
     *            describes the function of the option
     * @param mand
     *            A flag indication whether this option is mandatory
     */
    public CommandLineOption(final String shortOpt, final String longOpt,
            final boolean hasArg, final String desc, final boolean mand) {
        option = new Option(shortOpt, longOpt, hasArg, desc);
        option.setRequired(mand);
    }
	
	
    /**
     * Parses the argument.
     *
     * @param arg the arg
     * @param addArgs the add args
     * @throws CommandLineParseException the parse exception
     */
    public abstract void parse(String arg, Object... addArgs) throws CommandLineParseException;

    
    /**
     * Gets the option.
     *
     * @return the option
     */
    public final Option getOption() {
    	return option;
    }
    
    /**
     * Gets the short identifier.
     *
     * @return the short identifier
     */
    public final String getShortIdentifier() {
    	return option.getOpt();
    }

}
