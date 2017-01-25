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

import openlr.map.GeoCoordinatesImpl;
import openlr.map.Line;
import openlr.map.Node;

/**
 * This class creates the JavaScript code for drawing a sequence of
 * <code>google.maps.Marker</code>s representing the start nodes of each line
 * and the location end node.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class LocationNodes {

	/**
	 * The anchor point of the marker image used for a node.
	 */
	private static final int[] IMAGE_NODES_ANCHOR_POINT = new int[] {16, 16};

	/**
	 * The URL of the image used for a node marker.
	 */
	private static final String IMAGE_NODES_URL = "http://maps.google.com/mapfiles/kml/shapes/placemark_circle.png";

	/**
	 * The script code.
	 */
	private final String script;

	/**
	 * Delivers the JavaScript code for drawing a sequence of
	 * <code>google.maps.Marker</code>s representing the start nodes of each
	 * line as the location end node.
	 * 
	 * @param varName
	 *            The identifier of the JavaScript variable the Polyline object
	 *            is assigned to.
	 * @param locationLines
	 *            The list of lines to create the entries from.
	 */
	public LocationNodes(final String varName,
			final List<? extends Line> locationLines) {

		script = createMarkerSequence(varName, locationLines);
	}

	/**
	 * @return The JavaScript code drawing the location lines.
	 */
	@Override
	public final String toString() {
		return script;
	}

	/**
	 * Creates the sequence of marker commands.
	 * 
	 * @param variablePrefix
	 *            A prefix of the variable name for each marker. It will be
	 *            suffixed by running number starting from 1.
	 * @param lines
	 *            The location lines.
	 * @return The commands for creation of the node markers.
	 */
	private String createMarkerSequence(final String variablePrefix,
			final List<? extends Line> lines) {

		StringBuilder result = new StringBuilder();

		// first the icon image
		String iconVar = variablePrefix + "Icon";
		result.append(
				new MarkerImage(iconVar, IMAGE_NODES_URL,
						IMAGE_NODES_ANCHOR_POINT, null)).append(NEW_LINE);

		int i = 0;
		Line line = null;

		for (i = 0; i < lines.size(); i++) {
			line = lines.get(i);
			createNodeMarker(variablePrefix + (i + 1), iconVar, result,
					line.getStartNode());
		}

		// append the end point of the last line
		createNodeMarker(variablePrefix + (i + 1), iconVar, result,
				line.getEndNode());

		return result.toString();
	}

	/**
	 * Creates a single node marker.
	 * 
	 * @param varName
	 *            The name of the JavaScript variable for this marker.
	 * @param iconVariable
	 *            The name of the JavaScript variable defining the
	 *            google.maps.MakrerImage to use for drawing the node marker.
	 * @param scriptCode
	 *            The script code up to this point.
	 * @param node
	 *            The node this marker stands for.
	 */
    private void createNodeMarker(final String varName,
            final String iconVariable, final StringBuilder scriptCode,
            final Node node) {
        String message = "Node #" + node.getID() + " at "
                + node.getLatitudeDeg() + ", " + node.getLongitudeDeg();
        scriptCode.append(new Marker(message, varName, GeoCoordinatesImpl
                .newGeoCoordinatesUnchecked(node.getLongitudeDeg(),
                        node.getLatitudeDeg()), iconVariable));
        scriptCode.append(NEW_LINE);
    }
}
