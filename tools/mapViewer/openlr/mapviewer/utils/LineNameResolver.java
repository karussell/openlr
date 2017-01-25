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
package openlr.mapviewer.utils;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The LineNameResolver is used to resolve a line name for a line in a proper
 * language (or locale).
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public final class LineNameResolver {
	
	/** the default locale for line names */
	private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
	
	/**
	 * Utility class shall not be instantiated.
	 */
	private LineNameResolver() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Resolves a line name. The method checks if a name in the current locale
	 * exists and if so it concatenates all names of that locale. If no names
	 * in the current locale exists the method tries the default locale. If also
	 * no default locale names exist then the first locale entry will be used.
	 * 
	 * @param names the names
	 * 
	 * @return the string
	 */
	public static String resolveLineName(final Map<Locale, List<String>> names) {
		Locale currentLocale = Locale.getDefault();
		StringBuilder sb = new StringBuilder();
		boolean found = false;
		if (names.containsKey(currentLocale)) {
			List<String> currentNames = names.get(currentLocale);
			if (currentNames != null && !currentNames.isEmpty()) {
				for (int i = 0; i < currentNames.size(); i++) {
					sb.append(currentNames.get(i));
					if (i != currentNames.size() - 1) {
						sb.append(", ");
					}
				}
				found = true;
			}
		} else if (!found && names.containsKey(DEFAULT_LOCALE)) {
			List<String> defaultNames = names.get(DEFAULT_LOCALE);
			if (defaultNames != null && !defaultNames.isEmpty()) {
				for (int i = 0; i < defaultNames.size(); i++) {
					sb.append(defaultNames.get(i));
					if (i != defaultNames.size() - 1) {
						sb.append(", ");
					}
				}
				found = true;
			}
		} else if (!found) {
			Collection<List<String>> allNames = names.values();
			for (List<String> nameList : allNames) {
				if (nameList != null && !nameList.isEmpty()) {
					for (int i = 0; i < nameList.size(); i++) {
						sb.append(nameList.get(i));
						if (i != nameList.size() - 1) {
							sb.append(", ");
						}
					}
					break;
				}
			}
		}
		return sb.toString();
	}
}
