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
package openlr.otk.kml;

import java.util.List;

import openlr.map.GeoCoordinates;
import openlr.map.Line;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;

/**
 * Bundles a KML output generation. Provides utility functions to write spatial
 * objects to a KML file.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public final class KmlUtil {

	/** The Constant COORDS_ARRAY_SIZE. */
	private static final int COORDS_ARRAY_SIZE = 6;

	/**
	 * Disabled constructor
	 */
	private KmlUtil() {
		// disabled constructor
	}

	/**
	 * Creates a KML point at a specific location.
	 * 
	 * @param longitude
	 *            the longitude value of the location.
	 * @param latitude
	 *            the latitude value of the location.
	 * @param id
	 *            the id of the KML element.
	 * @param name
	 *            the name of the KML element.
	 * @param description
	 *            the description of the KML element.
	 * @param style
	 *            an identifier of a style that should be applied to this KML
	 *            element.
	 * @return The created placemark
	 */
	public static Placemark createPoint(final double longitude,
			final double latitude, final String id, final String name,
			final String description, final StyleIdentifier style) {

		final Placemark placemark = new Placemark()
				.withStyleUrl("#" + style.getID()).withName(name)
				.withDescription(description);
		final Point point = placemark.createAndSetPoint().withId(id);
		point.addToCoordinates(longitude, latitude);

		return placemark;
	}

	/**
	 * Creates a KML line string from a given {@link openlr.map.Line}.
	 * 
	 * @param line
	 *            the {@link openlr.map.Line} to store in the KML folder.
	 * @param id
	 *            the id of the KML element.
	 * @param name
	 *            the name of the KML element.
	 * @param description
	 *            the description of the KML element.
	 * @param style
	 *            an identifier of a style that should be applied to this KML
	 *            element.
	 * @return The created placemark
	 */
	public static Placemark createLineString(final Line line, final String id,
			final String name, final String description,
			final StyleIdentifier style) {
		final Placemark placemark = new Placemark().withName(name)
				.withStyleUrl("#" + style.getID()).withDescription(description);
		final LineString lineString = placemark.createAndSetLineString()
				.withId(id);
		List<GeoCoordinates> shape = line.getShapeCoordinates();
		if (shape != null) {
			for (GeoCoordinates gc : shape) {
				lineString.addToCoordinates(gc.getLongitudeDeg(),
						gc.getLatitudeDeg());
			}
		} else {
			// the style of the line cannot be changed to dashed as KML does not
			// support this feature.
			lineString.addToCoordinates(line.getStartNode().getLongitudeDeg(),
					line.getStartNode().getLatitudeDeg());
			lineString.addToCoordinates(line.getEndNode().getLongitudeDeg(),
					line.getEndNode().getLatitudeDeg());
			placemark.setDescription(description + " [abstract geometry]");
		}
		return placemark;
	}

	/**
	 * Creates a KML line string from a given sequence of geo coordinates.
	 * 
	 * @param coordinates
	 *            the sequence of geo coordinates that defines the path of the
	 *            line string
	 * @param id
	 *            the id of the KML element.
	 * @param name
	 *            the name of the KML element.
	 * @param description
	 *            the description of the KML element.
	 * @param style
	 *            an identifier of a style that should be applied to this KML
	 *            element.
	 * @return The created placemark
	 */
	public static Placemark createLineString(
			final List<GeoCoordinates> coordinates, final String id,
			final String name, final String description,
			final StyleIdentifier style) {
		final Placemark placemark = new Placemark().withName(name)
				.withStyleUrl("#" + style.getID()).withDescription(description);
		final LineString lineString = placemark.createAndSetLineString()
				.withId(id);

		for (GeoCoordinates coord : coordinates) {
			lineString.addToCoordinates(coord.getLongitudeDeg(),
					coord.getLatitudeDeg());
		}

		return placemark;
	}
}
