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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JToggleButton;

/**
 * The enum GuiButtons holds all buttons in the toolbar.
 */
enum GuiButtons {

	/** The INFO. */
	INFO(ToolBarConstants.INFO_TOOL_NAME,
			ToolBarConstants.INFO_ICON,
			true,
			ToolBarConstants.INFO_ACTION_COMMAND,
			ToolBarConstants.INFO_TOOL_TIP),

	/** The FILTER. */
	FILTER(ToolBarConstants.FILTER_TOOL_NAME,
			ToolBarConstants.FILTER_ICON,
			true,
			ToolBarConstants.FILTER_ACTION_COMMAND,
			ToolBarConstants.FILTER_TOOL_TIP),

	/** The SEARCH. */
	SEARCH(ToolBarConstants.SEARCH_TOOL_NAME,
			ToolBarConstants.SEARCH_ICON,
			true,
			ToolBarConstants.SEARCH_ACTION_COMMAND,
			ToolBarConstants.SEARCH_TOOL_TIP),

	/** The LOAD_LOCATION. */
	LOAD_LOCATION(ToolBarConstants.LOAD_LOCATION_TOOL_NAME,
			ToolBarConstants.LOAD_LOCATION_ICON,
			true,
			ToolBarConstants.LOAD_LOCATION_ACTION_COMMAND,
			ToolBarConstants.LOAD_LOCATION_TOOL_TIP),

	/** The MEASURE. */
	MEASURE(ToolBarConstants.MEASURE_TOOL_NAME,
			ToolBarConstants.MEASURE_ICON,
			true,
			ToolBarConstants.MEASURE_ACTION_COMMAND,
			ToolBarConstants.MEASURE_TOOL_TIP),

	/** The RESET. */
	RESET(ToolBarConstants.RESET_MAP_TOOL_NAME,
			ToolBarConstants.RESET_MAP_ICON,
			false,
			ToolBarConstants.RESET_MAP_ACTION_COMMAND,
			ToolBarConstants.RESET_MAP_TOOL_TIP),

	/** The SELECT. */
	SELECT(ToolBarConstants.SELECT_TOOL_NAME, ToolBarConstants.SELECT_ICON,
			true, ToolBarConstants.SELECT_ACTION_COMMAND,
			ToolBarConstants.SELECT_TOOL_TIP),

			
	/** The SCREENSHOT. */
	SCREENSHOT(ToolBarConstants.SCREENSHOT_TOOL_NAME,
			ToolBarConstants.SCREENSHOT_ICON,
			false,
			ToolBarConstants.SCREENSHOT_ACTION_COMMAND,
			ToolBarConstants.SCREENSHOT_TOOL_TIP);
			
	

	/** The Constant BNT_FONT_SIZE. */
	private static final float BNT_FONT_SIZE = 10.0f;

	/** The btn. */
	protected final AbstractButton btn;
	
	/** The Constant VALUES. */
	private static final List<GuiButtons> VALUES = Collections.unmodifiableList(Arrays.asList(values()));

	/**
	 * Instantiates a new gui buttons.
	 * 
	 * @param name
	 *            the button name
	 * @param icon
	 *            the icon
	 * @param toggle
	 *            if true it is a toggle button
	 * @param command
	 *            the action command
	 * @param tt
	 *            the tool tip
	 */
	GuiButtons(final String name, final Icon icon, final boolean toggle,
			final String command, final String tt) {
		if (toggle) {
			btn = new JToggleButton(name, icon);
		} else {
			btn = new JButton(name, icon);
		}
		btn.setActionCommand(command);
		btn.setToolTipText(tt);
		btn.setFont(btn.getFont().deriveFont(BNT_FONT_SIZE));
		btn.setVerticalTextPosition(ToolBar.BOTTOM);
		btn.setHorizontalTextPosition(ToolBar.CENTER);
	}
	
	/**
	 * Gets the gui buttons.
	 *
	 * @return the gui buttons
	 */
	public static List<GuiButtons> getGuiButtons() {
		return VALUES;
	}
}
