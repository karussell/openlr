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

import openlr.otk.common.CommandLineParseException;

/**
 * This class defines an command line argument that is not specified by a
 * option.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 * 
 * @param <T>
 *            The type of output data this argument implementation produces from
 *            the input strings
 */
public abstract class Argument<T> {

    /**
     * The argument name
     */
    private final String argName;

    /**
     * The argument description to display in the usage
     */
    private final String desc;

    /**
     * A flag indication whether this argument is mandatory.
     */
    private final boolean mand;

    /**
     * Creates a new argument instance
     * 
     * @param name
     *            The argument name
     * @param mandatory
     *            A flag indication whether this argument is mandatory.
     * @param description
     *            The argument description
     */
    public Argument(final String name, final boolean mandatory,
            final String description) {
        argName = name;
        mand = mandatory;
        desc = description;
    }
    
    /**
     * Parses the given value. This will calculate the internal data depending
     * on the input value and makes it available via {@link #getData()}.
     * 
     * @param commandLineValue
     *            The related value specified by the user
     * @throws CommandLineParseException
     *             If an error occurs in parsing the given value
     */
    public abstract void parse(final String commandLineValue)
            throws CommandLineParseException;
    
    /**
     * Delivers the data that is produced according to the given command line
     * value. Can return {@code null} if {@link #parse(String)} was not yet
     * called, i.e. no user input was yet provided.
     * 
     * @return The data produced according to the input value
     */
    public abstract T getData();

    /**
     * Delivers the argument name
     * 
     * @return The argument name
     */
    public final String getName() {
        return argName;
    }

    /**
     * Delivers the argument description
     * 
     * @return The argument description
     */
    public final String getDescription() {
        return desc;
    }

    /**
     * Delivers whether this argument is mandatory for the tool.
     * 
     * @return {@code true} if the argument is mandatory, otherwise
     *         {@code false}
     */
    public final boolean isRequired() {
        return mand;
    }
}
