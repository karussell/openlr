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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;
import openlr.mapviewer.MapViewer;
import openlr.mapviewer.gui.layer.MapLayerStore;
import openlr.mapviewer.gui.layer.MapLayerStore.MapFilter;
import openlr.mapviewer.gui.utils.VisibilityDependentPropertiesObservationListener;
import openlr.mapviewer.properties.MapViewerPropertiesObserver;

/**
 * The class LayerSelector represents a single filter option which can be
 * selected and deselected. It also holds the current color which can also be
 * changed.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class LayerSelector extends JPanel implements MapViewerPropertiesObserver {

	/** Serial ID. */
	private static final long serialVersionUID = 3109854915392689697L;

	/** The Constant WIDTH. */
	private static final int WIDTH = 10;

	/** The Constant HEIGHT. */
	private static final int HEIGHT = 10;

	/** The check box. */
	private final JCheckBox checkBox;

	/** The color btn. */
	private final JButton colorBtn;

	/** The id of this layer selector. */
	private final int id;
	
	/**
	 * The property key specifying the line color for this layer.
	 */
	private final String propKeyLineColor;

	/**
	 * The Constructor.
	 * @param i
	 *            the id
	 * @param t
	 *            the type
	 * @param n
	 *            the name
	 * @param tt
	 *            the tool tip
	 * @param selected
	 *            the selected
	 * @param ms
	 *            the ms map layer store
	 * @param lineColorProperty 
	 *            The property key specifying the line color for this layer.
	 */
	LayerSelector(final int i, final MapFilter t, final String n,
			final String tt, final boolean selected, final String lineColorProperty,
			final MapLayerStore ms) {
		super(new MigLayout("insets 0"));
		id = i;
		checkBox = new JCheckBox();
		checkBox.setSelected(selected);
		
		// use text area for the check box label to automatically wrap lines
		JTextArea label = new JTextArea(n);
		label.setLineWrap(true);
		label.setWrapStyleWord(true);
		label.setEditable(false);
		label.setBackground(getBackground());				
		label.setSelectionColor(getBackground());
		label.setToolTipText(tt);		
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				if (checkBox.isEnabled()) {
					checkBox.setSelected(!checkBox.isSelected());
				}
			}
		});
		
		colorBtn = new JButton();

		colorBtn.setBackground(MapViewer.PROPERTIES.getColorProperty(lineColorProperty));
		colorBtn.setMaximumSize(new Dimension(WIDTH, HEIGHT));

		LayerSelectorListener lsl = new LayerSelectorListener(n, ms, i, t, lineColorProperty);
		checkBox.addItemListener(lsl);
		colorBtn.addActionListener(lsl);


		checkBox.setToolTipText(tt);
		add(colorBtn);
		add(checkBox);
		add(label);

        addAncestorListener(new VisibilityDependentPropertiesObservationListener(this));
	
		propKeyLineColor = lineColorProperty;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public final int getID() {
		return id;
	}

	/**
	 * Checks if is layer activated.
	 * 
	 * @return true, if is layer activated
	 */
	public final boolean isLayerActivated() {
		return checkBox.isSelected();
	}

	/**
	 * Activate the layer.
	 */
	public final void activate() {
		checkBox.setEnabled(true);
		colorBtn.setEnabled(true);
	}

	/**
	 * Deactivate the layer.
	 */
	public final void deactivate() {
		checkBox.setEnabled(false);
		colorBtn.setEnabled(false);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void updateProperty(final String changedProperty) {
		// the color can be changed by a different dialog 
		if (propKeyLineColor.equals(changedProperty)) {
		  colorBtn.setBackground(MapViewer.PROPERTIES.getColorProperty(propKeyLineColor));	
		}
	}

	    /**
	 * Listener for selection and color changes.
	 * 
	 */
	private static class LayerSelectorListener implements ActionListener,
			ItemListener {

		/** The name. */
		private final String name;

		/** The color. */
		private Color color;

		/** The map store. */
		private final MapLayerStore mapStore;

		/** The id. */
		private final int id;

		/** The type. */
		private final MapFilter type;
		
		/**
		 * The property key specifying the line color for this layer.
		 */
		private final String propKeyLineColor;

		/**
		 * Instantiates a new layer selector listener.
		 * 
		 * @param n
		 *            the name
		 * @param ms
		 *            the map store
		 * @param i
		 *            the id
		 * @param t
		 *            the corresponding map filter
		 * @param propertyLineColor
		 *            The property key specifying the line color for this layer.
		 */
		LayerSelectorListener(final String n, 
				final MapLayerStore ms, final int i, final MapFilter t, 
				final String propertyLineColor) {
			name = n;
			mapStore = ms;
			id = i;
			type = t;
			propKeyLineColor = propertyLineColor;
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public final void actionPerformed(final ActionEvent e) {
			Color c = JColorChooser.showDialog(null,
					"Choose color for " + name, color);
			if (c != null) {
				((JButton) e.getSource()).setBackground(c);
                color = c;
				MapViewer.PROPERTIES.setColorProperty(propKeyLineColor, c);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final void itemStateChanged(final ItemEvent e) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				mapStore.deactivateFilterLayer(type, id);
			} else {
				mapStore.activateFilterLayer(type, id);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    public final String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("LayerSelector id: ").append(id);
		sb.append(" ").append(propKeyLineColor);
		if (!checkBox.isSelected()) { 
			sb.append(" not selected");
		} else {
			sb.append(" selected");
		}
		return sb.toString();
	}
}
