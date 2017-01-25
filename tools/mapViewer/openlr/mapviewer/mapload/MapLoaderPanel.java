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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import openlr.map.loader.OpenLRMapLoader;

/**
 * The Class MapLoaderPanel.
 */
public class MapLoaderPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8983290672564547770L;

	/** The parent. */
	private final JDialog parent;

	/** The Constant MAP_LOADER_SELECTION. */
	private final JComboBox mapLoaderSelection = new JComboBox();

	/** The map name field. */
	private final JTextField mapNameField = new JTextField(30);

	/** The selected loader. */
	private HashMap<Integer, MapLoadParameterPanel> selectedLoader = new HashMap<Integer, MapLoadParameterPanel>();

	/** The loader panel holder. */
	private final JPanel loaderPanelHolder = new JPanel(new MigLayout("",
			"[grow]", ""));

	/** The map loaders. */
	private final List<OpenLRMapLoader> loader;

	/**
	 * Instantiates a new map loader panel.
	 * 
	 * @param p
	 *            the p
	 * @param l
	 *            the l
	 * @param mapIndex
	 *            the map index
	 */
	public MapLoaderPanel(final JDialog p, final List<OpenLRMapLoader> l,
			final int mapIndex) {
		parent = p;
		loader = l;
		setLayout(new MigLayout());
		String title = "Map " + Integer.toString(mapIndex);
		setBorder(BorderFactory.createTitledBorder(title));
		MapLoadDialogListener mldl = new MapLoadDialogListener();
		mapLoaderSelection.addItemListener(mldl);
		int index = 0;
		for (OpenLRMapLoader ol : loader) {
			Map<Integer, String> values = MapLoadOptionRW.loadParameter(ol,
					mapIndex);
			selectedLoader.put(index, new MapLoadParameterPanel(ol, values));
			mapLoaderSelection.addItem(ol.getName());
			index++;
		}
		mapLoaderSelection.setSelectedIndex(0);

		add(new JLabel("Select loader: "));
		add(mapLoaderSelection);
		add(new JLabel("Map name: "));
		add(mapNameField, "wrap");
		loaderPanelHolder.setBorder(BorderFactory
				.createTitledBorder("Parameter"));

		loaderPanelHolder.add(selectedLoader.get(0), "wrap");

		add(loaderPanelHolder, "grow, wrap, span 4");
	}

	/**
	 * Listener for changes of the map loader implementation.
	 * 
	 * @see MapLoadDialogEvent
	 */
	private final class MapLoadDialogListener implements ItemListener {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void itemStateChanged(final ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				int index = mapLoaderSelection.getSelectedIndex();
				loaderPanelHolder.removeAll();
				loaderPanelHolder.add(selectedLoader.get(index), "wrap");
				parent.pack();
			}
		}
	}

	/**
	 * Gets the selected loader.
	 * 
	 * @return the selected loader
	 */
	public final OpenLRMapLoader getSelectedLoader() {
		return loader.get(mapLoaderSelection.getSelectedIndex());
	}

	/**
	 * Gets the parameter.
	 * 
	 * @return the parameter
	 */
	public final Map<Integer, String> getParameterValues() {
		return selectedLoader.get(mapLoaderSelection.getSelectedIndex())
				.getParameterValues();
	}

	/**
	 * Gets the all parameter.
	 * 
	 * @return the all parameter
	 */
	public final Map<OpenLRMapLoader, Map<Integer, String>> getAllParameter() {
		Map<OpenLRMapLoader, Map<Integer, String>> values = new HashMap<OpenLRMapLoader, Map<Integer, String>>();
		int index = 0;
		for (OpenLRMapLoader l : loader) {
			values.put(l, selectedLoader.get(index).getParameterValues());
			index++;
		}
		return values;
	}

	/**
	 * Check parameter.
	 * 
	 * @return true, if successful
	 */
	public final boolean checkParameter() {
		return selectedLoader.get(mapLoaderSelection.getSelectedIndex())
				.checkParameter();
	}

	/**
	 * Gets the map name.
	 * 
	 * @return the map name
	 */
	public final String getMapName() {
		return mapNameField.getText();
	}

}
