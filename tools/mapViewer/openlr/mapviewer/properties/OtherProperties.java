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
package openlr.mapviewer.properties;

/**
 * Enumeration that specifies other properties of the MapViewer application.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public enum OtherProperties implements MapViewerProperty {

	/** The command to start a web browser for the current user. */
	BROWSER_COMMAND("browser.command"),
	
	/**
	 * Toggles display of encoder log
	 */
	SHOW_ENCODER_LOG("show.encoder.log"),
	
	/**
	 * Toggles display of decoder log
	 */
	SHOW_DECODER_LOG("show.decoder.log");

	
	/**
	 * The property key.
	 */
	private final String keyString;

	/**
	 * Creates an instance.
	 * @param key The property key.
	 */
	private OtherProperties(final String key) {
		keyString = key;
	}

	/**
	 * @return The key of the property..
	 */
	@Override
    public String key() {
		return keyString;
	}
}
