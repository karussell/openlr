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

/** 
 * This class creates the JavaScript code for defining a google.maps.MarkerImage.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class MarkerImage {
	   
	/**
	 * The script code.
	 */	
	private final String script;
	
	/**
	 * Creates code for a new google.maps.MarkerImage.
	 * 
	 * @param variableName
	 *            The name of the created JavaScript object of this marker.
	 * @param iconUrl
	 *            The URL to the icon image.
	 * @param anchorPoint
	 *            The anchor point of the icon in pixels ({x, y}). This
	 *            parameter is optional. If not specified the default is the
	 *            middle of the bottom.
	 * @param size
	 *            The size of the icon in pixels ({width, height}).
	 */
	public MarkerImage(final String variableName, final String iconUrl,
			final int[] anchorPoint, final int[] size) {

		StringBuilder strb = new StringBuilder();
		strb.append("var " + variableName + " = new google.maps.MarkerImage('"
				+ iconUrl + "', null, null, ");

		if (anchorPoint != null) {
			strb.append("new google.maps.Point(" + anchorPoint[0] + ", "
					+ anchorPoint[1] + ")");
		} else {
			strb.append("null");
		}
		strb.append(",");
		if (size != null) {
			strb.append("new google.maps.Size(" + size[0] + ", " + size[1]
					+ ")");
		} else {
			strb.append("null");
		}

		strb.append(");");
		script = strb.toString();
	}

	/**
	 * @return The JavaScript code defining the googl.maps.MarkerImage.
	 */
	@Override
	public final String toString() {
		return script;
	}
}
