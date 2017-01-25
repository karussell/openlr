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
package openlr.mapviewer.gui.panels.toolbar;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * The class ToolBarConstants provides static data (image file, tooltip text,
 * name, action command) for the toolbar.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public final class ToolBarConstants {
	
	/**
	 * Instantiates a new tool bar constants.
	 */
	private ToolBarConstants() {
		throw new UnsupportedOperationException();
	}
	
	
    
    
    /** *******************  INFO  ******************************. */
    
    public static final String INFO_TOOL_NAME = "Map info";    
    
    /** Tool tip text. */
    public static final String INFO_TOOL_TIP = "Map info";    
    
    /** Icon for the control. */    
    private static final String INFO_ICON_IMAGE = "/png/globe.png";    
    
    /** The Constant INFO_ICON. */
    public static final Icon INFO_ICON = new ImageIcon(ToolBarConstants.class.getResource(INFO_ICON_IMAGE));
    
    /** The Constant INFO_ACTION_COMMAND. */
    public static final String INFO_ACTION_COMMAND = "INFO";
    
    
    
     
    /** *******************  FILTER  ******************************. */
    
	/** The Constant TOOL_NAME. */
	public static final String FILTER_TOOL_NAME = "Filter lines";

	/** Tool tip text. */
	public static final String FILTER_TOOL_TIP = "Filter lines according to FRC and FOW";

	/** Icon for the control. */
	private static final String FILTER_ICON_IMAGE = "/png/server.png";
	
	/** The Constant FILTER_ICON. */
	public static final Icon FILTER_ICON = new ImageIcon(ToolBarConstants.class.getResource(FILTER_ICON_IMAGE));
    
    /** The Constant FILTER_ACTION_COMMAND. */
    public static final String FILTER_ACTION_COMMAND = "FILTER";
    
    
   
    
    
    /** *******************  RESET MAP  ******************************. */
    
	/** Name for this tool. */
	public static final String RESET_MAP_TOOL_NAME = "Reset map";
	
	/** Tool tip text. */
	public static final String RESET_MAP_TOOL_TIP = "Reset map view";
	
	/** Icon for the control. */
	private static final String RESET_MAP_IMAGE = "/png/refresh.png";
	
	/** The Constant RESET_MAP_ICON. */
	public static final Icon RESET_MAP_ICON = new ImageIcon(ToolBarConstants.class.getResource(RESET_MAP_IMAGE));
    
    /** The Constant RESET_MAP_ACTION_COMMAND. */
    public static final String RESET_MAP_ACTION_COMMAND = "RESET";
    
    
    /** *******************  SEARCH  ******************************. */
    
	/** Tool name. */
	public static final String SEARCH_TOOL_NAME = "Search";
	
	/** Tool tip text. */
	public static final String SEARCH_TOOL_TIP = "Search";
	
	/** Icon for the control. */
	private static final String SEARCH_ICON_IMAGE = "/png/search.png";

	/** The Constant SEARCH_ICON. */
	public static final Icon SEARCH_ICON = new ImageIcon(ToolBarConstants.class.getResource(SEARCH_ICON_IMAGE));
    
    /** The Constant SEARCH_ACTION_COMMAND. */
    public static final String SEARCH_ACTION_COMMAND = "SEARCH";
    
    /** *******************  SELECT  ******************************. */
    
	/** Tool tip text. */
    public static final String SELECT_TOOL_TIP = "Select location lines";
    
    /** Icon for the control. */    
    private static final String SELECT_ICON_IMAGE = "/png/edit.png";
    
    /** Tool name. */
    public static final String SELECT_TOOL_NAME = "Select location";
    
    /** The Constant SELECT_ICON. */
    public static final Icon SELECT_ICON = new ImageIcon(ToolBarConstants.class.getResource(SELECT_ICON_IMAGE));
    
    /** The Constant SELECT_ACTION_COMMAND. */
    public static final String SELECT_ACTION_COMMAND = "SELECT";
    
    
    /** *******************  SELECT MAP1 ******************************. */
    
	/** Tool tip text. */
    public static final String SELECT_MAP1_TOOL_TIP = "activate the map 1";
    
    /** Icon for the control. */    
    private static final String SELECT_MAP1_ICON_IMAGE = "/png/edit.png";
    
    /** Tool name. */
    public static final String SELECT_MAP1_TOOL_NAME = "Activate map1";
    
    /** The Constant SELECT_ICON. */
    public static final Icon SELECT_MAP1_ICON = new ImageIcon(ToolBarConstants.class.getResource(SELECT_MAP1_ICON_IMAGE));
    
    /** The Constant SELECT_ACTION_COMMAND. */
    public static final String SELECT_MAP1_ACTION_COMMAND = "SELECT_MAP1";
    
    /** *******************  SELECT MAP2 ******************************. */
    
	/** Tool tip text. */
    public static final String SELECT_MAP2_TOOL_TIP = "activate the map 2";
    
    /** Icon for the control. */    
    private static final String SELECT_MAP2_ICON_IMAGE = "/png/edit.png";
    
    /** Tool name. */
    public static final String SELECT_MAP2_TOOL_NAME = "Activate map2";
    
    /** The Constant SELECT_ICON. */
    public static final Icon SELECT_MAP2_ICON = new ImageIcon(ToolBarConstants.class.getResource(SELECT_MAP2_ICON_IMAGE));
    
    /** The Constant SELECT_ACTION_COMMAND. */
    public static final String SELECT_MAP2_ACTION_COMMAND = "SELECT_MAP2";
    
    
    /** *******************  MEASURE  ******************************. */
    

	/** The Constant TOOL_NAME. */
	public static final String MEASURE_TOOL_NAME = "Measure";

	/** Tool tip text. */
	public static final String MEASURE_TOOL_TIP = "Measure distance between two points";

	/** Icon for the control. */
	private static final String MEASURE_ICON_IMAGE = "/png/applications.png";
	
	/** The Constant MEASURE_ICON. */
	public static final Icon MEASURE_ICON = new ImageIcon(ToolBarConstants.class.getResource(MEASURE_ICON_IMAGE));
    
    /** The Constant MEASURE_ACTION_COMMAND. */
    public static final String MEASURE_ACTION_COMMAND = "MEASURE";
    
    
    /********************* LOAD LOCATION ****************************************/
    
    /** The Constant TOOL_NAME. */
	public static final String LOAD_LOCATION_TOOL_NAME = "Load locations";

	/** Tool tip text. */
	public static final String LOAD_LOCATION_TOOL_TIP = "Load locations from file";

	/** Icon for the control. */
	private static final String LOAD_LOCATION_ICON_IMAGE = "/png/forward.png";
	
	/** The Constant MEASURE_ICON. */
	public static final Icon LOAD_LOCATION_ICON = new ImageIcon(ToolBarConstants.class.getResource(LOAD_LOCATION_ICON_IMAGE));
    
    /** The Constant MEASURE_ACTION_COMMAND. */
    public static final String LOAD_LOCATION_ACTION_COMMAND = "LOAD_LOCATION";
    
    
/** *******************  SCREENSHOT  ******************************. */
    

	/** The Constant TOOL_NAME. */
	public static final String SCREENSHOT_TOOL_NAME = "Screenshot";

	/** Tool tip text. */
	public static final String SCREENSHOT_TOOL_TIP = "Take a screenshot of the map";

	/** Icon for the control. */
	private static final String SCREENSHOT_ICON_IMAGE = "/png/picture.png";
	
	/** The Constant MEASURE_ICON. */
	public static final Icon SCREENSHOT_ICON = new ImageIcon(ToolBarConstants.class.getResource(SCREENSHOT_ICON_IMAGE));
    
    /** The Constant MEASURE_ACTION_COMMAND. */
    public static final String SCREENSHOT_ACTION_COMMAND = "SCREENSHOT";
    
    /** *******************  GOOGLE MAPS  ******************************. */
    
    /** The Constant TOOL_NAME. */
    public static final String GOOGLE_MAPS_TOOL_NAME = "Google Maps";
    
    /** Tool tip text. */
    public static final String GOOGLE_MAPS_TOOL_TIP = "Show current location in google maps.";
    
    /** Icon for the control. */
    private static final String GOOGLE_MAPS_ICON_IMAGE = "/png/gmaps.png";
    
    /** The Constant MEASURE_ICON. */
    public static final Icon GOOGLE_MAPS_ICON = new ImageIcon(ToolBarConstants.class.getResource(GOOGLE_MAPS_ICON_IMAGE));
    
    /** The Constant MEASURE_ACTION_COMMAND. */
    public static final String GOOGLE_MAPS_ACTION_COMMAND = "GOOGLE_MAPS";
    	
}
