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
package openlr.mapviewer.mapload;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import net.miginfocom.swing.MigLayout;
import openlr.map.loader.MapLoadParameter;
import openlr.map.loader.OpenLRMapLoader;
import openlr.mapviewer.gui.MapViewerGui;

/**
 * The MapLoadDialog lets the user to choose between all map loader
 * implementations found in the class path. The corresponding parameter are
 * shown after selecting a map loader implementation. It will be checked whether
 * all required parameter are set before starting the map load process.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class MapLoadDialog extends JDialog {

	/**
	 * The Enum ReturnCode.
	 */
	public enum ReturnCode {

		/** The LOAD. */
		LOAD,

		/** The ABORT. */
		ABORT;
	}

	/**
	 * The loader options.
	 */
	private enum LoaderOptions {

		/** The ON e_ map. */
		ONE_MAP,
		/** The TW o_ map s. */
		TWO_MAPS;
	}

	/**
	 * The Enum MapNumber.
	 */
	public enum MapNumber {

		/** The one. */
		ONE,

		/** The two. */
		TWO;
	}

	/** Serial ID. */
	private static final long serialVersionUID = -528457772259092837L;

	/** The Constant OK_BTN. */
	private static final JButton OK_BTN = new JButton("OK");

	/** The Constant CANCEL_BTN. */
	private static final JButton CANCEL_BTN = new JButton("Cancel");

	/** The return code. */
	private ReturnCode returnCode = ReturnCode.ABORT;

	/** The current loader option. */
	private LoaderOptions loaderOption = LoaderOptions.ONE_MAP;

	/** The required param label. */
	private static final JLabel REQUIRED_PARAM_LABEL = new JLabel(
			"* required parameter");

	/** The map loader panel1. */
	private final MapLoaderPanel mapLoaderPanel1;

	/** The map loader panel2. */
	private final MapLoaderPanel mapLoaderPanel2;

	/**
	 * Creates a new map load dialog with a list of map loader implementation.
	 * 
	 * @param loader
	 *            the loader
	 */
	public MapLoadDialog(final List<OpenLRMapLoader> loader) {
		setTitle("Select map loader and parameters...");
		setIconImage(MapViewerGui.OPENLR_ICON);
		JPanel panel = new JPanel(new MigLayout("",
				"[left][left,grow][center][right]", "[][]"));
		setContentPane(panel);

		MapLoadDialogListener mldl = new MapLoadDialogListener();

		JPanel optionsPanel = new JPanel(new MigLayout());
		optionsPanel.setBorder(BorderFactory
				.createTitledBorder("Loader Options"));
		JRadioButton rB1 = new JRadioButton("Load one map");
		JRadioButton rB2 = new JRadioButton("Load two maps");
		rB1.setActionCommand("one");
		rB2.setActionCommand("two");
		rB1.addActionListener(mldl);
		rB2.addActionListener(mldl);
		rB1.setSelected(true);
		ButtonGroup bGroup = new ButtonGroup();
		bGroup.add(rB1);
		bGroup.add(rB2);
		optionsPanel.add(rB1);
		optionsPanel.add(rB2);

		OK_BTN.addActionListener(mldl);
		CANCEL_BTN.addActionListener(mldl);
		OK_BTN.setActionCommand("ok");
		CANCEL_BTN.setActionCommand("cancel");
		optionsPanel.add(OK_BTN);
		optionsPanel.add(CANCEL_BTN);
		add(optionsPanel, "grow, wrap");
		mapLoaderPanel1 = new MapLoaderPanel(this, loader, 1);
		mapLoaderPanel2 = new MapLoaderPanel(this, loader, 2);
		add(mapLoaderPanel1, "wrap");
		add(REQUIRED_PARAM_LABEL);
		pack();
		setLocationRelativeTo(null);
	}

	/**
	 * Gets the return code.
	 * 
	 * @return the return code
	 */
	public final ReturnCode getReturnCode() {
		return returnCode;
	}

	/**
	 * Gets the map loader selection index.
	 * 
	 * @param map
	 *            the map
	 * @return the map loader selection index
	 */
	public final OpenLRMapLoader getMapLoaderSelectionIndex(final MapNumber map) {
		OpenLRMapLoader loader = null;
		switch (map) {
		default:
		case ONE:
			loader = mapLoaderPanel1.getSelectedLoader();
			break;
		case TWO:
			loader = mapLoaderPanel2.getSelectedLoader();
			break;
		}
		return loader;
	}

	/**
	 * Two maps selected.
	 * 
	 * @return true, if successful
	 */
	public final boolean twoMapsSelected() {
		return loaderOption == LoaderOptions.TWO_MAPS;
	}

	/**
	 * Listener for changes of the map loader implementation.
	 * 
	 * @see MapLoadDialogEvent
	 */
	private final class MapLoadDialogListener implements ActionListener {

		/**
		 * {@inheritDoc}
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void actionPerformed(final ActionEvent e) {
			String aCommand = e.getActionCommand();
			if ("ok".equals(aCommand)) {
				boolean parameterOK = false;
				switch (loaderOption) {
				case ONE_MAP:
				default:
					parameterOK = mapLoaderPanel1.checkParameter();
					break;
				case TWO_MAPS:
					parameterOK = mapLoaderPanel1.checkParameter()
							&& mapLoaderPanel2.checkParameter();
					break;
				}
				if (parameterOK) {
					// save parameter
					List<Map<OpenLRMapLoader, Map<Integer, String>>> params = new ArrayList<Map<OpenLRMapLoader, Map<Integer, String>>>();
					params.add(mapLoaderPanel1.getAllParameter());
					params.add(mapLoaderPanel2.getAllParameter());
					MapLoadOptionRW.saveParameter(params);
					returnCode = ReturnCode.LOAD;
					setVisible(false);
				} else {
					JOptionPane
							.showMessageDialog(
									null,
									"Parameter input is not valid or a parameter is missing",
									"Invalid parameter",
									JOptionPane.ERROR_MESSAGE);
				}
			} else if ("cancel".equals(aCommand)) {
				returnCode = ReturnCode.ABORT;
				setVisible(false);
			} else if ("one".equals(aCommand)) {
				remove(mapLoaderPanel2);
				loaderOption = LoaderOptions.ONE_MAP;
				pack();
			} else if ("two".equals(aCommand)) {
				remove(REQUIRED_PARAM_LABEL);
				add(mapLoaderPanel2, "wrap");
				add(REQUIRED_PARAM_LABEL);
				loaderOption = LoaderOptions.TWO_MAPS;
				pack();
			}
		}
	}

	/**
	 * Gets the parameters.
	 * 
	 * @param map
	 *            the map
	 * @return the parameters
	 */
	public final Collection<MapLoadParameter> getParameters(final MapNumber map) {
		Map<Integer, String> values = null;
		OpenLRMapLoader loader = null;
		switch (map) {
		default:
		case ONE:
			values = mapLoaderPanel1.getParameterValues();
			loader = mapLoaderPanel1.getSelectedLoader();
			break;
		case TWO:
			values = mapLoaderPanel2.getParameterValues();
			loader = mapLoaderPanel2.getSelectedLoader();
			break;
		}
		Collection<MapLoadParameter> params = new ArrayList<MapLoadParameter>();
		for (MapLoadParameter p : loader.getParameter()) {
			p.setValue(values.get(p.getIdentifier()));
			params.add(p);
		}
		return params;
	}

	/**
	 * Gets the map name.
	 * 
	 * @param map
	 *            the map
	 * @return the map name
	 */
	public final String getMapName(final MapNumber map) {
		String name = null;
		switch (map) {
		default:
		case ONE:
			name = mapLoaderPanel1.getMapName();
			break;
		case TWO:
			name = mapLoaderPanel2.getMapName();
			break;
		}
		return name;
	}

}
