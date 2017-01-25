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
package openlr.otk.coding;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import openlr.OpenLRProcessingException;
import openlr.map.MapDatabase;
import openlr.otk.common.CommandLineParseException;
import openlr.otk.options.CommandLineData;
import openlr.otk.options.InputFileOption;
import openlr.otk.options.InputFileOrDataOption;
import openlr.otk.options.MapLoadFromParametersStringOption;
import openlr.properties.OpenLRPropertiesReader;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

/**
 * The class BaseOptions provides base parameters and base functionality for the
 * encoding and decoding tools.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
abstract class BaseOptions extends CommandLineData {
    
    /**
     * @param toolIdentifier
     *            The identifier of the related OTK tool
     * @param description
     *            A description of the related OTK tool used for building the
     *            usage information to the user
     */
    public BaseOptions(final String toolIdentifier, final String description) {
        super(toolIdentifier, description);
    }

    /** The Constant LOG. */
    private static final Logger LOG = Logger.getLogger(BaseOptions.class);

    /** The encoder properties. */
    private Configuration properties;

    /** The Constant inputOption. */
    protected final InputFileOrDataOption inputOption = new InputFileOrDataOption();

    

    /**
     * The map access option
     */
    protected final MapLoadFromParametersStringOption mapAccessOption = new MapLoadFromParametersStringOption(
            "m",
            "map-access",
            true);

    /**
     * Command line parameter: properties
     */
    protected final InputFileOption propertiesOption = new InputFileOption(
            "p",
            "properties",
            "Path to the properties. If no properties are declared then the default properties are used.",
            false);

    

    /**
     * Gets the map path.
     * 
     * @return the map path
     */
    public final MapDatabase getMap() {
        return mapAccessOption.getMapDB();
    }

    /**
     * Delivers the relevant properties for this run.
     * 
     * @return The relevant properties
     */
    public final Configuration getProperties() {
        return properties;
    }

    /**
     * Sets the relevant properties either from the user setting in the
     * properties option or if not set from the given resource from the class
     * path.
     * 
     * @param propsOption
     *            The input properties action
     * @param defaultClassPathProperties The
     *            name of a properties file to search in the class path for
     * @throws CommandLineParseException
     *             If an error occurs reading the options 
     */
    protected final void setProperties(final InputFileOption propsOption,
            final String defaultClassPathProperties) throws CommandLineParseException {

        InputStream is = null;
        byte[] data = propsOption.getData();

        try {

            if (data == null) {

                is = Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream(defaultClassPathProperties);

            } else {
                is = new ByteArrayInputStream(data);
            }
            properties = OpenLRPropertiesReader.loadPropertiesFromStream(is,
                    true);
        } catch (OpenLRProcessingException e) {
            throw new CommandLineParseException("Parsing of properties failed, message: "
                    + e, e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                LOG.warn("Error closing properties stream");
            }
        }
    }

    /**
     * Delivers the input data stream.
     * 
     * @return the input stream
     */
    public final InputStream getInput() {
        return inputOption.getInputStream();
    }

    /**
     * Delivers an identifier/ name of the input source.
     * 
     * @return The source data identifier
     */
    public final String getInputSourceIdentifier() {

        return inputOption.getInputSource();
    }
}
