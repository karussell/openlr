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
package openlr.mapviewer.gui.panels.locationSelection;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;
import openlr.geomap.tools.SelectionProcessor;
import openlr.map.GeoCoordinates;
import openlr.map.InvalidMapDataException;
import openlr.map.Line;
import openlr.mapviewer.location.LocationHolder;
import openlr.mapviewer.utils.FindClosestLine;
import openlr.mapviewer.utils.Formatter;

/**
 * The Class SelectionTypePanel.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class SelectionTypePanel extends JPanel implements SelectionProcessor {

	/**
	 * The Enum InputType.
	 */
	private enum InputType {
		/** The line. */
		LINE,
		/** The neg offset. */
		NEG_OFFSET,
		/** The pos offset. */
		POS_OFFSET,
		/** The geo coordinate. */
		GEO_COORDINATE;
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -685192343253888613L;

	/** The Constant ENTER_DATA_BTN. */
	private static final JButton ENTER_DATA_BTN = new JButton("Enter data");

	/** The Constant SELECT_LINE. */
	private static final JRadioButton SELECT_LINE = new JRadioButton("Line");

	/** The Constant SELECT_GRID. */
	private static final JRadioButton ENTER_ROWS_COLS = new JRadioButton(
			"rows/columns");

	/** The Constant SELECT_POFF. */
	private static final JRadioButton SELECT_POFF = new JRadioButton(
			"pos. Offset");

	/** The Constant SELECT_NOFF. */
	private static final JRadioButton SELECT_NOFF = new JRadioButton(
			"neg. Offset");

	/** The Constant SELECT_COORD. */
	private static final JRadioButton SELECT_COORD = new JRadioButton(
			"Coordinate");

	/** The Constant UNIT_METERS. */
	private static final JRadioButton ENTER_RADIUS = new JRadioButton("radius");

	/** The Constant group1. */
	private static final ButtonGroup SELECTION_GROUP = new ButtonGroup();

	/** The Constant normalBorder. */
	private static final Border NORMAL_BORDER = BorderFactory
			.createTitledBorder("Selection type");

	/** The Constant grayBorder. */
	private static final Border GRAY_BORDER = BorderFactory.createTitledBorder(
			BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Selection type",
			TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
			(Font) UIManager.getDefaults().get("Font"), Color.LIGHT_GRAY);

	/**
	 * A reference to the currently assigned location holder providing the state
	 * of the selection
	 */
	private LocationHolder currentState;

	/** The current input type. */
	private InputType currentInputType;

	/**
	 * Instantiates a new selection type panel.
	 * 
	 * @param lh
	 *            the lh
	 */
	public SelectionTypePanel(final LocationHolder lh) {

		setBorder(NORMAL_BORDER);
		setLayout(new MigLayout("insets 0"));
		add(SELECT_LINE);
		add(SELECT_POFF);
		add(ENTER_ROWS_COLS, "wrap");
		add(SELECT_COORD);
		add(SELECT_NOFF);
		add(ENTER_RADIUS, "wrap");

		add(ENTER_DATA_BTN, "wrap,span");
		SELECTION_GROUP.add(SELECT_LINE);
		SELECTION_GROUP.add(SELECT_POFF);
		SELECTION_GROUP.add(SELECT_NOFF);
		SELECTION_GROUP.add(SELECT_COORD);
		SELECTION_GROUP.add(ENTER_ROWS_COLS);
		SELECTION_GROUP.add(ENTER_RADIUS);

		SelectionTypePanelAction stpa = new SelectionTypePanelAction(this);
		Enumeration<AbstractButton> buttons = SELECTION_GROUP.getElements();
		while (buttons.hasMoreElements()) {
			buttons.nextElement().addActionListener(stpa);
		}

		ENTER_DATA_BTN.addActionListener(new EnterDataActionListener(this));

		unset(true);

		setState(lh);
	}

	/**
	 * Processes the input from the mouse selection.
	 * 
	 * @param coordinate
	 *            The geo coordinate representing the mouse click location
	 */
	@Override
	public final void selectionClicked(final GeoCoordinates coordinate) {

		try {
			handleUserInput(coordinate.getLongitudeDeg(),
					coordinate.getLatitudeDeg());
		} catch (InvalidMapDataException e) {
			throw new IllegalArgumentException(
					"Geo Coordinate delivered by user selection is not valid: "
							+ Formatter.COORD_FORMATTER.format(coordinate));
		}
	}

	/**
	 * Handle user input.
	 * 
	 * @param lon
	 *            the lon
	 * @param lat
	 *            the lat
	 * @throws InvalidMapDataException
	 *             the invalid map data exception
	 */
	private void handleUserInput(final double lon, final double lat)
			throws InvalidMapDataException {
		if (currentInputType != null) {
			switch (currentInputType) {
			case GEO_COORDINATE:
				currentState.setGeoCoordinatePoi(lon, lat);
				break;
			default:
			case LINE:
				Line closestLine = FindClosestLine.determineClosestLine(
						currentState.getMapDatabase(), lon, lat);
				currentState.addOrRemoveLine(closestLine);
				break;
			case NEG_OFFSET:
				currentState.setNegOffset(lon, lat);
				break;
			case POS_OFFSET:
				currentState.setPosOffset(lon, lat);
				break;
			}
		}
	}

	/**
	 * Sets up the UI state according to the data in the given location holder
	 * 
	 * @param locationHolder
	 *            the location holder to set the state for
	 */
	public final void setState(final LocationHolder locationHolder) {

		currentState = locationHolder;

		unset(false);

		if (locationHolder.getLocationType() != null) {

			// we only enable the sub-elements if the panel is enabled at all
			boolean enabledAtAll = isEnabled();

			switch (locationHolder.getLocationType()) {
			case LINE_LOCATION:
				SELECT_LINE.setEnabled(enabledAtAll);
				if (locationHolder.offsetsSelectable()) {
					SELECT_POFF.setEnabled(enabledAtAll);
					SELECT_NOFF.setEnabled(enabledAtAll);
				}

				clickButtonAccordingToCurrentInput(SELECT_LINE);
				ENTER_DATA_BTN.setEnabled(enabledAtAll);
				break;
			case GEO_COORDINATES:
				SELECT_COORD.setEnabled(enabledAtAll);
				clickButtonAccordingToCurrentInput(SELECT_COORD);
				ENTER_DATA_BTN.setEnabled(enabledAtAll);
				break;
			case POI_WITH_ACCESS_POINT:
				SELECT_LINE.setEnabled(enabledAtAll);
				if (locationHolder.offsetsSelectable()) {
					SELECT_POFF.setEnabled(enabledAtAll);
				}
				SELECT_COORD.setEnabled(enabledAtAll);
				clickButtonAccordingToCurrentInput(SELECT_LINE);
				ENTER_DATA_BTN.setEnabled(enabledAtAll);
				break;
			case POINT_ALONG_LINE:
				SELECT_LINE.setEnabled(enabledAtAll);
				if (locationHolder.offsetsSelectable()) {
					SELECT_POFF.setEnabled(enabledAtAll);
				}
				clickButtonAccordingToCurrentInput(SELECT_LINE);
				ENTER_DATA_BTN.setEnabled(enabledAtAll);
				break;
			case CIRCLE:
				ENTER_DATA_BTN.setEnabled(enabledAtAll);
				ENTER_RADIUS.setEnabled(enabledAtAll);
				SELECT_COORD.setEnabled(enabledAtAll);
				clickButtonAccordingToCurrentInput(SELECT_COORD);
				break;
			case GRID:
				ENTER_ROWS_COLS.setEnabled(enabledAtAll);
				SELECT_COORD.setEnabled(enabledAtAll);
				ENTER_DATA_BTN.setEnabled(enabledAtAll);
				clickButtonAccordingToCurrentInput(SELECT_COORD);
				break;
			case RECTANGLE:
			case POLYGON:
				SELECT_COORD.setEnabled(enabledAtAll);
				clickButtonAccordingToCurrentInput(SELECT_COORD);
				break;
			case CLOSED_LINE:
				SELECT_LINE.setEnabled(enabledAtAll);
				clickButtonAccordingToCurrentInput(SELECT_COORD);
				break;
			case UNKNOWN:
			default:
				// everything stays false!
			}
			if (enabledAtAll) {
				setBorder(NORMAL_BORDER);
			}
		} else {
			unset(true);
		}
	}

	/**
	 * This method executes the button that corresponds to the current input
	 * type that is set in the given location holder. This simulates the
	 * re-execution of the selection choice by the user when the UI state is
	 * restored from the location holder.
	 * @param defaultButton
	 *            The button that shall be clicked if there is no input type set
	 *            yet.
	 */
	private void clickButtonAccordingToCurrentInput(
			final JRadioButton defaultButton) {
		JRadioButton button;
		if (currentInputType != null) {
			switch (currentInputType) {
			case GEO_COORDINATE:
				button = SELECT_COORD;
				break;
			case LINE:
				button = SELECT_LINE;
				break;
			case POS_OFFSET:
				button = SELECT_POFF;
				break;
			case NEG_OFFSET:
				button = SELECT_NOFF;
				break;
			default:
				throw new IllegalStateException("Unexpected input type : "
						+ currentInputType);
			}
			
			if (!button.isEnabled()) {
			    button = defaultButton;
			}
		} else {
			button = defaultButton;
		}

		button.doClick();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setEnabled(final boolean enabled) {
		super.setEnabled(enabled);
		if (enabled) {
			setState(currentState);

		} else {
			unset(false);
			setBorder(GRAY_BORDER);
		}
	}

	/**
	 * Resets all the UI sub-elements to the state it has when it contains no
	 * location data.
	 * 
	 * @param clear
	 *            If {@code true} the current selection shall be cleared to, not
	 *            just set disabled
	 */
	private void unset(final boolean clear) {

		Enumeration<AbstractButton> buttons = SELECTION_GROUP.getElements();
		while (buttons.hasMoreElements()) {
			buttons.nextElement().setEnabled(false);
		}

		ENTER_DATA_BTN.setEnabled(false);

		setBorder(GRAY_BORDER);
		if (clear) {
			SELECTION_GROUP.clearSelection();
			currentInputType = null;
		}
		
	}

	/**
	 * The Class SelectionTypePanelAction.
	 */
	private static final class SelectionTypePanelAction implements
			ActionListener {

		/** The one that provides the current state. */
		private final SelectionTypePanel stateKeeper;

		/**
		 * Instantiates a new selection type panel action.
		 * 
		 * @param stateProvider
		 *            the one that provides the current location selection state
		 */
		public SelectionTypePanelAction(final SelectionTypePanel stateProvider) {
			stateKeeper = stateProvider;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public synchronized void actionPerformed(final ActionEvent e) {

			Object sourceButton = e.getSource();
			if (sourceButton == SELECT_LINE) {
				stateKeeper.currentInputType = InputType.LINE;
			} else if (sourceButton == SELECT_POFF) {
				stateKeeper.currentInputType = InputType.POS_OFFSET;
			} else if (sourceButton == SELECT_NOFF) {
				stateKeeper.currentInputType = InputType.NEG_OFFSET;
			} else if (sourceButton == SELECT_COORD) {
				stateKeeper.currentInputType = InputType.GEO_COORDINATE;
			}
		}
	}

	/**
	 * The Class SelectionTypePanelAction.
	 */
	private static final class EnterDataActionListener implements
			ActionListener {

		/** The one that provides the current state. */
		private final SelectionTypePanel stateKeeper;

		/**
		 * Instantiates a new enter data action.
		 * 
		 * @param stateProvider
		 *            the one that provides the current location selection state
		 */
		public EnterDataActionListener(final SelectionTypePanel stateProvider) {
			stateKeeper = stateProvider;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public synchronized void actionPerformed(final ActionEvent e) {

			Object sourceButton = e.getSource();
			LocationHolder locationHolder = stateKeeper.currentState;

			if (sourceButton == ENTER_DATA_BTN) {
				if (SELECT_LINE.isSelected()) {
					String value = JOptionPane.showInputDialog(null,
							"Please enter line ID", "Enter line ID",
							JOptionPane.QUESTION_MESSAGE);
					if (value != null) {
						try {
							long id = Long.parseLong(value);
							locationHolder.addOrRemoveLine(id);
						} catch (NumberFormatException nfe) {
							JOptionPane.showMessageDialog(null,
									"Invalid value", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				} else if (SELECT_COORD.isSelected()) {
					JPanel panel = new JPanel();
					panel.add(new JLabel("Longitude: "));
					JTextField lonField = new JTextField();
					lonField.setColumns(8);
					panel.add(lonField);
					panel.add(Box.createHorizontalStrut(5));
					panel.add(new JLabel("Latitude: "));
					JTextField latField = new JTextField();
					latField.setColumns(8);
					panel.add(latField);
					int retValue = JOptionPane.showConfirmDialog(null, panel,
							"Please enter the coordinate.",
							JOptionPane.OK_CANCEL_OPTION);
					if (retValue == 0) {
						try {
							Double lon = Double.parseDouble(lonField.getText());
							Double lat = Double.parseDouble(latField.getText());
							try {
								locationHolder.setGeoCoordPoi(lon, lat);
							} catch (InvalidMapDataException e1) {
								JOptionPane
										.showMessageDialog(
												null,
												"Invalid value. The coordinates are out of bounds.",
												"Error",
												JOptionPane.ERROR_MESSAGE);
							}
						} catch (NumberFormatException nfe) {
							JOptionPane.showMessageDialog(null,
									"Invalid value", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				} else if (SELECT_NOFF.isSelected()) {
					int currNegOff = locationHolder.getNegOff();
					int newNegOff = askForOffsetValue(currNegOff, false);
					try {
						if (newNegOff >= 0) {
							locationHolder.setNegativeOffset(newNegOff);
						}
					} catch (Exception nfe) {
						JOptionPane.showMessageDialog(null, "Invalid value",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				} else if (SELECT_POFF.isSelected()) {
					int currPosOff = locationHolder.getPosOff();
					int newPosOff = askForOffsetValue(currPosOff, true);
					if (newPosOff >= 0) {
						try {
							locationHolder.setPositiveOffset(newPosOff);
						} catch (InvalidMapDataException e1) {
							JOptionPane.showMessageDialog(null,
									"Invalid value", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}

				} else if (ENTER_ROWS_COLS.isSelected()) {
					int currRows = locationHolder.getRows();
					int currColumns = locationHolder.getColumns();
					JPanel panel = new JPanel();
					panel.add(new JLabel("Rows: "));
					JTextField rowsField = new JTextField();
					rowsField.setColumns(8);
					if (currRows != -1) {
						rowsField.setText(Integer.toString(currRows));
					}
					panel.add(rowsField);
					panel.add(Box.createHorizontalStrut(5));
					panel.add(new JLabel("Columns: "));
					JTextField columnsField = new JTextField();
					columnsField.setColumns(8);
					if (currColumns != -1) {
						columnsField.setText(Integer.toString(currColumns));
					}
					panel.add(columnsField);
					int retValue = JOptionPane.showConfirmDialog(null, panel,
							"Please enter rows and columns.",
							JOptionPane.OK_CANCEL_OPTION);
					if (retValue == 0) {
						try {
							Integer nrows = Integer.parseInt(rowsField
									.getText());
							Integer ncols = Integer.parseInt(columnsField
									.getText());
							if ((nrows >= 1 && ncols >= 2)
									|| (nrows >= 2 && ncols >= 1)) {
								locationHolder.setRowsColumns(nrows, ncols);
							} else {
								JOptionPane
										.showMessageDialog(
												null,
												"Invalid rows/coliumns values. At least one value must be greater than 1.",
												"Error",
												JOptionPane.ERROR_MESSAGE);
							}
						} catch (NumberFormatException nfe) {
							JOptionPane
									.showMessageDialog(
											null,
											"Invalid value. Only integer values are allowed.",
											"Error", JOptionPane.ERROR_MESSAGE);
						} catch (InvalidMapDataException e2) {
							JOptionPane.showMessageDialog(null,
									"Invalid value for rows and columns",
									"Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				}

				else if (ENTER_RADIUS.isSelected()) {
					String value = JOptionPane
							.showInputDialog(null,
									"Please enter circle radius in meter",
									"Enter circle radius",
									JOptionPane.QUESTION_MESSAGE);
					if (value != null) {
						try {
							int radius = Integer.parseInt(value);
							if (radius > 0) {
								locationHolder.setAreaRadius(radius);
							} else {
								JOptionPane
										.showMessageDialog(
												null,
												"Invalid value. The radius must be greater than 0 ",
												"Error",
												JOptionPane.ERROR_MESSAGE);
							}

						} catch (NumberFormatException nfe) {
							JOptionPane.showMessageDialog(null,
									"Invalid value", "Error",
									JOptionPane.ERROR_MESSAGE);
						} catch (InvalidMapDataException e2) {
							JOptionPane.showMessageDialog(null,
									"Invalid value for radius", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		}

		private int askForOffsetValue(int current, boolean isPositive) {
			String currVal = "";
			if (current != -1) {
				currVal = Integer.toString(current);
			}
			String direction = "negative";
			if (isPositive) {
				direction = "positive";
			}
			String value = (String) JOptionPane.showInputDialog(null,
					"Please enter " + direction + " offset in meter", "Enter "
							+ direction + " offset",
					JOptionPane.QUESTION_MESSAGE, null, null, currVal);
			if (value != null) {
				try {
					return Integer.parseInt(value);
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(null, "Invalid value",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			return -1;
		}

	}
}
