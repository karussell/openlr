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
import openlr.map.GeoCoordinates;

/** 
 * This class creates the JavaScript code for drawing a google.maps.Marker.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class Marker {
   
	/**
	 * The script code.
	 */
   private final String script;
   
	/**
	 * Creates a new googl.maps.Marker at the specified GEO-coordinate.
	 * 
	 * @param title
	 *            The title text, visible when the mouse accesses the marker.
	 * @param variableName
	 *            The name of the created JavaScript object of this marker.
	 * @param point
	 *            The point the marker marks.
	 */
	public Marker(final String title, final String variableName,
			final GeoCoordinates point) {
		this(title, variableName, point, null);
	}

	/**
	 * Creates a new googl.maps.Marker at the specified GEO-coordinate with a
	 * customized icon image.
	 * 
	 * @param title
	 *            The title text, visible when the mouse accesses the marker.
	 * @param variableName
	 *            The name of the created JavaScript object of this marker.
	 * @param point
	 *            The point the marker marks.
	 * @param markerImage
	 *            The name of a predefined google.maps.MarkerImage (see
	 *            {@link MarkerImage}) replacing the default icon. The variable
	 *            with this name should be set in the surrounding script.
	 */
	public Marker(final String title, final String variableName,
			final GeoCoordinates point, final String markerImage) {

		StringBuilder result = new StringBuilder("var " + variableName
				+ " = new google.maps.Marker({" + NEW_LINE
				+ "  position: new google.maps.LatLng("
				+ point.getLatitudeDeg() + "," + "  " + point.getLongitudeDeg()
				+ ")," + NEW_LINE + " map: map, " + NEW_LINE + "  title:\""
				+ title + "\"");
		if (markerImage != null) {
			result.append("," + NEW_LINE);
			result.append(" icon: " + markerImage);
		}
		result.append(NEW_LINE + "}); " + NEW_LINE);

		script = result.toString();
	}

	/**
	 * @return The JavaScript code drawing the googl.maps.Marker.
	 */
	@Override
	public final String toString() {
		return script;
	}	
}
