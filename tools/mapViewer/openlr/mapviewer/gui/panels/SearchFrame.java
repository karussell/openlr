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

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import openlr.map.GeoCoordinates;
import openlr.map.GeoCoordinatesImpl;
import openlr.map.InvalidMapDataException;
import openlr.map.Line;
import openlr.map.MapDatabase;
import openlr.map.Node;
import openlr.mapviewer.gui.MapViewerGui;
import openlr.mapviewer.gui.layer.MapLayerStore;
import openlr.mapviewer.linesearch.gui.LineSearchComboBox;
import openlr.mapviewer.linesearch.model.LineNameModel.Item;
import openlr.mapviewer.maplayer.GeoCoordinateSearchMapLayer;
import openlr.mapviewer.maplayer.LineNameSearchMapLayer;
import openlr.mapviewer.maplayer.LineSearchMapLayer;
import openlr.mapviewer.maplayer.NodeSearchMapLayer;
import openlr.mapviewer.utils.bbox.BoundingBox;

/**
 * The SearchDialog enables the user to search for coordinates, nodes and lines.
 * If the element was found in the map then it will be highlighted.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class SearchFrame extends MapTool implements ItemListener  {

	/**
	 * 
	 */
	private static final int WIDTH_LINE_SEARCH_BOX = 350;

	/** The coord btn. */
	private final JRadioButton coordBtn = new JRadioButton("Coordinate");

	/** The line btn. */
	private final JRadioButton lineBtn = new JRadioButton("Line ID");

	/** The radio button to enable the search via line name. */
	private final JRadioButton lineNameBtn = new JRadioButton("Road name");

	/** The node btn. */
	private final JRadioButton nodeBtn = new JRadioButton("Node ID");

	/** The lat field. */
	private final JTextField latField = new JTextField(12);

	/** The lon field. */
	private final JTextField lonField = new JTextField(12);

	/** The line field. */
	private final JTextField lineField = new JTextField(20);

	/** The node field. */
	private final JTextField nodeField = new JTextField(20);

    /**
     * A reference to the assigned map layer store
     */
    private final MapLayerStore maplayerStore;
    
	    /**
     * Creates a new search dialog.
     * 
     * @param ms
     *            the map store
     * @param mapDB
     *            The map database
     * @param mapPane
     *            The related map pane
     */
    public SearchFrame(final MapLayerStore ms, final MapDatabase mapDB, final OpenLRMapPane mapPane) {
		JPanel panel = new JPanel(new MigLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Search for ..."));

		maplayerStore = ms;
	    JFrame dialogFrame = getDialogFrame();
		dialogFrame.setTitle("Search for coordinates, nodes and lines");
		coordBtn.addItemListener(this);
		nodeBtn.addItemListener(this);
		lineBtn.addItemListener(this);

        LineSearchComboBox lineSearchBox = new LineSearchComboBox();
        lineSearchBox.setPreferredSize(new Dimension(WIDTH_LINE_SEARCH_BOX,
                lineSearchBox.getHeight()));
        JPanel lineSearchFieldContainer = new JPanel();
        lineSearchFieldContainer.setLayout(new GridLayout(1, 1));
        lineSearchFieldContainer.setPreferredSize(new Dimension(
                WIDTH_LINE_SEARCH_BOX, lineSearchBox.getHeight()));
        lineSearchFieldContainer.add(lineSearchBox);
        LineNameSearchListener lineSearchListener = new LineNameSearchListener(
                lineSearchBox, mapDB);
        lineNameBtn.addItemListener(lineSearchListener);

		nodeField.setEnabled(false);
		lineField.setEnabled(false);
		latField.setEnabled(false);
		lonField.setEnabled(false);
		lineSearchBox.setEnabled(false);

		ButtonGroup group = new ButtonGroup();
		group.add(coordBtn);
		group.add(nodeBtn);
		group.add(lineBtn);
		group.add(lineNameBtn);

		panel.add(coordBtn);
		panel.add(new JLabel("Longitude: "));
		panel.add(lonField);
		panel.add(new JLabel("  Latitude: "));
		panel.add(latField, "wrap");

		panel.add(lineBtn);
		panel.add(new JLabel("ID: "));
		panel.add(lineField, "wrap, span 2 1");

		panel.add(nodeBtn);
		panel.add(new JLabel("ID: "));
		panel.add(nodeField, "wrap, span 2 1");

		panel.add(lineNameBtn);
		panel.add(new JLabel("Search: "));
		panel.add(lineSearchFieldContainer, "wrap, span 3 1");

        SearchDialogAction sda = new SearchDialogAction(this, ms,
                mapDB, lineSearchBox, mapPane);

		JButton searchBtn = new JButton("Search");
		searchBtn.addActionListener(sda);
		searchBtn.setActionCommand("search");

		JButton abortBtn = new JButton("Close");
		abortBtn.addActionListener(sda);
		abortBtn.setActionCommand("close");

		panel.add(searchBtn);
		panel.add(abortBtn);

		coordBtn.doClick();
		dialogFrame.getRootPane().setDefaultButton(searchBtn);

		dialogFrame.setContentPane(panel);
		dialogFrame.setIconImage(MapViewerGui.OPENLR_ICON);
		dialogFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		dialogFrame.pack();
		dialogFrame.setLocationRelativeTo(null);
		
		dialogFrame.setVisible(true);
		
		addCloseAction(new CloseAction() {
            
            @Override
            public void close() {
                maplayerStore.removeSearchLayer();
            }
        });
	}

	/**
	 * The Class SearchDialogAction.
	 */
	private static final class SearchDialogAction implements ActionListener {

		/** The dialog. */
		private final SearchFrame dialog;

		/** The map store. */
		private final MapLayerStore mapStore;

		/**
		 * A reference to the map database
		 */
		private final MapDatabase repo;
		
		/**
		 * The line name search box
		 */
		private final LineSearchComboBox lineNameBox;
		
		/**
		 * A reference to the related map pane
		 */
		private final OpenLRMapPane mapGfx;

		                /**
         * Instantiates a new search dialog action.
         * 
         * @param d
         *            the search dialog
         * @param ms
         *            the map store
         * @param mapDB
         *            The map database
         * @param lineNameSearchBox
         *            The reference to the line search input element
         * @param mapPane
         *            A reference to the related map pane
         */
        SearchDialogAction(final SearchFrame d, final MapLayerStore ms,
                final MapDatabase mapDB,
                final LineSearchComboBox lineNameSearchBox,
                final OpenLRMapPane mapPane) {
			dialog = d;
			mapStore = ms;
			repo = mapDB;
			lineNameBox = lineNameSearchBox;
			mapGfx = mapPane;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(final ActionEvent e) {
			if ("close".equals(e.getActionCommand())) {
				dialog.close();
			} else if ("search".equals(e.getActionCommand())) {
				if (dialog.coordBtn.isSelected()) {
					coordSearch();
				} else if (dialog.nodeBtn.isSelected()) {
					nodeSearch();
				} else if (dialog.lineBtn.isSelected()) {
					lineSearch();
				} else if (dialog.lineNameBtn.isSelected()) {
					lineNameSearch();
				}
			}
		}

		/**
		 * Search for a coordinate and highlight it if the coordinate is valid.
		 */
		private void coordSearch() {
			try {
				GeoCoordinates c = new GeoCoordinatesImpl(
						Double.valueOf(dialog.lonField.getText()),
						Double.valueOf(dialog.latField.getText()));
				mapStore.setSearchLayer(new GeoCoordinateSearchMapLayer(c));
                BoundingBox bb = StandardZoomCalculator.standardZoomTo(c);
                mapGfx.setViewport(bb);
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(null, "Invalid Coordinate",
						"Error", JOptionPane.ERROR_MESSAGE);
			} catch (InvalidMapDataException imde) {
				JOptionPane.showMessageDialog(null, "Invalid Coordinate",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}

		/**
		 * Search for a node and highlight it if it was found in the map.
		 */
		private void nodeSearch() {
			Node n = null;
			try {
				n = repo.getNode(Long.valueOf(dialog.nodeField.getText()));
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(null, "Invalid ID", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (n != null) {
				mapStore.setSearchLayer(new NodeSearchMapLayer(n));
                BoundingBox bb = StandardZoomCalculator.standardZoomTo(n
                        .getGeoCoordinates());
                mapGfx.setViewport(bb);
			} else {
				JOptionPane.showMessageDialog(null, "Node with ID "
						+ dialog.nodeField.getText() + " not found!", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		/**
		 * Search for a line and highlight it if it was found in the map.
		 */
		private void lineSearch() {
			Line l = null;
			try {
				l = repo.getLine(Long.valueOf(dialog.lineField.getText()));
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(null, "Invalid ID", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (l != null) {
				mapStore.setSearchLayer(new LineSearchMapLayer(l));
                BoundingBox bb = StandardZoomCalculator.standardZoomTo(l);
                mapGfx.setViewport(bb);
			} else {
				JOptionPane.showMessageDialog(null,
						"Cannot find a line with this id", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		/**
		 * Search for a line and highlight it if it was found in the map.
		 */
		private void lineNameSearch() {
            Item lineObject = lineNameBox.getSelectedLineNameItem();
			if (lineObject != null) {
				List<Long> ids = lineObject.getLineIDs();
				List<Line> lines = new ArrayList<Line>();
				for (long l : ids) {
					Line line = repo.getLine(l);
					lines.add(line);
				}

				if (!lines.isEmpty()) {
					mapStore.setSearchLayer(new LineNameSearchMapLayer(lines));
                    BoundingBox bb = StandardZoomCalculator
                            .standardZoomTo(lines.toArray(new Line[lines.size()]));
                    mapGfx.setViewport(bb);
				} else {
					JOptionPane.showMessageDialog(null,
							"Cannot find lines belonging to name \""
									+ lineNameBox
											.getSelectedLineNameItem()
											.getLineName() + "\"", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null,
						"No valid line name selected", "Error",
						JOptionPane.ERROR_MESSAGE);
			}		    
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void itemStateChanged(final ItemEvent e) {
		Object o = e.getSource();
		if (coordBtn.equals(o)) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				lonField.setEnabled(true);
				latField.setEnabled(true);
				lonField.requestFocus();
			} else {
				lonField.setEnabled(false);
				latField.setEnabled(false);
			}
		} else if (nodeBtn.equals(o)) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				nodeField.setEnabled(true);
                nodeField.requestFocus();
			} else {
				nodeField.setEnabled(false);
			}
		} else if (lineBtn.equals(o)) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				lineField.setEnabled(true);
				lineField.requestFocus();
			} else {
				lineField.setEnabled(false);
			}
		} 
		
	}
}
