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
package openlr.mapviewer.properties;

/**
 * Enumeration that specifies all properties relevant for drawing the map 
 * representation of the MapViewer application.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public enum MapDrawingProperties implements MapViewerProperty {

	/** FOW type "other" */
	FOW_OTHER("colors.fow.other"),

	/** FOW type "slip road" */
	FOW_SLIPROAD("colors.fow.sliproad"),

	/** FOW type "traffic square" */
	FOW_TRAFFIC_SQUARE("colors.fow.trafficSquare"),

	/** FOW type "roundabout" */
	FOW_ROUNDABOUT("colors.fow.roundabout"),

	/** FOW type "single carriage way" */
	FOW_SINGLE_CARRIAGEWAY("colors.fow.singleCarriageway"),

	/** FOW type "multiple carriage way" */
	FOW_MULTIPLE_CARRIAGEWAY("colors.fow.multipleCarriageway"),

	/** FOW type "motor way" */
	FOW_MOTORWAY("colors.fow.motorway"),

	/** FOW type "undefined" */
	FOW_UNDEFINED("colors.fow.undefined"),

	/** FRC class 7 */
	FRC_FRC7("colors.frc.frc7"),

	/** FRC class 6 */
	FRC_FRC6("colors.frc.frc6"),

	/** FRC class 5 */
	FRC_FRC5("colors.frc.frc5"),

	/** FRC class 4 */
	FRC_FRC4("colors.frc.frc4"),
	
	/** FRC class 0 */
	FRC_FRC3("colors.frc.frc3"),	
	
	/** FRC class 0 */
	FRC_FRC2("colors.frc.frc2"),	

	/** FRC class 1 */
	FRC_FRC1("colors.frc.frc1"),
	
	/** FRC class 0 */
	FRC_FRC0("colors.frc.frc0"),	

    /** Stored location. */
	LOCATION_STORED("colors.location.stored"),

	/** Decoded location. */
	LOCATION_DECODER("colors.location.decoder"),

	/** Encoded location. */
	LOCATION_ENCODER("colors.location.encoder"),
	
	/** All locations after loading several. */
	LOCATIONS_ALL("colors.locations.all"),

	/** A highlighted location. */
	HIGHLIGHTED_LINE("colors.line.highlighted"),

	/** The negative offset point. */
	LOCATION_NEG_OFF("colors.location.negOff"),

	/** The positive offset point. */
	LOCATION_POS_OFF("colors.location.posOff"),

	/** The geo coordinate of a geo-coordinate location. */
	LOCATION_GEO_COORD("colors.location.geoCoord"),

	/** The start node of a location. */
	LOCATION_START_NODE("colors.location.startNode"),

	/** A common network node. */
	NODE("colors.node"),

	/** The start node of a line. */
	LINE_START_NODE("colors.line.startNode"),

	/** The lines of a search result. */
	SEARCH_RESULT("colors.search.result"),

	/** The lines of the selected location. */
	LOCATION_SELECTED("colors.location.selected"),

	/** The line of the measure element. */
	MAPPANE_MEASURE("colors.mappane.measure"),

	/** A common network line */
	LINE("colors.line"),

	/** The scale information on the map panel. */
	MAPPANE_SCALE("colors.mappane.scale"),
	
	/** The stroke size. */
	NODE_STROKE_SIZE("size.stroke.node"), 
	
	/** The stroke size for lines. */
	LINE_STROKE_SIZE("size.stroke.line"), 
	
	/** The stroke size for location lines. */	
	LOCATION_LINE_STROKE_SIZE("size.location.stroke.line"), 
	
	/** The node mark size. */
	NODE_MARK_SIZE("size.mark.node"),
	
	/** The point mark size. */
	POINT_MARK_SIZE("size.mark.point"),
	
	/**  The area location covered line. */
	AREA_LOCATION_COVERED_LINE("colors.area.location.covered.line"),
	
	/**  The area location intersected line. */
	AREA_LOCATION_INTERSECTED_LINE("colors.area.location.intersected.line");

	/**
	 * The property key.
	 */
	private final String keyString;

	/**
	 * Creates an instance.
	 * @param key The property key.
	 */
	private MapDrawingProperties(final String key) {
		keyString = key;
	}

	/**
	 * @return The key of the property..
	 */
	@Override
    public String key() {
		return keyString;
	}
}
