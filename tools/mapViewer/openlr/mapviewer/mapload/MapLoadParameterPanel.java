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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import openlr.map.loader.MapLoadParameter;
import openlr.map.loader.OpenLRMapLoader;

/**
 * The MapLoadParameterPanel shows all parameter which are needed to load map
 * data with a specific map loader implementation. It also checks whether all
 * required parameter are set or not.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class MapLoadParameterPanel extends JPanel {

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 7833346710991982429L;

	/** The params. */
	private final List<SingleParameter> params = new ArrayList<SingleParameter>();
	
	/** The loader. */
	private final OpenLRMapLoader loader;
	

	/**
	 * Instantiates a new map load parameter panel for a map loader
	 * implementation.
	 *
	 * @param l the loader
	 * @param values the values
	 */
	public MapLoadParameterPanel(final OpenLRMapLoader l, final Map<Integer, String> values) {
		super(new MigLayout("", "", ""));
		loader = l;
		Collection<MapLoadParameter> parameters = loader.getParameter();
		for (MapLoadParameter mlp : parameters) {
			String value = values.get(mlp.getIdentifier());
			SingleParameter sp = new SingleParameter(mlp, value);
			params.add(sp);
			add(sp, "wrap");
		}		
	}
	
	/**
	 * Gets the loader.
	 *
	 * @return the loader
	 */
	public final OpenLRMapLoader getLoader() {
		return loader;
	}

	/**
	 * Check if all required parameter are set.
	 * 
	 * @return true, if successful
	 */
	public final boolean checkParameter() {
		for (SingleParameter sp : params) {
			if (sp.getParameter().isRequired()
					&& (sp.getValue() == null || sp.getValue().isEmpty() || sp
							.getValue().equals(" "))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Gets the parameter values.
	 * 
	 * @return the parameter values
	 */
	public final Map<Integer, String> getParameterValues() {
		Map<Integer, String> returnValue = new HashMap<Integer, String>();
		for (SingleParameter sp : params) {
			MapLoadParameter param = sp.getParameter();
			returnValue.put(param.getIdentifier(), sp.getValue());
		}
		return returnValue;
	}
	
}
