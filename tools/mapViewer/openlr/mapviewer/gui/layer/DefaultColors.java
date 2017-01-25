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
package openlr.mapviewer.gui.layer;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public final class DefaultColors {
	
	/**
	 * Utility class shall not be instantiated
	 */
	private DefaultColors() {	
		// http://www.colorhunter.com/tag/FF00FF/2
		throw new UnsupportedOperationException();
	}
	
	/** The Constant SCALE_COLOR. */
	public static final Color SCALE_COLOR = Color.red;
	
	/** The Constant MEASURE_COLOR. */
	public static final Color MEASURE_COLOR = Color.red;
	
	/** The Constant SELECTED_LOCATION. */
	public static final Color SELECTED_LOCATION = Color.magenta;
	
	/** The Constant SEARCH_COLOR. */
	public static final Color SEARCH_COLOR = Color.red;
	
	/** The Constant DEFAULT_LINE_COLOR. */
	public static final Color DEFAULT_LINE_COLOR = Color.black;
	
	/** The Constant START_NODE_COLOR. */
	public static final Color START_NODE_COLOR = Color.blue;
	
	/** The Constant POINT_COLOR. */
	public static final Color NODE_COLOR = Color.gray;
	
	/** The Constant LOC_START_NODE_COLOR. */
	public static final Color LOC_START_NODE_COLOR = Color.blue;
	
	/** The Constant LOC_GEO_COORD_COLOR. */
	public static final Color LOC_GEO_COORD_COLOR = Color.red;
	
	/** The Constant LOC_POS_OFF_COLOR. */
	public static final Color LOC_POS_OFF_COLOR = Color.cyan;
	
	/** The Constant LOC_NEG_OFF_COLOR. */
	public static final Color LOC_NEG_OFF_COLOR = Color.orange;
	
	/** The Constant LOC_ACCESS_COLOR. */
	public static final Color LOC_ACCESS_COLOR = Color.cyan;
	
	/** The Constant HIGHLIGHT_LINE_COLOR. */
	public static final Color HIGHLIGHT_LINE_COLOR = Color.red;
	
	/** The Constant ALL_LOCATIONS_COLOR. */
	public static final Color ALL_LOCATIONS_COLOR = Color.yellow;
	
	/** The Constant ENCODER_LOCATION_COLOR. */
	public static final Color ENCODER_LOCATION_COLOR = Color.magenta;
	
	/** The Constant DECODER_LOCATION_COLOR. */
	public static final Color DECODER_LOCATION_COLOR = Color.green;
	
	/** The Constant STORED_LOCATION_COLOR. */
	public static final Color STORED_LOCATION_COLOR = Color.pink;
	
	/** The Constant COVERED_LINE_AREA_LOCATION_COLOR. */
	public static final Color COVERED_LINE_AREA_LOCATION_COLOR = Color.blue;
	
	/** The Constant COVERED_LINE_AREA_LOCATION_COLOR. */
	public static final Color INTERSECTED_LINE_AREA_LOCATION_COLOR = Color.red;

	/**
	 * MapDrawingProperties depending on the functional road class.
	 */
	public enum FRCColors {

		/** The FRC0. */
		FRC0(Color.red),

		/** The FRC1. */
		FRC1(Color.blue),

		/** The FRC2. */
		FRC2(Color.cyan),

		/** The FRC3. */
		FRC3(Color.gray),

		/** The FRC4. */
		FRC4(Color.green),

		/** The FRC5. */
		FRC5(Color.yellow),

		/** The FRC6. */
		FRC6(Color.magenta),

		/** The FRC7. */
		FRC7(Color.lightGray);

		/** The color. */
		private final Color color;
		
		/** The Constant VALUES. */
		private static final List<FRCColors> VALUES = Collections.unmodifiableList(Arrays.asList(values()));

		/**
		 * Instantiates a new FRC color value.
		 * 
		 * @param c
		 *            the color
		 */
		FRCColors(final Color c) {
			color = c;
		}

		/**
		 * Gets the color.
		 * 
		 * @return the color
		 */
		public Color getColor() {
			return color;
		}
		
		/**
		 * Gets the fRC colors.
		 *
		 * @return the fRC colors
		 */
		public static List<FRCColors> getFRCColors() {
			return VALUES;
		}
	}

	/**
	 * The enumeration of FOWColors.
	 */
	public enum FOWColors {

		/** The UNDEFINED. */
		UNDEFINED(Color.red),

		/** The FOW1. */
		MOTORWAY(Color.blue),

		/** The FOW2. */
		MULTIPLE_CARRIAGEWAY(Color.cyan),

		/** The FOW3. */
		SINGLE_CARRIAGEWAY(Color.gray),

		/** The FOW4. */
		ROUNDABOUT(Color.green),

		/** The FOW5. */
		TRAFFIC_SQUARE(Color.yellow),

		/** The FOW6. */
		SLIPROAD(Color.magenta),

		/** The FOW7. */
		OTHER(Color.lightGray);

		/** The color. */
		private final Color color;
		
		/** The Constant VALUES. */
		private static final List<FOWColors> VALUES = Collections.unmodifiableList(Arrays.asList(values()));

		/**
		 * Instantiates a new FOW colors.
		 * 
		 * @param c
		 *            the c
		 */
		FOWColors(final Color c) {
			color = c;
		}

		/**
		 * Gets the color.
		 * 
		 * @return the color
		 */
		public Color getColor() {
			return color;
		}
		
		/**
		 * Gets the fOW colors.
		 *
		 * @return the fOW colors
		 */
		public static List<FOWColors> getFOWColors() {
			return VALUES;
		}
	}


}
