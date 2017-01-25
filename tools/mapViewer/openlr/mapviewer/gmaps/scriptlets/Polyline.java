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

import java.util.List;

import openlr.map.GeoCoordinates;
import openlr.mapviewer.gmaps.GoogleMapsStarter;

/**
 * This class encapsulates the JavaScript representation of a Google Maps
 * Polyline which is an line defined by shape points.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class Polyline {

    /**
     * The coordinates
     */
    private final List<GeoCoordinates> theCoordinates;

    /**
     * The array identifier
     */
    private final String id;

    private final String scriptPrefix;

    private final PolylineOptions options;

    /**
     * Creates a new Polyline in a simple way. All default options are used,
     * except the color code can be set here.
     * 
     * @param coordinates
     *            The ordered list of coordinates used to define the Polyline
     * @param variableName
     *            The identifier of the Polyline JavaScript object
     * @param lineColor
     *            The color code (HTML) to use for drawing the lines
     */
    public Polyline(List<GeoCoordinates> coordinates,
            final String variableName, final String lineColor) {
        if (coordinates == null) {
            throw new IllegalArgumentException(
                    "Coordinates list provided to LatLongArray must not be null!");
        }
        theCoordinates = coordinates;
        id = variableName;

        options = new PolylineOptions(id + "Options", lineColor,
                PolylineOptions.STROKE_OPACITY_DEFAULT,
                PolylineOptions.STROKE_WEIGHT_DEFAULT);

        // in case of default options we write a generated options instance into
        // the script before the later polyline
        scriptPrefix = options.toString();
    }

    /**
     * This sets up a Polyline referring to an externally defined
     * {@link PolylineOptions} instance. This enables to implement reuse of a
     * static options set effectively. <b>Note that in this case the caller is
     * responsible to add the polylineOptions to the script code before this
     * {@link Polyline}!</b> It will be referred from the Polyline via its
     * {@link PolylineOptions#getVariableName()}.
     * 
     * @param coordinates
     *            The ordered list of coordinates used to define the Polyline
     * @param variableName
     *            The identifier of the Polyline JavaScript object
     * @param polylineOptions
     *            A prepared polylin Options instance whic will be referred to
     *            by the Polyline.
     */
    public Polyline(List<GeoCoordinates> coordinates,
            final String variableName, final PolylineOptions polylineOptions) {
        theCoordinates = coordinates;
        id = variableName;
        options = polylineOptions;
        scriptPrefix = "";
    }

    /**
     * Delivers the specified variable name of the Polylone JavaScript object.
     * 
     * @return The array variable name
     */
    public String getVariableName() {
        return id;
    }

    /**
     * Prints the JavaScript representation of this Polyline
     */
    @Override
    public String toString() {

        StringBuilder scriptCode = new StringBuilder(scriptPrefix);
        String arrayVariableName = id + "Arr";

        addPathCoordinatesArray(scriptCode, arrayVariableName);

        addPolylineDefinition(id, scriptCode, arrayVariableName);

        return scriptCode.toString();
    }

    private void addPathCoordinatesArray(final StringBuilder scriptCode,
            final String arrayVariableName) {
        boolean first = true;

        scriptCode.append("var " + arrayVariableName + " = [");

        for (GeoCoordinates current : theCoordinates) {

            if (!first) { // if there were entries before
                scriptCode.append(",").append(NEW_LINE);
            } else {
                first = false;
            }

            scriptCode.append("new google.maps.LatLng(")
                    .append(current.getLatitudeDeg()).append(", ")
                    .append(current.getLongitudeDeg()).append(")");

        }

        scriptCode.append("];").append(NEW_LINE);
    }

    /**
     * Adds the actual google.maps.Polyline object definition code.
     * 
     * @param varName
     *            The variable name of the Polyline object in the script.
     * @param scriptCode
     *            The script code up to this point.
     * @param arrayVarName
     *            The variable name of the array containing the Polyline points
     *            which should be yet contained in the script.
     */
    private void addPolylineDefinition(final String varName,
            final StringBuilder scriptCode, final String arrayVarName) {

        scriptCode.append("var " + varName + " = new google.maps.Polyline("
                + options.getVariableName() + ");" + NEW_LINE + varName
                + ".setMap(" + GoogleMapsStarter.IDENTIFIER_MAP_OBJECT + ");"
                + NEW_LINE + varName + ".setPath(" + arrayVarName + ");"
                + NEW_LINE);
    }

}
