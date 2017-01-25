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
package openlr.mapviewer.utils;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import openlr.map.Line;

/**
 * @author svba
 *
 */
public final class FeatureInfo {

	/**
	 * Instantiates a new feature info.
	 */
	private FeatureInfo() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Creates the feature info for a line.
	 *
	 * @param line the feature
	 * @param html the html
	 * @return the information about the line
	 */
	public static String createLineInfo(final Line line, final boolean html) {
		StringBuilder sb = new StringBuilder();
		String id = Long.toString(line.getID());
		String frc = line.getFRC().name();
		String fow = line.getFOW().name();
		String length = ((Integer) line.getLineLength()).toString();
		String startID = Long.toString(line.getStartNode().getID());
		String endID = Long.toString(line.getEndNode().getID());
		String startLon = Formatter.COORD_FORMATTER.format(line.getStartNode()
				.getLongitudeDeg());
		String startLat = Formatter.COORD_FORMATTER.format(line.getStartNode()
				.getLatitudeDeg());
		String endLon = Formatter.COORD_FORMATTER.format(line.getEndNode()
				.getLongitudeDeg());
		String endLat = Formatter.COORD_FORMATTER.format(line.getEndNode()
				.getLatitudeDeg());
		Map<Locale, List<String>> names = (Map<Locale, List<String>>) line
				.getNames();
		if (html) {
			sb.append("<html>");
		}
		sb.append("Line ID ").append(id);
		if (html) {
			sb.append("<br>");
		} else {
			sb.append("\n");
		}
		sb.append("FRC: ").append(frc);
		if (html) {
			sb.append("<br>");
		} else {
			sb.append("\n");
		}
		sb.append("FOW: ").append(fow);
		if (html) {
			sb.append("<br>");
		} else {
			sb.append("\n");
		}
		sb.append("Length ").append(length).append("m");
		if (html) {
			sb.append("<br>");
		} else {
			sb.append("\n");
		}
		sb.append("Start node: ").append(startID);
		if (html) {
			sb.append("<br>");
		} else {
			sb.append("\n");
		}
		sb.append(" at: ").append(startLon).append(" / ").append(startLat);
		if (html) {
			sb.append("<br>");
		} else {
			sb.append("\n");
		}
		sb.append("End node: ").append(endID);
		if (html) {
			sb.append("<br>");
		} else {
			sb.append("\n");
		}
		sb.append(" at: ").append(endLon).append(" / ").append(endLat);
		if (html) {
			sb.append("<br>");
		} else {
			sb.append("\n");
		}
		sb.append(LineNameResolver.resolveLineName(names));
		if (html) {
			sb.append("<br>");
			sb
					.append("<i>Press \"i\" to open information panel.</i><html>");
		} else {
			sb.append("\n");
		}
		return sb.toString();
	}
}

