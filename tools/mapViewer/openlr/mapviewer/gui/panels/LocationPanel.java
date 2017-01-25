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
package openlr.mapviewer.gui.panels;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;
import openlr.LocationType;
import openlr.geomap.tools.CursorTool;
import openlr.geomap.tools.SelectTool;
import openlr.map.InvalidMapDataException;
import openlr.mapviewer.MapChangeObserver;
import openlr.mapviewer.MapViewer;
import openlr.mapviewer.MapViewerException;
import openlr.mapviewer.MapsHolder;
import openlr.mapviewer.MapsHolder.MapIndex;
import openlr.mapviewer.coding.CodingPropertiesHolder;
import openlr.mapviewer.coding.CodingPropertiesHolder.CodingType;
import openlr.mapviewer.gmaps.GoogleMapsStarter;
import openlr.mapviewer.gui.MapViewerGui;
import openlr.mapviewer.gui.filechoose.DummyFileChooserFactory;
import openlr.mapviewer.gui.filechoose.FileChooserFactory;
import openlr.mapviewer.gui.panels.locationSelection.LocationTypePanel;
import openlr.mapviewer.gui.panels.locationSelection.OrientationPanel;
import openlr.mapviewer.gui.panels.locationSelection.SelectionTypePanel;
import openlr.mapviewer.gui.panels.locationSelection.SideOfRoadPanel;
import openlr.mapviewer.location.LocationChangeObserver;
import openlr.mapviewer.location.LocationCodingException;
import openlr.mapviewer.location.LocationHolder;
import openlr.mapviewer.location.LocationIncompleteException;
import openlr.mapviewer.properties.OtherProperties;

