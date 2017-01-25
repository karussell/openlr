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
package openlr.mapviewer.gui;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import openlr.mapviewer.MapViewerException;
import openlr.mapviewer.MapsHolder;
import openlr.mapviewer.coding.CodingPropertiesHolder;
import openlr.mapviewer.gui.filechoose.DirectoryRememberingFileChooserFactory;
import openlr.mapviewer.gui.filechoose.FileChooserFactory;
import openlr.mapviewer.gui.panels.LocationPanel;
import openlr.mapviewer.gui.panels.LocationReferencePanel;
import openlr.mapviewer.gui.panels.MapPanelsHolder;
import openlr.mapviewer.gui.panels.StatusBar;
import openlr.mapviewer.gui.panels.toolbar.ToolBar;
import openlr.mapviewer.gui.tools.MouseLineInfo;
import openlr.mapviewer.gui.tools.OpenLRMapPaneKeyDispatcher;
import openlr.properties.OpenLRPropertiesReader;
import openlr.properties.OpenLRPropertyException;

import org.apache.commons.configuration.FileConfiguration;

/**
 * The MapViewerGui is the main gui for the map viewer application. It comprises
 * a toolbar, the map pane, location and line information panels, and a status
 * bar.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class MapViewerGui extends JFrame {

	/** Serial ID. */
	private static final long serialVersionUID = 5793317510283777901L;

	/** The Constant ICON_URL. */
	private static final URL ICON_URL = MapViewerGui.class
			.getResource("/OpenLR-icon.gif");

	/** The Constant OPENLR_ICON. */
	public static final Image OPENLR_ICON = new ImageIcon(ICON_URL).getImage();

	/** Width of the window. */
	private static final int WIDTH = 970;

	/** Height of the window. */
	private static final int HEIGHT = 800;

	/** The Constant WAIT_CURSOR. */
	public static final Cursor WAIT_CURSOR = new Cursor(Cursor.WAIT_CURSOR);

	    /**
     * Creates a new map viewer gui and initializes all components.
     * 
     * @param mapHolder
     *            the map holder
     * @throws MapViewerException
     *             If an error occurs when setting up the parameters for the UI
     *             elements
     */
	public MapViewerGui(final MapsHolder mapHolder) throws MapViewerException {
		// 1. add Menubar
	    FileChooserFactory fcf = new DirectoryRememberingFileChooserFactory();
	    
	    CodingPropertiesHolder codingPropertiesHolder = createCodingPropertiesHolder();
		setJMenuBar(new MapViewerMenuBar(this, codingPropertiesHolder, fcf));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("OpenLR - Map Viewer");
		setSize(WIDTH, HEIGHT);

		StatusBar statusBar = new StatusBar();

        LocationPanel locPanel = new LocationPanel(mapHolder, codingPropertiesHolder);
		locPanel.setFileChooserFactory(fcf);
		
		// trigger the shutdown manager when the application window is closed
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(final WindowEvent e) {
                ShutDownManager.getInstance().statShutDown();
            }
        });		

		// 2. add Toolbar
        ToolBar toolBar = new ToolBar(mapHolder, locPanel, fcf, statusBar);

		prepareMouseListener(mapHolder, statusBar);

		JPanel panel = new JPanel(new MigLayout("insets 0", "[][grow]",
				"[][grow][grow][]"));
		panel.add(toolBar, "north, span");

		// +++++ 1. ADD locationPanel TO PANEL ++++++
		panel.add(locPanel, "grow");

		// add map panels
		JPanel mapsPanel = new MapPanelsHolder(mapHolder);
		mapsPanel.setSize(WIDTH, HEIGHT);
		panel.add(mapsPanel, "grow, wrap, span 1 3");

        LocationReferencePanel esp = new LocationReferencePanel(mapHolder,
                statusBar, codingPropertiesHolder);
		esp.setFileChooserFactory(fcf);

		// +++++ 3. ADD LocationReferencePanel TO PANEL ++++++
		panel.add(esp, "growx");
		panel.add(statusBar, "south, span");
		getContentPane().add(panel);

		if (ICON_URL != null) {
			ImageIcon im = new ImageIcon(ICON_URL);
			setIconImage(im.getImage());
		}

		setLocationRelativeTo(null);
	}

    /**
     * Prepare mouse listener.
     * 
     * @param mapHolder
     *            the map holder
     * @param sbar
     *            A reference to the status bar
     */
    private void prepareMouseListener(final MapsHolder mapHolder,
            final StatusBar sbar) {
        MouseLineInfo mli = new MouseLineInfo(mapHolder,
                MapsHolder.MapIndex.FIRST_MAP, sbar);
        mapHolder.getMapPane(MapsHolder.MapIndex.FIRST_MAP).getToolManager()
                .addMapMouseListener(mli);
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(
                        new OpenLRMapPaneKeyDispatcher(mli, this));
        if (mapHolder.hasTwoMaps()) {
            MouseLineInfo mli2 = new MouseLineInfo(mapHolder,
                    MapsHolder.MapIndex.SECOND_MAP, sbar);
            mapHolder.getMapPane(MapsHolder.MapIndex.SECOND_MAP)
                    .getToolManager().addMapMouseListener(mli2);
            KeyboardFocusManager.getCurrentKeyboardFocusManager()
                    .addKeyEventDispatcher(
                            new OpenLRMapPaneKeyDispatcher(mli2, this));
        }
    }

	/**
	 * Sets the application cursor.
	 * 
	 * @param requester
	 *            the requester
	 * @param cursor
	 *            the cursor
	 */
	public static void setApplicationCursor(final Container requester,
			final Cursor cursor) {
		Container current = requester;
		while (current.getParent() != null) {
			current = current.getParent();
		}
		current.setCursor(cursor);
	}

	/**
	 * Gets the application cursor.
	 * 
	 * @param requester
	 *            the requester
	 * @return the application cursor
	 */
	public static Cursor getApplicationCursor(final Container requester) {
		Container current = requester;
		while (current.getParent() != null) {
			current = current.getParent();
		}
		return current.getCursor();
	}

    /**
     * Sets up the coding properties holder
     * @return The set up coding properties holder
     * @throws MapViewerException
     *             If an error occurs reading the default properties from the
     *             class-path
     */
    private CodingPropertiesHolder createCodingPropertiesHolder()
            throws MapViewerException {

        try {
            FileConfiguration decoderProps = OpenLRPropertiesReader
                    .loadPropertiesFromStream(
                            Thread.currentThread()
                                    .getContextClassLoader()
                                    .getResourceAsStream(
                                            CodingPropertiesHolder.CodingType.DECODING
                                                    .getDefaultPropertiesPath()),
                            true);
            FileConfiguration encoderProps = OpenLRPropertiesReader
                    .loadPropertiesFromStream(
                            Thread.currentThread()
                                    .getContextClassLoader()
                                    .getResourceAsStream(
                                            CodingPropertiesHolder.CodingType.ENCODING
                                                    .getDefaultPropertiesPath()),
                            true);
            return new CodingPropertiesHolder(encoderProps, decoderProps);
        } catch (OpenLRPropertyException e) {
            throw new MapViewerException(
                    "Error reading the OpenLR encding and decoding properties from classpath",
                    e);
        }
    }
}
