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

import javax.swing.BorderFactory;

import net.miginfocom.swing.MigLayout;
import openlr.mapviewer.MapViewer;
import openlr.mapviewer.gui.layer.MapLayerStore;
import openlr.mapviewer.gui.layer.MapLayerStore.MapFilter;

/**
 * The Class FRCFilterPanel holds for each functional road class a layer selector. 
 * A layer selector selects/deselects the corresponding map layer and it also holds the
 * current color used to draw the lines.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class FRCFilterPanel extends AbstractFilterPanel {	
	
	/** The Constant NUMBER_FRC. */
	private static final int NUMBER_FRC = 8;
	
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = -439736841842060551L;

	/** The toolTips. */
	private static final String[] TOOLTIPS = {"main road",
											  "first class road",
											  "second class road",
											  "third class road",
											  "fourth class road",
											  "fifth class road",
											  "sixth class road",
											  "other class road"};
	
	/**
	 * Instantiates a new frc filter panel.
	 * 
	 * @param ms the map store
	 */
	FRCFilterPanel(final MapLayerStore ms) {
		setLayout(new MigLayout("insets 3, wrap 2"));

		layerSelectors = new LayerSelector[NUMBER_FRC];
		for (int i = 0; i < NUMBER_FRC; i++) {
			LayerSelector ls = new LayerSelector(i, MapFilter.FRC, "FRC" + i, TOOLTIPS[i], true, 
					MapViewer.PROPERTIES.getFrcColorProperty(i), ms);
			add(ls);
			layerSelectors[i] = ls;
		}
		setBorder(BorderFactory.createLineBorder(Color.black, 1));
	}

}
