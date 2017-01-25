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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import openlr.map.loader.MapLoadParameter;
import openlr.map.loader.OpenLRMapLoader;

/**
 * The Class MapLoadOptionRW.
 */
public final class MapLoadOptionRW {

	/** The Constant PARAMS_FILENAME. */
	private static final String PARAMS_FILENAME = "mapload.properties";

	/**
	 * Utility class shall not be instantiated.
	 */
	private MapLoadOptionRW() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Load parameter.
	 * 
	 * @param loader
	 *            the loader
	 * @param mapIndex
	 *            the map index
	 * @return the map
	 */
	public static Map<Integer, String> loadParameter(
			final OpenLRMapLoader loader, final int mapIndex) {
		File paramFile = new File(".", PARAMS_FILENAME);
		Map<Integer, String> storedValues = new HashMap<Integer, String>();
		if (paramFile.exists()) {
			try {
				BufferedReader br = new BufferedReader(
						new FileReader(paramFile));
				MapLoadParameterValues values = null;
				while ((values = readNextEntry(br)) != null) {
					if (values.getName().equals(loader.getName())) {
						for (MapLoadParameter p : loader.getParameter()) {
							String s = values.getParamValue(p.getIdentifier(),
									mapIndex);
							if (s != null) {
								storedValues.put(p.getIdentifier(), s);
							}
						}
					}
				}
				br.close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,
						"Cannot read the map load parameter!", "Error",
						JOptionPane.ERROR_MESSAGE, null);
			}
		}
		return storedValues;
	}

	/**
	 * Read next entry.
	 *
	 * @param br the br
	 * @return the map load parameter values
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static MapLoadParameterValues readNextEntry(final BufferedReader br)
			throws IOException {
		String line = null;
		MapLoadParameterValues values = null;
		String name = null;
		int currIdx = -1;
		int mapIndex = -1;
		int number = 0;
		int count = 0;
		while ((line = br.readLine()) != null) {
			if (line.isEmpty()) {
				if (currIdx != -1) {
					values.setParamValue(currIdx, mapIndex, line);
					currIdx = -1;
					count++;
				} else {
					continue;
				}
			} else if (name == null) {
				name = line;
				continue;
			} else if (values == null) {
				number = Integer.parseInt(line);
				values = new MapLoadParameterValues(name, number);
			} else if (currIdx == -1) {
				String[] indices = line.split("--");
				currIdx = Integer.parseInt(indices[0]);
				mapIndex = Integer.parseInt(indices[1]);
			} else {
				values.setParamValue(currIdx, mapIndex, line);
				currIdx = -1;
				count++;
			}
			if (count == number) {
				break;
			}
		}
		return values;
	}

	/**
	 * The Class MapLoadParameterValues.
	 */
	private static final class MapLoadParameterValues {

		/** The name. */
		private final String name;

		/** The param values. */
		private final HashMap<String, String> paramValues;

		/**
		 * Instantiates a new map load parameter values.
		 * 
		 * @param n
		 *            the n
		 * @param numberParams
		 *            the number params
		 */
		MapLoadParameterValues(final String n, final int numberParams) {
			name = n;
			paramValues = new HashMap<String, String>();
		}

		/**
		 * Sets the param value.
		 * 
		 * @param idx
		 *            the idx
		 * @param mapIndex
		 *            the map index
		 * @param val
		 *            the val
		 */
		void setParamValue(final int idx, final int mapIndex, final String val) {
			paramValues.put(resolveKey(idx, mapIndex), val);
		}

		/**
		 * Gets the name.
		 * 
		 * @return the name
		 */
		String getName() {
			return name;
		}

		/**
		 * Gets the param value.
		 * 
		 * @param idx
		 *            the idx
		 * @param mapIndex
		 *            the map index
		 * @return the param value
		 */
		String getParamValue(final int idx, final int mapIndex) {
			String key = resolveKey(idx, mapIndex);
			if (paramValues.containsKey(key)) {
				return paramValues.get(key);
			}
			return null;
		}
	}

	/**
	 * Resolve key.
	 * 
	 * @param idx
	 *            the idx
	 * @param mapIndex
	 *            the map index
	 * @return the string
	 */
	private static String resolveKey(final int idx, final int mapIndex) {
		return Integer.toString(idx) + "--" + Integer.toString(mapIndex);
	}

	/**
	 * Save parameter.
	 * 
	 * @param params
	 *            the params
	 */
	public static void saveParameter(
			final List<Map<OpenLRMapLoader, Map<Integer, String>>> params) {
		File paramFile = new File(".", PARAMS_FILENAME);
		if (paramFile.exists() && !paramFile.delete()) {
			JOptionPane.showMessageDialog(null,
					"Cannot save the map load parameters!", "Error",
					JOptionPane.ERROR_MESSAGE, null);
		}
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(paramFile));
			int mapIndex = 0;
			for (Map<OpenLRMapLoader, Map<Integer, String>> set : params) {
				mapIndex++;
				for (Entry<OpenLRMapLoader, Map<Integer, String>> entry : set.entrySet()) {
				    OpenLRMapLoader loader = entry.getKey();
				    Map<Integer, String> loaderParams = entry.getValue();
					StringBuilder sb = new StringBuilder();
					sb.append(loader.getName()).append("\n");
					sb.append(loader.getNumberOfParams()).append("\n");
					for (Entry<Integer, String>  p : loaderParams.entrySet()) {
						sb.append(resolveKey(p.getKey().intValue(), mapIndex))
								.append("\n");
						String v = p.getValue();
						if (v != null) {
							sb.append(v).append("\n");
						} else {
							sb.append("\n");
						}
					}
					pw.write(sb.toString());
					pw.flush();
				}
			}
			pw.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"Cannot save the map load parameter!", "Error",
					JOptionPane.ERROR_MESSAGE, null);
		}
	}

}
