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
import openlr.otk.common.Format;




/**
 * The Class InputFormatOption.
 */
public class InputFormatOption extends CommandLineOption {
	
	
	/**
     * The OpenLR data format of the input.
     */
    private Format inputFormat;
    
    /**
     * Instantiates a new input format option.
     *
     * @param mand the mand
     */
    public InputFormatOption(final boolean mand) {
		super("if", "input-format", true, "identifies the format of the input data\n" + Format.availableFormatString(), mand);
	}
    
    /**
     * {@inheritDoc}
     */
	@Override
    public final void parse(final String arg, final Object... addArgs)
            throws CommandLineParseException {
        inputFormat = Format.parseString(arg);
    }
	
	/**
     * Delivers the OpenLR data format of the input.
     * 
     * @return The template file to use for the input.
     */
    public final Format getInputFormat() {
        return inputFormat;
    }

}
