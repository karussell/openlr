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
package openlr.mapviewer;

import java.io.IOException;
import java.io.InputStream;

import openlr.map.InvalidMapDataException;
import openlr.mapviewer.gui.MapViewerGui;
import openlr.mapviewer.mapload.MapsLoader;
import openlr.mapviewer.properties.MapViewerProperties;

/**
 * The MapViewer is an OpenLR tool which draws a map based on the OpenLR map
 * package. It also enables the user to select a location and to encode it. The
 * location reference is shown and can be changed and decoded. Maps are loaded
 * using the map loader service and all implementations of this interface are
 * found during runtime.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public final class MapViewer {

	/** The Constant RUNTIME_PROPERTIES_FILE_NAME. */
	private static final String DEFAULT_PROPERTIES_FILE_NAME = "mapViewer.properties";

	/** The Constant PROPERTIES. */
	public static final MapViewerProperties PROPERTIES = new MapViewerProperties();

	/** normal application exit */
	private static final int DONE = 0;

	/** application aborted by user. */
	private static final int ABORT = 1;


	/**
	 * The main method starting the map viewer gui.
	 * 
	 * @param args
	 *            the args
	 */
	public static void main(final String[] args) {
		System.out.println("Start of OpenLR map viewer...");
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(DEFAULT_PROPERTIES_FILE_NAME);
		MapViewer mv = new MapViewer();
		int ret = mv.run(is);
		if (ret == ABORT) {
			System.out.println("End of OpenLR map viewer (aborted by user)!");
			System.exit(0);
		}
	}

	/**
	 * Run the application.
	 * 
	 * @param propertiesStream
	 *            the properties stream
	 * @return the exit code
	 */
	public int run(final InputStream propertiesStream) {
		System.out.println("Looking for OpenLR data loader...");
		MapsLoader mapsLoader = null;
		try {
			mapsLoader = new MapsLoader();
		} catch (MapViewerException e) {
			System.out.println("no map loader found");
			return ABORT;
		}
		System.out.println("Map loader found: " + mapsLoader.getNrOfMapLoader());

		try {
			PROPERTIES.load(propertiesStream);
			propertiesStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			System.err.println("Properties loading failed");
			return ABORT;
		}

		try {
		    MapsHolder mapHolder = mapsLoader.loadMaps();
			if (mapHolder == null) {
			    System.err.println("No maps loaded!");
			    return ABORT;
			}
			MapViewerGui mvg = new MapViewerGui(mapHolder);		
			mvg.setVisible(true);
			return DONE;
		} catch (InvalidMapDataException e1) {
			System.err.println("No maps loaded! " + e1.getMessage());
			return ABORT;
		} catch (MapViewerException e) {
			System.err.println("Error. " + e.getMessage());
			return ABORT;
		}

	}	

}
