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
package openlr.mapviewer.gui.panels.filtering;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import net.miginfocom.swing.MigLayout;
import openlr.mapviewer.gui.layer.MapLayerStore;

/**
 * The class FilterPanel is the main part of the filter frame. This panel
 * collects the sub-panels for filter options (frc, fow and nodes).
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class FilterPanel extends JPanel {

	/** Serial ID. */
	private static final long serialVersionUID = -7642557170039389123L;

	/** The frcBtn. */
	private final JRadioButton FRC_BTN = new JRadioButton("FRC filter");

	/** The fowBtn. */
	private final JRadioButton FOW_BTN = new JRadioButton("FOW filter");

	/** The allBtn. */
	private final JRadioButton ALL_BTN = new JRadioButton("No filter");

	/** The frc sub-panel. */
	private final FRCFilterPanel frcPanel;

	/** The fow sub-panel. */
	private final FOWFilterPanel fowPanel;

	/** The nodeBox. */
	private final JCheckBox NODE_BOX = new JCheckBox("Nodes", false);
	
	/**
	 * A reference to the assigned map layer store
	 */
	private final MapLayerStore maplayerStore;

	/**
	 * Instantiates a new filter panel using the map store ms.
	 * 
	 * @param ms
	 *            the map store
	 */
	public FilterPanel(final MapLayerStore ms) {
	    
		super(new MigLayout("insets 1"));
		maplayerStore = ms;
		
		setBorder(BorderFactory.createTitledBorder("Filter options"));
		frcPanel = new FRCFilterPanel(ms);
		fowPanel = new FOWFilterPanel(ms);

		add(FRC_BTN);
		add(frcPanel, "wrap");
		add(FOW_BTN);
		add(fowPanel, "wrap");
		add(ALL_BTN, "wrap");
		NODE_BOX.addItemListener(new NodeFilterListener(ms));
		add(NODE_BOX);

		ButtonGroup group = new ButtonGroup();
		group.add(FRC_BTN);
		group.add(FOW_BTN);
		group.add(ALL_BTN);

		FilterPanelListener fpl = new FilterPanelListener(ms);

		FRC_BTN.addItemListener(fpl);
		FOW_BTN.addItemListener(fpl);
		ALL_BTN.addItemListener(fpl);

		ALL_BTN.doClick();
	}
	
	/**
	 * Deactivates all highlight layers provided by this filter panel
	 */
	final void unset() {
        maplayerStore.resetFilter();
	}

	/**
	 * Listener for radio button events in the filter panel. If the selected
	 * button changes, the map layer will be updated accordingly.
	 * 
	 */
	private class FilterPanelListener implements ItemListener {

		/** The map store. */
		private final MapLayerStore mapStore;

		/**
		 * Instantiates a new filter panel listener.
		 * 
		 * @param ms
		 *            the map store
		 */
		public FilterPanelListener(final MapLayerStore ms) {
			mapStore = ms;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final void itemStateChanged(final ItemEvent e) {
			Object o = e.getSource();
			if (o == FRC_BTN && e.getStateChange() == ItemEvent.SELECTED) {
				frcPanel.activatePanel();
				fowPanel.deactivatePanel();
				mapStore.activateFRCLayers(frcPanel.getFilterStatus());
			} else if (o == FOW_BTN && e.getStateChange() == ItemEvent.SELECTED) {
				fowPanel.activatePanel();
				frcPanel.deactivatePanel();
				mapStore.activateFOWLayers(fowPanel.getFilterStatus());
			} else if (o == ALL_BTN && e.getStateChange() == ItemEvent.SELECTED) {
				mapStore.resetFilter();
				frcPanel.deactivatePanel();
				fowPanel.deactivatePanel();
			}
			if (NODE_BOX.isSelected()) {
				mapStore.showNodeLayer();
			}
		}
	}

	/**
	 * Listener for activating the node layer. If selected then nodes will be
	 * drawn otherwise no nodes are visible.
	 * 
	 */
	private static class NodeFilterListener implements ItemListener {

		/** The map store. */
		private final MapLayerStore mapStore;

		/**
		 * Instantiates a new node filter listener.
		 * 
		 * @param ms
		 *            the map store
		 */
		NodeFilterListener(final MapLayerStore ms) {
			mapStore = ms;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final void itemStateChanged(final ItemEvent e) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				mapStore.hideNodeLayer();
			} else {
				mapStore.showNodeLayer();
			}
		}
	}

}
