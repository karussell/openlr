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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import openlr.geomap.tools.Measure;
import openlr.geomap.tools.SelectTool;
import openlr.mapviewer.MapViewer;
import openlr.mapviewer.MapsHolder;
import openlr.mapviewer.MapsHolder.MapIndex;
import openlr.mapviewer.gui.filechoose.FileChooserFactory;
import openlr.mapviewer.gui.panels.LocationPanel;
import openlr.mapviewer.gui.panels.LocationsFrame;
import openlr.mapviewer.gui.panels.MapTool;
import openlr.mapviewer.gui.panels.MapToolManager;
import openlr.mapviewer.gui.panels.SearchFrame;
import openlr.mapviewer.gui.panels.StatusBar;
import openlr.mapviewer.gui.panels.filtering.FilterFrame;

/**
 * The ToolBarAction handles all actions after pressing a button in the toolbar.
 */
final class ToolBarAction implements ActionListener {


    /** The parent. */
	private final MapsHolder mapsHolder;
	
	/**
	 * A reference to the status bar to print information to it
	 */
	private final StatusBar statusBar;

	/** The loc panel. */
	private final LocationPanel locPanel;

	/**
	 * The file chooser factory.
	 */
	private final FileChooserFactory fileChooserFactory;

	/**
	 * Instantiates a new tool bar action.
	 *
	 * @param mapsH the maps h
	 * @param lp the lp
	 * @param fcf the file chooser factory
	 * @param sBar the s bar
	 */
	ToolBarAction(final MapsHolder mapsH, final LocationPanel lp,
			final FileChooserFactory fcf, 
			final StatusBar sBar) {

		mapsHolder = mapsH;
		statusBar = sBar;
		fileChooserFactory = fcf;
		locPanel = lp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void actionPerformed(final ActionEvent e) {
		String command = e.getActionCommand();
		MapIndex active = mapsHolder.getMapActive();

        MapToolManager activeToolManager = mapsHolder.getMapPane(active)
                .getToolManager();
        AbstractButton button = (AbstractButton) e.getSource();
        boolean selected = button.isSelected();
        
        // 
        if (!GuiButtons.SELECT.btn.isSelected()) {
            // TODO could be triggered by a CloseAction for CursorTools
            locPanel.setEnabled(false);
        }
        
        if (ToolBarConstants.INFO_ACTION_COMMAND.equals(command)) {
			// reset layers and tools
			activeToolManager.setCursorTool(null);
            
		} else if (ToolBarConstants.SCREENSHOT_ACTION_COMMAND.equals(command)) {
			JFileChooser jfc = fileChooserFactory
					.createFileChooser("SCREENSHOT");
			jfc.setDialogTitle("Save map screenshot to ...");
			jfc.setDialogType(JFileChooser.SAVE_DIALOG);
			int retValue = jfc.showSaveDialog(null);
			if (retValue == JFileChooser.APPROVE_OPTION) {
				File f = jfc.getSelectedFile();
				if (!f.getName().endsWith(".png")) {
					f = new File(f.getAbsolutePath() + ".png");
				}
                try {
                    mapsHolder.getMapPane(active).saveImage(f);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Error saving screenshot. Details: " + ex, "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
			}
		} else if (ToolBarConstants.FILTER_ACTION_COMMAND.equals(command)) {
		    
            if (selected) {
                startFilterTool(mapsHolder);
            } else {
                activeToolManager.removeUITool(FilterFrame.class);
            }

		} else if (ToolBarConstants.RESET_MAP_ACTION_COMMAND.equals(command)) {
			
			mapsHolder.getMapPane(active).resetMapView();
			
		} else if (ToolBarConstants.SEARCH_ACTION_COMMAND.equals(command)) {

            if (selected) {
                startSearchTool(mapsHolder);
            } else {
                activeToolManager.removeUITool(SearchFrame.class);
            }
			
		} else if (ToolBarConstants.SELECT_ACTION_COMMAND.equals(command)) {
		    
            SelectTool selectTool = new SelectTool();
            activeToolManager.setCursorTool(selectTool);
            
            locPanel.setSelectTool(selectTool);
            locPanel.setEnabled(true);

		} else if (ToolBarConstants.MEASURE_ACTION_COMMAND.equals(command)
				&& GuiButtons.MEASURE.btn.isSelected()) {

            activeToolManager
                    .setCursorTool(
                            new Measure(new MeasureStatusPrinter(statusBar),
                                    MapViewer.PROPERTIES
                                            .getMapPaneMeasureColor()));
            
		} else if (ToolBarConstants.LOAD_LOCATION_ACTION_COMMAND
				.equals(command)) {

            if (selected) {
                startLoadLocationsTool(mapsHolder);
            } else {
                activeToolManager.removeUITool(LocationsFrame.class);
            }
        }
	}

    /**
     * Creates a new filter frame for the active map.
     * 
     * @param mHolder
     *            The map data holder
     */
    private void startFilterTool(final MapsHolder mHolder) {

        MapIndex index = mHolder.getMapActive();
        String mapNamePrefix = (index.ordinal() + 1) + ": ";
        FilterFrame filterFrame = new FilterFrame(
                mHolder.getMapLayerStore(index), mapNamePrefix
                        + mHolder.getMapName(index));
        
        setupMapTool(filterFrame, GuiButtons.FILTER.btn);
    }
    
    /**
     * Creates a new search frame for the active map.
     * 
     * @param mHolder
     *            The map data holder
     */
    private void startSearchTool(final MapsHolder mHolder) {

        MapIndex index = mHolder.getMapActive();
        SearchFrame searchFrame = new SearchFrame(
                mHolder.getMapLayerStore(index), mHolder.getMapDatabase(index),
                mapsHolder.getMapPane(index));
        setupMapTool(searchFrame, GuiButtons.SEARCH.btn);
    }

    /**
     * Creates a new search frame for the given map or delivers the stored one
     * if it was already created before.
     * 
     * @param mHolder
     *            The map data holder
     */
    private void startLoadLocationsTool(final MapsHolder mHolder) {

        MapIndex index = mHolder.getMapActive();
        LocationsFrame locationsDialog = new LocationsFrame(
                mHolder.getLocationHolder(index), mHolder, index,
                fileChooserFactory, statusBar);
        setupMapTool(locationsDialog, GuiButtons.LOAD_LOCATION.btn);
    }

    /**
     * Performs all setup task to instantiate a {@link MapTool}.
     * 
     * @param tool
     *            The tools instance to setup
     * @param button
     *            The related tool-bar button
     */
    private void setupMapTool(final MapTool tool, final AbstractButton button) {

        MapIndex index = mapsHolder.getMapActive();

        MapToolManager toolManager = mapsHolder.getMapPane(index).getToolManager();
        DeToggleButtonOnToolCloseAction unsetAction = new DeToggleButtonOnToolCloseAction(
                mapsHolder, index, button);
        tool.addCloseAction(unsetAction);
        toolManager.addUITool(tool);
    }   

}
