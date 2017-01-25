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

import javax.swing.JPanel;

import cern.colt.map.OpenIntObjectHashMap;

/**
 * The class AbstractFilterPanel holds for each information type a layer selector. A layer
 * selector selects/deselects the corresponding map layer and it also holds the
 * current color used to draw the lines. 
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public abstract class AbstractFilterPanel extends JPanel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7751267295521109697L;
	
	/** The layer selectors. */
	protected LayerSelector[] layerSelectors;

	
	/**
	 * Gets the filter status of each layer.
	 * 
	 * @return the filter status
	 */
	public final OpenIntObjectHashMap getFilterStatus() {
		OpenIntObjectHashMap status = new OpenIntObjectHashMap();
		for (int i = 0; i < layerSelectors.length; i++) {
			status.put(layerSelectors[i].getID(), Boolean.valueOf(layerSelectors[i].isLayerActivated()));
		}
		return status;
	}
	
	/**
	 * Activate this panel.
	 */
	public final void activatePanel() {
		for (LayerSelector ls : layerSelectors) {
			ls.activate();
		}
	}

	/**
	 * Deactivate this panel.
	 */
	public final void deactivatePanel() {
		for (LayerSelector ls : layerSelectors) {
			ls.deactivate();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("status: {");
		for (int i = 0; i < layerSelectors.length; i++) {
			LayerSelector ls = layerSelectors[i];
			sb.append("[").append(ls).append("]");
			if (i < layerSelectors.length - 1) {
				sb.append(", ");
			}
		}
		sb.append("}");
		return sb.toString();
	}
	
}