/**
 * The LocationPanel shows the currently selected (or decoded) location data. It
 * also provides the possibility to encode or clear the location.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class LocationPanel extends JPanel implements LocationChangeObserver {

	/** Serial ID. */
	private static final long serialVersionUID = -7018009255318443184L;

	/** The Constant VISIBLE_ROWS. */
	private static final int VISIBLE_ROWS = 17;

	/** The Constant VISIBLE_COLUMNS. */
	private static final int VISIBLE_COLUMNS = 300;

	/** The Constant ENCODE_BTN. */
	private static final JButton ENCODE_BTN = new JButton("Encode");
	
	/** The Constant ENCODE_BTN. */
	private static final JButton GOOGLE_BTN = new JButton("GoogleMaps");

	/** The Constant ENCODE_BTN. */
	private static final JButton CLEAR_BTN = new JButton("Clear");

	/** The Constant SAVE_BTN. */
	private static final JButton SAVE_BTN = new JButton("Save");

	/** The loc type panel. */
	private final LocationTypePanel locTypePanel;

	/** The select type panel. */
	private final SelectionTypePanel selectTypePanel;

	/** The side of road panel. */
	private final SideOfRoadPanel sideOfRoadPanel;

	/** The list. */
	private final JList dataList;

	/** The orientation panel. */
	private final OrientationPanel orientationPanel;

	/**
	 * A reference to the location panel action.
	 */
	private final transient LocationPanelAction lpa;
	
    /**
     * A reference to the currently assigned location holder providing the state
     * of the selection
     */
    private LocationHolder currentState;

	/**
	 * Creates a new location panel.
	 * @param mapsH
	 *            the maps holder
	 * @param encoderPropertiesHolder     
     *          The provider of the central references to the OpenLR encoder
     *          settings
	 */
    public LocationPanel(final MapsHolder mapsH, final CodingPropertiesHolder encoderPropertiesHolder) {
		super(new MigLayout("insets 1"));
		currentState = mapsH.getLocationHolder(mapsH.getMapActive());
		currentState.addLocationChangeObserver(this);
		
		setBorder(BorderFactory.createTitledBorder("Location"));
		sideOfRoadPanel = new SideOfRoadPanel(currentState);
		orientationPanel = new OrientationPanel(currentState);
		selectTypePanel = new SelectionTypePanel(currentState);

		locTypePanel = new LocationTypePanel(currentState);
		add(locTypePanel, "wrap, span 4 1");
		add(selectTypePanel, "wrap, span 4 1");

		JScrollPane scroll = new JScrollPane();
		dataList = new JList(new DefaultListModel());
		dataList.setVisibleRowCount(VISIBLE_ROWS);
		dataList.setFixedCellWidth(VISIBLE_COLUMNS);
		scroll.setViewportView(dataList);
		add(scroll, "grow, wrap, span 4 1");
		add(sideOfRoadPanel, "wrap, span 4 1");
		add(orientationPanel, "wrap, span 4 1");
		lpa = new LocationPanelAction(this, encoderPropertiesHolder);
		ENCODE_BTN.addActionListener(lpa);
		CLEAR_BTN.addActionListener(lpa);
		SAVE_BTN.addActionListener(lpa);
		GOOGLE_BTN.addActionListener(lpa);
		ENCODE_BTN.setActionCommand("encode");
		CLEAR_BTN.setActionCommand("clear");
		SAVE_BTN.setActionCommand("save");
		GOOGLE_BTN.setActionCommand("google");
		add(ENCODE_BTN);
		add(CLEAR_BTN);
		add(SAVE_BTN);
		add(GOOGLE_BTN);
		ENCODE_BTN.setEnabled(false);
		CLEAR_BTN.setEnabled(false);
		SAVE_BTN.setEnabled(false);
		GOOGLE_BTN.setEnabled(false);
		
		setEnabled(false);
		
		mapsH.addMapChangeListener(new UpdateStateOnMapChangeObserver());
	}

	/**
	 * Sets a {@link FileChooserFactory} and so the ability to create file
	 * chooser dialogs in the functionalities of this UI class that start in the
	 * last directory after repeated opening.
	 * 
	 * @param fcf
	 *            The file chooser factory
	 */
	public final void setFileChooserFactory(final FileChooserFactory fcf) {
		lpa.setFileChooserFactory(fcf);
	}

	/**
	 * The LocationPanelAction deals with all action within the LocationPanel.
	 */
	private final class LocationPanelAction implements ActionListener {
		
		private final JPanel panel;
        /**
         * The provider of the central references to the OpenLR encoder settings
         */
		private final CodingPropertiesHolder encoderProperties;

		/**
		 * The file chooser factory.
		 */
		private FileChooserFactory fileChooserFactory = new DummyFileChooserFactory();

		/**
		 * Instantiates a new location panel action.
		 *
		 * @param p the p
         * @param encoderPropertiesHolder     
         *          The provider of the central references to the OpenLR encoder
         *          settings
		 */
        LocationPanelAction(final JPanel p,
                final CodingPropertiesHolder encoderPropertiesHolder) {
			panel = p;
			encoderProperties = encoderPropertiesHolder;
		}

		/**
		 * Sets a {@link FileChooserFactory}
		 * 
		 * @param fcf
		 *            The file chooser factory
		 */
		private void setFileChooserFactory(final FileChooserFactory fcf) {
			fileChooserFactory = fcf;
		}

		/**
		 * Action performed.
		 * 
		 * @param e
		 *            the e {@inheritDoc}
		 */
		@Override
		public void actionPerformed(final ActionEvent e) {
			String command = e.getActionCommand();

            if ("clear".equals(command)) {
				currentState.clearLocation();
			} else if ("encode".equals(command)) {
                performEncode();
			} else if ("save".equals(command)) {
				saveLocation();
			} else if ("google".equals(command)) {
				performGoogleMapsAction();
			}
		}

		/**
		 * Runs the encoding action.
		 */
        private void performEncode() {
            
            Cursor oldC = MapViewerGui.getApplicationCursor(panel);
            try {
              MapViewerGui.setApplicationCursor(panel, MapViewerGui.WAIT_CURSOR);

                StringWriter logReceiver = getLogReceiverIfEnabled();
                try {
                    currentState.encodeLocation(encoderProperties
                            .getProperties(CodingType.ENCODING),
                            logReceiver);

                    showLogIfEnabled(logReceiver);

                } catch (LocationCodingException e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(),
                            "Encoding error", JOptionPane.ERROR_MESSAGE);
                }

            } finally {
              MapViewerGui.setApplicationCursor(panel, oldC);
            }
        }
		
        /**
         * Delivers a {@link StringWriter} if feature of displaying the encoder
         * log is enabled.
         * 
         * @return The string writer acting as a log output receiver
         */
        private StringWriter getLogReceiverIfEnabled() {
            StringWriter logReceiver = null;
            if (Boolean.parseBoolean(MapViewer.PROPERTIES
                    .getProperty(OtherProperties.SHOW_ENCODER_LOG))) {
                logReceiver = new StringWriter();
            }
            return logReceiver;
        }
        
        /**
         * Activates the output of the encoder log if the given receiver is not
         * {@code null}. Prints out all the content of {@code logReceiver} then
         * and closes it.
         * 
         * @param logReceiver
         *            The log receiving writer
         */
        private void showLogIfEnabled(final StringWriter logReceiver) {
            if (logReceiver != null) {

                CodingLogFrame logOutput = new CodingLogFrame(
                        "Encoder log information", logReceiver.toString());
                logOutput.setVisible(true);
            }
        }        
        
		/**
		 * Executes opening the current location in Google Maps.
		 */
        private void performGoogleMapsAction() {
            try {

                try {
                    new GoogleMapsStarter().show(currentState
                            .createLocation("MapViewer location"));
                } catch (MapViewerException e1) {
                    JOptionPane.showMessageDialog(null,
                            "<html>Error opening the location in Google Maps:<br>"
                                    + e1.getMessage() + "</html>", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (InvalidMapDataException e) {
                JOptionPane.showMessageDialog(null,
                        "<html>Error opening the location in Google Maps:<br>"
                                + e.getMessage() + "</html>", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (LocationIncompleteException e) {
                JOptionPane
                .showMessageDialog(
                        null,
                        "Location data is not complete! "
                                + e.getMessage(), "Location error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

		/**
		 * Ask for file to save.
		 * 
		 * @return the file writer
		 */
		private FileWriter askForFileToSave() {
			JFileChooser jfc = fileChooserFactory
					.createFileChooser(FileChooserFactory.TOPIC_LOCATION);
			FileWriter fw = null;
			int re = jfc.showDialog(null, "Select locations file");
			if (re == JFileChooser.APPROVE_OPTION) {
				File file = jfc.getSelectedFile();
				if (file.exists()) {
					String[] options = { "Append", "Overwrite" };
					int retValue = JOptionPane
							.showOptionDialog(
									null,
									"A file with that name already exists. Do you want to append the location to the existing file or overwrite the existing file?",
									"File already exists",
									JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE, null,
									options, options[0]);
					if (retValue == 0) { // append
						try {
							fw = new FileWriter(file, true);
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(null,
									"Cannot open file!", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					} else if (retValue == 1) { // overwrite
						try {
							if (!file.delete()) {
								JOptionPane.showMessageDialog(null,
										"Cannot delete old file!", "Error",
										JOptionPane.ERROR_MESSAGE);
							} else {
								if (file.createNewFile()) {
									fw = new FileWriter(file);
								}
							}
						} catch (IOException ioe) {
							JOptionPane.showMessageDialog(null,
									"Cannot create new file!", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				} else {
					try {
						if (file.createNewFile()) {
							fw = new FileWriter(file);
						}
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null,
								"Cannot create new file!", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			return fw;
		}

		/**
		 * Save location.
		 */
		private void saveLocation() {
			String id = JOptionPane.showInputDialog(null,
					"Please enter the location ID");
			if (id != null) {
				FileWriter fw = askForFileToSave();
				if (fw != null) {
					try {
						String s = currentState.createLocationString(id);
						fw.write(s);
						fw.flush();
						fw.close();
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null,
								"Cannot write to file!", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
	}

	/**
	 * Update the status to the data contained in the given location holder.
	 * 
	 * @param locationHolder
	 *            the location holder
	 */
	@Override
	public final void update(final LocationHolder locationHolder) {
	    // only update to changes in the corresponding holder!
        if (currentState.equals(locationHolder)) {

            setState(currentState);
        } 
	}
	
    /**
     * Starts setup of the location input UI chain to adapt to state defined by
     * the given location holder.
     * 
     * @param locH
     *            The location holder
     */
    private void setState(final LocationHolder locH) {
        
        currentState = locH;

        LocationType locationType = locH.getLocationType();
        
        dataList.setListData(locH.getLocationInfo().toArray());

        boolean anyDataIsSet = locationType != null;      
        // we only enable the sub-elements if the panel is enabled at all
        boolean enabledAtAll = isEnabled();
        
        ENCODE_BTN.setEnabled(enabledAtAll && anyDataIsSet);
        CLEAR_BTN.setEnabled(enabledAtAll && anyDataIsSet);
        SAVE_BTN.setEnabled(enabledAtAll && anyDataIsSet);
        GOOGLE_BTN.setEnabled(enabledAtAll && anyDataIsSet);

        locTypePanel.setState(locH);       
        selectTypePanel.setState(locH);       
        sideOfRoadPanel.setState(locH);
        orientationPanel.setState(locH);
    }
    
    /**
     * Sets the select tool.
     *
     * @param selectTool the new select tool
     */
    public final void setSelectTool(final SelectTool selectTool) {
        selectTool.setSelectionProcessor(selectTypePanel);
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public final void setEnabled(final boolean enabled) {
        
        // the type is the very first datum that is set
        boolean alreadyData = currentState.getLocationType() != null;

        // only activate these buttons if there is already data
        ENCODE_BTN.setEnabled(enabled & alreadyData);
        SAVE_BTN.setEnabled(enabled & alreadyData);
        GOOGLE_BTN.setEnabled(enabled & alreadyData);
        CLEAR_BTN.setEnabled(enabled & alreadyData);
        
        dataList.setEnabled(enabled);
        
        locTypePanel.setEnabled(enabled);
        sideOfRoadPanel.setEnabled(enabled);
        orientationPanel.setEnabled(enabled);
        selectTypePanel.setEnabled(enabled);
        
        super.setEnabled(enabled);
    }

    /**
     * Updates the status of the UI elements on changes of the active map
     */
    private final class UpdateStateOnMapChangeObserver implements
            MapChangeObserver {
        
        /**
         * {@inheritDoc}
         */
        @Override
        public void update(final MapsHolder holder) {
            
            applyToState(holder);

            switchListeningToMapSelections(holder);
        }

        /**
         * Applies the location panel and its sub-panels to the state in the new active map
         * @param holder The maps holder
         */
        private void applyToState(final MapsHolder holder) {
            currentState.removeLocationChangeObserver(LocationPanel.this);
            LocationHolder locationHolderNew = holder.getLocationHolder(holder
                    .getMapActive());
            locationHolderNew.addLocationChangeObserver(LocationPanel.this);
            setState(locationHolderNew);
        }
        
        /**
         * Switches listening for mouse selections on the new active map and 
         * stops listening to the former one. 
         * @param holder The maps holder
         */        
        private void switchListeningToMapSelections(final MapsHolder holder) {
            
            MapIndex newActiveMap = holder.getMapActive();

            for (MapIndex index : MapIndex.values()) {

                MapToolManager toolManager = holder.getMapPane(index)
                        .getToolManager();
                CursorTool activeCursTool = toolManager.getActiveCursorTool();

                if (activeCursTool instanceof SelectTool) {
                    SelectTool selTool = (SelectTool) activeCursTool;

                    if (index == newActiveMap) {

                        selTool.setSelectionProcessor(selectTypePanel);
                    } else {
                        selTool.setSelectionProcessor(null);
                    }
                }
            }
        }

    }

}

