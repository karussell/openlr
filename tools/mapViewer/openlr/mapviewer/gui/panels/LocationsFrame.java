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
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import net.miginfocom.swing.MigLayout;
import openlr.location.Location;
import openlr.location.utils.LocationData;
import openlr.location.utils.LocationDataIterator;
import openlr.location.utils.LocationDataReader;
import openlr.map.InvalidMapDataException;
import openlr.mapviewer.MapsHolder;
import openlr.mapviewer.MapsHolder.MapIndex;
import openlr.mapviewer.gui.MapViewerGui;
import openlr.mapviewer.gui.filechoose.FileChooserFactory;
import openlr.mapviewer.location.LocationHolder;
import openlr.mapviewer.maplayer.AllLocationsMapLayer;
import openlr.mapviewer.utils.bbox.BoundingBox;

import org.apache.log4j.Logger;

/**
 * The Class LocationsFrame.
 */
public class LocationsFrame extends MapTool implements ActionListener,
		DocumentListener {

    /**
     * The default index value provided to the user where all loaded locations
     * are visible.
     */
    private static final String DEFAULT_ALL_LOCATIONS_INDEX = "0";

    /** The Constant COLUMNS. */
	private static final int CURRENT_FILE_COLUMNS = 30;

	/**
	 * The logger
	 */
	private static final Logger LOG = Logger.getLogger(LocationsFrame.class);
	
	/** The Constant CURRENT_INDEX_COLUMNS. */
	private static final int CURRENT_INDEX_COLUMNS = 4;

	/** The next btn. */
	private final JButton nextBtn = new JButton("Next");

	/** The prev btn. */
	private final JButton prevBtn = new JButton("Prev");

	/** The select file btn. */
	private final JButton selectFileBtn = new JButton("Select location file");

	/** The load btn. */
	private final JButton loadBtn = new JButton("Load locations");

	/** The current file. */
	private final JTextField currentFile = new JTextField();

	/** The current index. */
	private final JTextField currentIndex = new JTextField();

	/** The id label. */
	private final JLabel idLabel = new JLabel("[all locations]");

	/** The row label. */
	private final JLabel typeLabel = new JLabel("--");

	/** The max label. */
	private final JLabel maxLabel = new JLabel(" / ");

	/** The clear btn. */
	private final JButton clearBtn = new JButton("Clear");

	/** The data iterator. */
	private LocationDataIterator dataIterator;

	/** The location. */
	private final LocationHolder location;

	/** The map. */
	private final MapsHolder mapsHolder;

	/** The map index. */
	private final MapsHolder.MapIndex mapIndex;

	/** The status bar. */
	private final StatusBar statusBar;	

	/**
	 * The file chooser factory.
	 */
	private final FileChooserFactory fileChooserFactory;

	/**
	 * Instantiates a new locations frame.
	 * 
	 * @param lh
	 *            the lh
	 * @param ms
	 *            the ms
	 * @param mapIdx
	 *            the map idx
	 * @param fcf
	 *            the file chooser factory
	 * @param sBar
	 *            the s bar
	 */
	public LocationsFrame(final LocationHolder lh, final MapsHolder ms,
			final MapIndex mapIdx, final FileChooserFactory fcf,
			final StatusBar sBar) {
		super();
		mapsHolder = ms;
		mapIndex = mapIdx;
		statusBar = sBar;
		fileChooserFactory = fcf;
		JPanel panel = new JPanel(new MigLayout("insets 8", "[]", "[][]"));
	    final JFrame dialogFrame = getDialogFrame();
		dialogFrame.setContentPane(panel);
		dialogFrame.setTitle("Stored locations");
		JPanel loadPanel = new JPanel(new MigLayout("insets 8", "[][][]",
				"[][]"));
		loadPanel.setBorder(BorderFactory
				.createTitledBorder("Load location file"));
		loadPanel.add(new JLabel("Selected file: "));
		loadPanel.add(currentFile, "wrap, span 2");
		loadPanel.add(selectFileBtn);
		loadPanel.add(loadBtn);
		loadPanel.add(clearBtn);
		loadBtn.setEnabled(false);
		clearBtn.setActionCommand("clear");
		clearBtn.addActionListener(this);
		clearBtn.setEnabled(false);
		currentFile.setColumns(CURRENT_FILE_COLUMNS);
		currentFile.getDocument().addDocumentListener(this);
		selectFileBtn.setActionCommand("selectFile");
		loadBtn.setActionCommand("load");
		selectFileBtn.addActionListener(this);
		loadBtn.addActionListener(this);
		JPanel lineLocPanel = new JPanel(new MigLayout("insets 8",
				"[][][][][]", "[][][]"));
		lineLocPanel.setBorder(BorderFactory
				.createTitledBorder("Locations"));
		lineLocPanel.add(prevBtn);
		prevBtn.setActionCommand("prev");
		prevBtn.addActionListener(this);
		prevBtn.setEnabled(false);
		currentIndex.setEnabled(false);
		nextBtn.setEnabled(false);
		lineLocPanel.add(currentIndex);
		lineLocPanel.add(maxLabel);
		lineLocPanel.add(nextBtn, "wrap");
		nextBtn.setActionCommand("next");
		nextBtn.addActionListener(this);
		lineLocPanel.add(new JLabel("ID: "));
		lineLocPanel.add(idLabel, "wrap, span 3");
		lineLocPanel.add(new JLabel("Type: "));
		lineLocPanel.add(typeLabel, "span 3");
		currentIndex.setColumns(CURRENT_INDEX_COLUMNS);
		currentIndex.getDocument().addDocumentListener(this);
		currentIndex.setHorizontalAlignment(JTextField.CENTER);
		location = lh;
		panel.add(loadPanel, "wrap");
		panel.add(lineLocPanel, "wrap");
	    JButton abortBtn = new JButton("Close");
	    abortBtn.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent e) {
                dialogFrame.setVisible(false);              
            }
        });
		panel.add(abortBtn);
		dialogFrame.setIconImage(MapViewerGui.OPENLR_ICON);
		dialogFrame.pack();
		dialogFrame.setLocationRelativeTo(null);
				
		addCloseAction(new CloseAction() {
            
            @Override
            public void close() {
                mapsHolder.getMapLayerStore(mapIndex).removeLocationsLayer();               
            }
        });
		dialogFrame.setVisible(true);
	}

	/**
	 * Action performed.
	 * 
	 * @param e
	 *            the e {@inheritDoc}
	 */
	@Override
	public final void actionPerformed(final ActionEvent e) {
		Location ll = null;
	    JFrame dialogFrame = getDialogFrame();
		if (e.getActionCommand().equals("clear")) {
			mapsHolder.getMapLayerStore(mapIndex).removeLocationsLayer();
			prevBtn.setEnabled(false);
			currentIndex.setEnabled(false);
			nextBtn.setEnabled(false);
			currentIndex.setText("");
			maxLabel.setText(" / ");
			if (!currentFile.getText().isEmpty()) {
				loadBtn.setEnabled(true);
			}
		} else if (e.getActionCommand().equals("selectFile")) {
			JFileChooser jfc = fileChooserFactory
					.createFileChooser(FileChooserFactory.TOPIC_LOCATION);
			int re = jfc.showDialog(null, "Select location file");
			if (re == JFileChooser.APPROVE_OPTION) {
				String fName = jfc.getSelectedFile().getAbsolutePath();
				currentFile.setText(fName);
				loadBtn.setEnabled(true);
			}
		} else if (e.getActionCommand().equals("load")) {
			Cursor oldCursor = dialogFrame.getCursor();
            dialogFrame.setCursor(MapViewerGui.WAIT_CURSOR);
            try {
                statusBar.setMessage("Loading locations...");
                LocationData data = null;
                try {
                    data = LocationDataReader.loadLocationData(new File(
                            currentFile.getText()), mapsHolder
                            .getMapDatabase(mapIndex));
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(dialogFrame,
                            "Cannot load location file!", "Load failure",
                            JOptionPane.ERROR_MESSAGE);
                }
                if (data != null) {
                    if (data.hasErrors()) {
                        showLoadingErrors(data);
                    }
                    dataIterator = data.getIterator();
                    maxLabel.setText(" / " + dataIterator.size());
                    currentIndex.setText(DEFAULT_ALL_LOCATIONS_INDEX);
                    AllLocationsMapLayer maplayer = new AllLocationsMapLayer(
                            data.getLocations());
                    mapsHolder.getMapLayerStore(mapIndex).setLocationsLayer(
                            maplayer);
                    activateAllLocations();

                } else {
                    JOptionPane.showMessageDialog(dialogFrame,
                            "Cannot load location file!", "Load failure",
                            JOptionPane.ERROR_MESSAGE);
                }
                statusBar.setMessage("Loading finished!");
            } finally {
                dialogFrame.setCursor(oldCursor);
                prevBtn.setEnabled(dataIterator.currentIndex() >= 0);
                nextBtn.setEnabled(dataIterator.hasNext());      
                clearBtn.setEnabled(true);
                currentIndex.setEnabled(true);
                loadBtn.setEnabled(false);
            }
		} else {
            if (e.getActionCommand().equals("prev")) {
                if (dataIterator.currentIndex() == 0) {
                    activateAllLocations();
                    currentIndex.setText(DEFAULT_ALL_LOCATIONS_INDEX);
                } else {

                    ll = dataIterator.previous();
                }
            } else if (e.getActionCommand().equals("next")) {

                ll = dataIterator.next();
            }
			if (ll != null) {
			    currentIndex.setText(Integer.toString(dataIterator
			            .currentIndex() + 1));
				activateNewLocation(ll);											
			} 
			prevBtn.setEnabled(dataIterator.currentIndex() >= 0);
			nextBtn.setEnabled(dataIterator.hasNext());
		}
	}

    /**
     * Shows a message dialog informing about the errors that occurred while
     * reading the location file
     * 
     * @param data
     *            The related location data
     */
    private void showLoadingErrors(final LocationData data) {
        StringBuilder message = new StringBuilder();
        message.append("<html>").append(data.getNrOfErrors())
                .append(" locations couldn't be loaded from file!<br>");

        int count = 1;
        for (String errorLine : data.getErrors()) {
            message.append(count++).append(": ").append(errorLine)
                    .append("<br>");
        }

        message.append("</html>");
        JLabel errorLabel = new JLabel(message.toString());
        JOptionPane.showMessageDialog(getDialogFrame(), errorLabel,
                "Load failure", JOptionPane.ERROR_MESSAGE);
    }

	/**
	 * Activates the display of state "all locations"
	 */
    private void activateAllLocations() {
        dataIterator.reset();
        location.clearLocation();
        idLabel.setText("[all locations]");
        typeLabel.setText("--");
        mapsHolder.getMapPane(mapIndex).resetMapView();
    }
	
    /**
     * Performs the task that are necessary when a new selected location has to
     * be activated.
     * 
     * @param loc
     *            The location
     * @param dialogFrame
     * @throws HeadlessException
     */
    private void activateNewLocation(final Location loc) {
        try {
            location.setLocation(loc);
            idLabel.setText(loc.getID());
            typeLabel.setText(loc.getLocationType().name());
            BoundingBox bb = StandardZoomCalculator.standardZoomTo(loc);
            mapsHolder.getMapPane(mapIndex).setViewport(bb);
            
        } catch (InvalidMapDataException ex) {
            LOG.error("Error setting selected location " + loc, ex);
            JOptionPane.showMessageDialog(getDialogFrame(),
                    "Cannot activate location " + loc.getID() + ", error: "
                            + ex.getMessage(), "Location error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

	/**
	 * Changed update.
	 * 
	 * @param e
	 *            the e {@inheritDoc}
	 */
	@Override
	public final void changedUpdate(final DocumentEvent e) {
		update(e);
	}

	/**
	 * Insert update.
	 * 
	 * @param e
	 *            the e {@inheritDoc}
	 */
	@Override
	public final void insertUpdate(final DocumentEvent e) {
		update(e);
	}

	/**
	 * Performs the action done when inserting or updating the file name
	 * document.
	 * 
	 * @param e
	 *            The document event to process.
	 */
	private void update(final DocumentEvent e) {
		Document d = e.getDocument();
		if (d == currentFile.getDocument()) {
			if (!currentFile.getText().isEmpty()) {
				loadBtn.setEnabled(true);
			} else {
				loadBtn.setEnabled(false);
			}
		} else if (d == currentIndex.getDocument()) {
			String iText = currentIndex.getText();
			int indexEntered;
            try {
                indexEntered = Integer.parseInt(iText);
                if (indexEntered >= 0
                        && indexEntered <= dataIterator.size()) {
                    
                    if (indexEntered > 0) {                        
                        // we remove one, to the user we provided indexes 1 to
                        // (n + 1), while the iterator knows 0 to n
                        Location ll = dataIterator.setCurrent(indexEntered - 1);
                        activateNewLocation(ll);
                    }  else {
                        // index zero in the UI is a special, it shows all loaded locations
                        activateAllLocations();
                    }
                }
            } catch (NumberFormatException nfe) {
                LOG.error("Cannot parse input text as valid location index: "
                        + iText);
            }

            prevBtn.setEnabled(dataIterator.currentIndex() >= 0);
            nextBtn.setEnabled(dataIterator.hasNext());
		}
	}

	/**
	 * Removes the update.
	 * 
	 * @param e
	 *            the e {@inheritDoc}
	 */
	@Override
	public final void removeUpdate(final DocumentEvent e) {
		Document d = e.getDocument();
		if (d == currentFile.getDocument() && currentFile.getText().isEmpty()) {
			loadBtn.setEnabled(false);
		}
	}
}
