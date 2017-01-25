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
package openlr.mapviewer.gmaps.scriptlets;

import static openlr.mapviewer.gmaps.scriptlets.ScriptConstants.NEW_LINE;

import java.util.HashMap;
import java.util.Map;

import openlr.mapviewer.gmaps.GoogleMapsStarter;

/**
 * This class encapsulates a Google Maps PolylineOptions object which can be
 * used to setup a {@link Polyline} scriptlet.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class PolylineOptions {

    public static final double STROKE_OPACITY_DEFAULT = 0.5;
    
    public static final int STROKE_WEIGHT_DEFAULT = 4;
    
    private final String id;

    private final Map<String, String> keyValues = new HashMap<String, String>();

    public PolylineOptions(final String variableName, final String strokeColor,
            final double strokeOpacity, final int strokeWeight) {

        id = variableName;

        keyValues.put("strokeColor", strokeColor);
        keyValues.put("strokeOpacity", "" + strokeOpacity);
        keyValues.put("strokeWeight", "" + strokeWeight);
        // this is so far not changeable
        keyValues.put("map", GoogleMapsStarter.IDENTIFIER_MAP_OBJECT);
    }

    /**
     * Delivers the variable name assigned to this PolylineOptions object.
     * 
     * @return The variable identifier
     */
    String getVariableName() {
        return id;
    }

    /**
     * Prints the JavaScript code of the PolylineOptions object.
     */
    @Override
    public String toString() {
        return "var " + id + " = {" + NEW_LINE + "strokeColor: \""
                + keyValues.get("strokeColor") + "\"," + "strokeOpacity: "
                + keyValues.get("strokeOpacity") + "," + NEW_LINE
                + "strokeWeight: " + keyValues.get("strokeWeight") + "};" + NEW_LINE;
    }

}
