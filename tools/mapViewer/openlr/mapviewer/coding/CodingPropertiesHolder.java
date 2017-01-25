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
package openlr.mapviewer.coding;

import org.apache.commons.configuration.FileConfiguration;

/**
 * This class acts a central registry that provides access to unique instances
 * of encoder and decoder properties.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class CodingPropertiesHolder {

    /**
     * The possible coding types
     */
    public enum CodingType {

        /**
         * OpenLR decoding
         */
        DECODING("OpenLR-Decoder-Properties.xml"),

        /**
         * OpenLR encoding
         */
        ENCODING("OpenLR-Encoder-Properties.xml");

        /** The default properties */
        private final String defaultPropertiesPath;

        /**
         * 
         * @param defaultPropsPath
         *            The default properties path
         */
        private CodingType(final String defaultPropsPath) {
            defaultPropertiesPath = defaultPropsPath;
        }

        /**
         * Delivers the path in the class-path to the default properties for
         * this type
         * 
         * @return The sub-path in the class-path to the default properties
         */
        public String getDefaultPropertiesPath() {
            return defaultPropertiesPath;
        }
    }

    /**
     * The map holding the currently active configurations for each type
     */    
    private final FileConfiguration[] configs = new FileConfiguration[CodingType
            .values().length];

    /**
     * Initializes the properties holder.
     * 
     * @param initialEncodingConfig
     *            The initial encoder configuration
     * @param initialDecodingConfig
     *            The initial decoder configuration
     */
    public CodingPropertiesHolder(final FileConfiguration initialEncodingConfig,
            final FileConfiguration initialDecodingConfig) {
        configs[CodingType.ENCODING.ordinal()] = initialEncodingConfig;
        configs[CodingType.DECODING.ordinal()] = initialDecodingConfig;
    }

    /**
     * Delivers the current properties
     * 
     * @param type
     *            the type of properties to deliver
     * @return The current properties
     */
    public final FileConfiguration getProperties(final CodingType type) {
        return configs[type.ordinal()];
    }

    /**
     * Sets the properties that shall replace the current settings
     * 
     * @param type
     *            the type of properties to set
     * @param config
     *            The properties to activate
     */
    public final void setProperties(final CodingType type,
            final FileConfiguration config) {
        configs[type.ordinal()] = config;
    }

}
