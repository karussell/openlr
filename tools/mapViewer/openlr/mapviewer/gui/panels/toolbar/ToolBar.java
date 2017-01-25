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

import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JToolBar;

import openlr.geomap.tools.CursorTool;
import openlr.geomap.tools.Measure;
import openlr.geomap.tools.SelectTool;
import openlr.mapviewer.MapChangeObserver;
import openlr.mapviewer.MapsHolder;
import openlr.mapviewer.gui.filechoose.FileChooserFactory;
import openlr.mapviewer.gui.panels.LocationPanel;
import openlr.mapviewer.gui.panels.LocationsFrame;
import openlr.mapviewer.gui.panels.MapTool;
import openlr.mapviewer.gui.panels.MapToolManager;
import openlr.mapviewer.gui.panels.SearchFrame;
import openlr.mapviewer.gui.panels.StatusBar;
import openlr.mapviewer.gui.panels.filtering.FilterFrame;

/**
 * The ToolBar represents the main navigation for the MapViewer. Every action
 * has its own icon and pressing the button starts the corresponding action.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class ToolBar extends JToolBar implements MapChangeObserver {

	/** Serial ID. */
	private static final long serialVersionUID = 7878716026649687301L;

	/** The group of all toggle buttons. */
	private final ButtonGroup group = new ButtonGroup();
	
	/** 
	 * A reference to the location panel
	 */
	private final LocationPanel locationsPanel;
	
    /**
     * All known {@link MapTool}s assigned to their tool-bar buttons
     */
    private static final Map<Class<? extends MapTool>, AbstractButton> KNOWN_MAP_TOOLS_WITH_BUTTONS = new HashMap<Class<? extends MapTool>, AbstractButton>();
    static {
        KNOWN_MAP_TOOLS_WITH_BUTTONS
                .put(SearchFrame.class, GuiButtons.SEARCH.btn);
        KNOWN_MAP_TOOLS_WITH_BUTTONS
                .put(FilterFrame.class, GuiButtons.FILTER.btn);
        KNOWN_MAP_TOOLS_WITH_BUTTONS.put(LocationsFrame.class,
                GuiButtons.LOAD_LOCATION.btn);
    }	

	/**
	 * Creates a new toolbar.
	 *
	 * @param mapsH the maps h
	 * @param locPanel the loc panel
	 * @param fcf the file chooser factory
	 * @param sBar the s bar
	 */
    public ToolBar(final MapsHolder mapsH, final LocationPanel locPanel,
            final FileChooserFactory fcf, final StatusBar sBar) {
		setOrientation(JToolBar.HORIZONTAL);
		setFloatable(false);
		this.locationsPanel = locPanel;
		mapsH.addMapChangeListener(this);  
        ToolBarAction tba = new ToolBarAction(mapsH, locPanel, fcf, sBar);
		for (GuiButtons gbtn : GuiButtons.getGuiButtons()) {
			gbtn.btn.addActionListener(tba);
		}
		add(GuiButtons.FILTER.btn);
		add(GuiButtons.SEARCH.btn);
		add(GuiButtons.LOAD_LOCATION.btn);
		add(GuiButtons.RESET.btn);
		add(GuiButtons.SCREENSHOT.btn);
		addSeparator();
		add(GuiButtons.INFO.btn);
		add(GuiButtons.MEASURE.btn);
		add(GuiButtons.SELECT.btn);

		group.add(GuiButtons.INFO.btn);
		group.add(GuiButtons.MEASURE.btn);
		group.add(GuiButtons.SELECT.btn);
		GuiButtons.INFO.btn.setSelected(true);
	}
	

    /**
     * Updates the tool bar buttons according to the tool state of the changed
     * active map.
     * 
     * @param holder
     *            The map holder providing the map information
     */
    @Override
    public final void update(final MapsHolder holder) {
        MapToolManager toolManager = holder.getMapPane(holder.getMapActive())
                .getToolManager();
        setupToolBarFromState(toolManager);
    }

    /**
     * Enables or disables the tool-bar buttons according to the status in the
     * given tool manager.
     * 
     * @param toolManager
     *            The tool manager containing the state of the tools assigned to
     *            the associated map
     */
    private void setupToolBarFromState(final MapToolManager toolManager) {

        CursorTool cursorTool = toolManager.getActiveCursorTool();
        if (cursorTool == null) {
            GuiButtons.INFO.btn.setSelected(true);
        }

        GuiButtons.MEASURE.btn.setSelected(cursorTool instanceof Measure);

        boolean selectionActive = cursorTool instanceof SelectTool;
        GuiButtons.SELECT.btn.setSelected(selectionActive);

        if (!selectionActive) {
            locationsPanel.setEnabled(false);
        } else {
            locationsPanel.setEnabled(true);
        }

        for (Map.Entry<Class<? extends MapTool>, AbstractButton> toolWithButton : KNOWN_MAP_TOOLS_WITH_BUTTONS
                .entrySet()) {

            Class<? extends MapTool> tool = toolWithButton.getKey();
            AbstractButton button = toolWithButton.getValue();
            button.setSelected(toolManager.getActiveUITool(tool) != null);
        }
    }	
}
