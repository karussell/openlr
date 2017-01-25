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

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;

import net.miginfocom.swing.MigLayout;
import openlr.mapviewer.MapsHolder;
import openlr.mapviewer.MapsHolder.MapIndex;

/**
 * The Class MapPanelsHolder.
 */
public class MapPanelsHolder extends JPanel implements ItemListener {

	/**
	 * the resize weight for the split panel.
	 */
	private static final double RESIZE_WEIGHT = 0.5;

	/**
	 * 
	 */
	private static final long serialVersionUID = -8218225428401413955L;

	/** The frcBtn. */
	private final JRadioButton firstMapBtn;

	/** The frcBtn. */
	private final JRadioButton secondMapBtn;
	
	/** The maps holder. */
	private final MapsHolder mapsHolder;


	/**
	 * Instantiates a new map panels holder.
	 * 
	 * @param mapsH
	 *            the maps h
	 */
	public MapPanelsHolder(final MapsHolder mapsH) {
		setLayout(new MigLayout("insets 0", "[grow]", "[grow][grow]"));
		mapsHolder = mapsH;
		if (mapsH.hasTwoMaps()) {
            firstMapBtn = new JRadioButton(mapsH.getMapName(MapIndex.FIRST_MAP));
            secondMapBtn = new JRadioButton(
                    mapsH.getMapName(MapIndex.SECOND_MAP));
			JPanel mapPanel = new JPanel(new MigLayout("insets 2", "[grow]",
					"[][grow]"));
			JPanel mapPanel2 = new JPanel(new MigLayout("insets 2", "[grow]",
					"[][grow]"));

			mapPanel.add(firstMapBtn, "wrap");
            mapPanel.add(mapsH.getMapPane(MapIndex.FIRST_MAP).getJMapPane(),
                    "grow");
			mapPanel2.add(secondMapBtn, "wrap");
            mapPanel2.add(mapsH.getMapPane(MapIndex.SECOND_MAP).getJMapPane(),
                    "grow");
			
			ButtonGroup group = new ButtonGroup();
			group.add(firstMapBtn);
			group.add(secondMapBtn);
			firstMapBtn.addItemListener(this);
			secondMapBtn.addItemListener(this);
			firstMapBtn.setSelected(true);
			
			JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
					mapPanel, mapPanel2);
			splitPane.setResizeWeight(RESIZE_WEIGHT);
			add(splitPane, "grow, wrap, span 1 3");
		} else {
			JPanel mapPanel = new JPanel(new MigLayout("insets 2", "[grow]",
					"[grow]"));
			mapPanel.setBorder(BorderFactory.createTitledBorder("Map: "
					+ mapsH.getMapName(MapIndex.FIRST_MAP)));
            mapPanel.add(mapsH.getMapPane(MapIndex.FIRST_MAP).getJMapPane(),
                    "grow");
			add(mapPanel, "grow, wrap, span 1 3");
			firstMapBtn = null;
			secondMapBtn = null;
		}
		mapsHolder.setMapActive(MapIndex.FIRST_MAP);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void itemStateChanged(final ItemEvent e) {
		Object o = e.getSource();
		if (o == firstMapBtn && e.getStateChange() == ItemEvent.SELECTED) {
			mapsHolder.setMapActive(MapIndex.FIRST_MAP);
	        firstMapBtn.setForeground(null);
	        secondMapBtn.setForeground(Color.GRAY);
		} else if (o == secondMapBtn && e.getStateChange() == ItemEvent.SELECTED) {
			mapsHolder.setMapActive(MapIndex.SECOND_MAP);
			firstMapBtn.setForeground(Color.GRAY);
			secondMapBtn.setForeground(null);
		}		
	}

}
