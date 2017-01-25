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
package openlr.otk.kml.locationreference;

import openlr.otk.kml.StyleIdentifier;

/**
 * This enumeration defines style identifiers available for formatting location
 * references.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
enum LocRefStyleIdentifier implements StyleIdentifier {

    /**
     * The style identifier of LRP styles
     */
    LRP("locRefPoint"),

    /**
     * The style identifier of common point styles
     */
    POINT("point");

    /**
     * The identifier string
     */
    private final String identifier;

    /**
     * Creates a new style type identifier
     * 
     * @param id
     *            A unique identifier of the style, this string will be extended
     *            by the hash code of this class to make it unique
     */
    private LocRefStyleIdentifier(final String id) {
        identifier = id + "_" + getClass().getName().hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getID() {
        return identifier;
    }

}
