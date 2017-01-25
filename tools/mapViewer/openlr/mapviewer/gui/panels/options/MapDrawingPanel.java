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
package openlr.mapviewer.gui.panels.options;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.text.JTextComponent;

import net.miginfocom.swing.MigLayout;
import openlr.mapviewer.MapViewer;
import openlr.mapviewer.gui.utils.VisibilityDependentPropertiesObservationListener;
import openlr.mapviewer.properties.MapDrawingProperties;
import openlr.mapviewer.properties.MapViewerProperties;
import openlr.mapviewer.properties.MapViewerPropertiesObserver;

/** 
 * This class draws an JPanel presenting all the possible colors settings for 
 * the MapViewer application.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class MapDrawingPanel extends JPanel implements MapViewerPropertiesObserver {

	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	/** The Constant WIDTH. */
	private static final int BUTTON_WIDTH = 10;

	/** The Constant HEIGHT. */
	private static final int BUTTON_HEIGHT = 10;

	/** The index of the property key in the lists of color properties. */
	private static final int INDEX_KEY = 0;
	/** The index of the label for an option in the lists of color properties. */
	private static final int INDEX_LABEL = 1;
	/** The index of the tool-tip for an option in the lists of color properties. */
	private static final int INDEX_TOOL_TIP = 2;
	
	/** The preferred size of each of the four sub-boxes inside the panel. */
	private static final Dimension PREFERRED_SIZE_CELL = new Dimension(180, 20);
	
	/**
	 * The width of the stroke input field in pixels.
	 */
	private static final int WIDTH_STROKE_INPUT_FIELDS = 30;	
	

	/** The data for the FOW options. */
	private static final List<String[]> FOW_COLORS = Arrays.asList(
			new String[] {MapDrawingProperties.FOW_UNDEFINED.key(), "undefined", "FOW 0"},
			new String[] {MapDrawingProperties.FOW_MOTORWAY.key(), "motorway", "FOW 1"},
			new String[] {MapDrawingProperties.FOW_MULTIPLE_CARRIAGEWAY.key(), "multiple carriageway", "FOW 2"},
			new String[] {MapDrawingProperties.FOW_SINGLE_CARRIAGEWAY.key(), "single carriageway", "FOW 3"},
			new String[] {MapDrawingProperties.FOW_ROUNDABOUT.key(), "roundabout", "FOW 4"},
			new String[] {MapDrawingProperties.FOW_TRAFFIC_SQUARE.key(), "trafficsquare", "FOW 5"},
			new String[] {MapDrawingProperties.FOW_SLIPROAD.key(), "sliproad", "FOW 6"},
			new String[] {MapDrawingProperties.FOW_OTHER.key(), "other", "FOW 7"}
			);
	
	/** The data for the FRC options. */
	private static final List<String[]> FRC_COLORS = Arrays.asList(
			new String[] {MapDrawingProperties.FRC_FRC0.key(), "FRC 0", "main road"},
			new String[] {MapDrawingProperties.FRC_FRC1.key(), "FRC 1", "first class road"},
			new String[] {MapDrawingProperties.FRC_FRC2.key(), "FRC 2", "second class road"},
			new String[] {MapDrawingProperties.FRC_FRC3.key(), "FRC 3", "third class road"},
			new String[] {MapDrawingProperties.FRC_FRC4.key(), "FRC 4", "fourth class road"},
			new String[] {MapDrawingProperties.FRC_FRC5.key(), "FRC 5", "fift class road"},
			new String[] {MapDrawingProperties.FRC_FRC6.key(), "FRC 6", "sixt class road"},
			new String[] {MapDrawingProperties.FRC_FRC7.key(), "FRC 7", "other class road"}
	);

	/** The data for the Location options. */
	private static final List<String[]> LOCATION_COLORS = Arrays.asList(
			new String[] {MapDrawingProperties.LOCATION_ENCODER.key(), "Encoded location" , "An encoded location."},
			new String[] {MapDrawingProperties.LOCATION_POS_OFF.key(), "Positive offset" , "The positive offset point, aka the access point."}, 			
			new String[] {MapDrawingProperties.LOCATION_NEG_OFF.key(), "Negative offset" , "The negative offset point."}, 
			new String[] {MapDrawingProperties.LOCATION_SELECTED.key(), "Selected location" , "The selected location."},
			new String[] {MapDrawingProperties.LOCATION_STORED.key(), "Stored location", "The currently visited location of a loaded collection of locations." }, 
			new String[] {MapDrawingProperties.LOCATIONS_ALL.key(), "All locations", "All locations of a loaded location file." },
			new String[] {MapDrawingProperties.LOCATION_GEO_COORD.key(), "Geo-coordinate", "<html>The geo-coordinate of a geo-coordinate location and <br>the POI of a POI-with-access-point location.</hmtl>" },
			new String[] {MapDrawingProperties.AREA_LOCATION_COVERED_LINE.key(), "Covered Line", "covered line for area location" },
			new String[] {MapDrawingProperties.AREA_LOCATION_INTERSECTED_LINE.key(), "Intersected Line", "intersected line for area location" }
			
			);

	/** The data for the "other" options. */
	private static final List<String[]> OTHER_COLORS = Arrays.asList(
			new String[] {MapDrawingProperties.LINE.key(), "Line", "A line of the map network."},
			new String[] {MapDrawingProperties.NODE.key(), "Node", "The border color of a node of the map network. The node point is filled with a brighter version of that color."},			
			new String[] {MapDrawingProperties.LINE_START_NODE.key(), "Line start node", "The start node of a line."},
			new String[] {MapDrawingProperties.MAPPANE_MEASURE.key(), "Map panel measure", "The line displayed when using the measure feature."},
			new String[] {MapDrawingProperties.MAPPANE_SCALE.key(), "Map panel scale", "The scale information of the map panel."},
			new String[] {MapDrawingProperties.HIGHLIGHTED_LINE.key(), "<html>mouse-over</html>", "The color of the line next to the mouse pointer."},
			new String[] {MapDrawingProperties.SEARCH_RESULT.key(), "<html>search result</html>", "The lines of a the result of a search."}
			);	
	
	/** The data for the stroke options. */
	private static final List<String[]> STROKES = Arrays.asList(
			new String[] {MapDrawingProperties.LINE_STROKE_SIZE.key(), "Line stroke size", "The size of the stroke of the lines."},
			new String[] {MapDrawingProperties.LOCATION_LINE_STROKE_SIZE.key(), "Location stroke size", "The size of the line stroke for a location."},			
			new String[] {MapDrawingProperties.NODE_MARK_SIZE.key(), "Node size", "The base size of a painted network node."},
			new String[] {MapDrawingProperties.POINT_MARK_SIZE.key(), "Point size", "<html>The size of the point representing the start node of a line or location,<br> a geo coordinate, the positive or negative offset.</html>"}
			);		

	/**
	 * Creates a new MapDrawingPanel.
	 */
	public MapDrawingPanel() {
		add(createColorsPanel());
        addAncestorListener(new VisibilityDependentPropertiesObservationListener(this));
	}
	
	/**
	 * Creates the sub-panel containing all the options elements.
	 * @return The sub-panel.
	 */
	private JPanel createColorsPanel() {
		
		JPanel panel = new JPanel();
		
		panel.setLayout(new MigLayout("insets 0", "[][right]", "[top][top][b]"));
	
		JPanel fowPanel = createOptionsPanel("FOW colors", FOW_COLORS);
		fowPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		panel.add(fowPanel);
		
		JPanel frcPanel = createOptionsPanel("FRC colors", FRC_COLORS);
		frcPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		panel.add(frcPanel, "wrap");
		
		JPanel panel3 = createOptionsPanel("Location colors", LOCATION_COLORS);
		panel3.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		panel.add(panel3);
	
		JPanel panel4 = createOptionsPanel("Other colors", OTHER_COLORS);
		panel4.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		panel.add(panel4, "wrap");
			
		JPanel panel5 = createStrokePropertiesPanel();
		panel5.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		panel.add(panel5);	
		
		JButton resetDefaults = new JButton("Reset to defaults");
		resetDefaults.addActionListener(new ResetToDefaultsAction(this));
		panel.add(resetDefaults);
		
		return panel;	
	}

	/**
	 * Creates an sub-panel containing a subset of the colors options.
	 * 
	 * @param title
	 *            The title of this subset of options.
	 * @param data
	 *            The data for the elements.
	 * @return The created panel.
	 */
	private JPanel createOptionsPanel(final String title, final List<String[]> data) {

		JPanel panel = new JPanel();
		panel.setPreferredSize(PREFERRED_SIZE_CELL);
		panel.setLayout(new MigLayout("insets 0"));
		panel.add(new JLabel(title), "span 2, wrap");
		panel.add(new JSeparator(), "span 2, growx, wrap");

		for (String[] entry : data) {
			String name = entry[INDEX_LABEL];
			String key = entry[INDEX_KEY];
			String tt = entry[INDEX_TOOL_TIP];
			panel.add(createColorButton(key, name, MapViewer.PROPERTIES
					.getColorProperty(key), tt));
			JLabel label = new JLabel(name);
			label.setToolTipText(tt);
			panel.add(label, "wrap");

		}

		return panel;
	}

	/**
	 * Creates a color chooser button with a connected {@link JColorChooser}.
	 * 
	 * @param propertyKey
	 *            The property behind the settings element.
	 * @param name
	 *            The name of the setting as displayed to the user.
	 * @param initialColor
	 *            The initial color of the button.
	 * @param toolTip
	 *            An optional tool-tip text.
	 * @return The created button.
	 */
	private JButton createColorButton(final String propertyKey,
			final String name, final Color initialColor, final String toolTip) {

		JButton colorBtn = new JButton();
		colorBtn.setBackground(initialColor);
		colorBtn.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		colorBtn.setToolTipText(toolTip);
		colorBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				Color c = JColorChooser.showDialog(null, "Choose color for "
						+ name, initialColor);
				if (c != null) {
					((JButton) e.getSource()).setBackground(c);
					MapViewer.PROPERTIES.setColorProperty(propertyKey, c);
				}
			}
		});
		return colorBtn;
	}
	
	/**
	 * Creates the sub-panel containing all the node stroke elements.
	 * @return The sub-panel.
	 */
	private JPanel createStrokePropertiesPanel() {
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(PREFERRED_SIZE_CELL);
		panel.setLayout(new MigLayout("insets 0, fillx", "[][right]"));
		panel.add(new JLabel("Dimensions"), "span 2, wrap");
		panel.add(new JSeparator(), "span 2, growx, wrap");

		for (String[] entry : STROKES) {
			String name = entry[INDEX_LABEL];
			String key = entry[INDEX_KEY];
			String tt = entry[INDEX_TOOL_TIP];
			JLabel lineStrokeLabel = new JLabel(name);
			lineStrokeLabel.setToolTipText(tt);
			panel.add(lineStrokeLabel);

			panel.add(createValueInputField(tt, key, WIDTH_STROKE_INPUT_FIELDS),
					"wrap");
		}

		return panel;
	}

	/**
	 * Creates an input field for the specified property.
	 * @param toolTip The tool-tip text.
	 * @param property The key of the corresponding property.
	 * @param width The width in pixels.
	 * @return The input field instance.
	 */
	private JTextField createValueInputField(final String toolTip,
			final String property, final int width) {

		String value = MapViewer.PROPERTIES.getProperty(property);
		JTextField tf = new JTextField();
		tf.setText(value);
		tf.setHorizontalAlignment(JTextField.RIGHT);
		tf.setPreferredSize(new Dimension(width, tf.getHeight()));
		tf.setToolTipText(toolTip);

		UpdatePropertyOnFocusLostListener focusLostListener = 
			new UpdatePropertyOnFocusLostListener(property);
		focusLostListener.setInputVerifier(new IntegerInputVerifier());
		tf.addFocusListener(focusLostListener);

		UpdatePropertyOnActionPerformedListener actPerfListener = 
			new UpdatePropertyOnActionPerformedListener(property);
		actPerfListener.setInputVerifier(new IntegerInputVerifier());
		tf.addActionListener(actPerfListener);

		return tf;
	}

	/**
	 * An implementation of {@link ActionListener} that performs the task
	 * to reset all the setting to the system defaults.
	 */
	private static final class ResetToDefaultsAction implements ActionListener {

		/** The parent component. */
		private Component parentComponent;
		
		/**
		 * Creates a new instance.
		 * @param parent The parent component for aligning the confirmation 
		 * dialog displayed during this action.
		 */
		private ResetToDefaultsAction(final Component parent) {
			parentComponent = parent;
		}

		/**
		 * Resets all the user setting to the MapViewer defaults.
		 * @param e The event.
		 */
		@Override
		public void actionPerformed(final ActionEvent e) {
			int selection = JOptionPane.showConfirmDialog(parentComponent,
					"This will remove all your user specific color settings " 
					+ "permanently. Proceed?", "Reset to default colors", 
					JOptionPane.OK_CANCEL_OPTION);
			
			if (selection == JOptionPane.OK_OPTION) {
				for (String[] entry : FOW_COLORS) {
					MapViewer.PROPERTIES.removeUserProperty(entry[INDEX_KEY]);
				}
				for (String[] entry : FRC_COLORS) {
					MapViewer.PROPERTIES.removeUserProperty(entry[INDEX_KEY]);
				}
				for (String[] entry : LOCATION_COLORS) {
					MapViewer.PROPERTIES.removeUserProperty(entry[INDEX_KEY]);
				}
				for (String[] entry : OTHER_COLORS) {
					MapViewer.PROPERTIES.removeUserProperty(entry[INDEX_KEY]);
				}
				for (String[] entry : STROKES) {
					MapViewer.PROPERTIES.removeUserProperty(entry[INDEX_KEY]);
				}
			}
		}
	}

	/**
	 * An implementation of {@link InputVerifier} for {@link JTextComponent}s 
	 * that checks the input for being an integer. In case of an error it returns
	 * <code>false</code> an paints the background of the input component red.
	 */
	private static class IntegerInputVerifier extends InputVerifier {
			
		@Override
		public boolean verify(final JComponent input) {
			boolean result = true;
			try {
			    if (input instanceof JTextComponent) {
				  Integer.parseInt(((JTextComponent) input).getText());
			    } else {
                    throw new IllegalStateException(
                            "Expected to process JTextComponents");
			    }
			} catch (NumberFormatException e) {
				// skip wrong input
				result = false;
			}
			return result;
		}
		
		@Override			
	    public boolean shouldYieldFocus(final javax.swing.JComponent input) {
	        if (!verify(input)) {
	            input.setBackground(Color.RED);
	            return false;
	        } else {
	            input.setBackground(null);
	            return true;
	        }
	    }		
	}
	/**
	 * Listens to changes regarding color properties from other parts of the 
	 * application and updates the colors of the {@link MapDrawingPanel} by 
	 * recreating it entirely.
	 * @param changedProperty The changed property.
	 */
	@Override
	public final void updateProperty(final String changedProperty) {
		if (MapViewerProperties.isMapDrawingProperty(changedProperty)) {
			removeAll();
			add(createColorsPanel());
		}
	}
}
