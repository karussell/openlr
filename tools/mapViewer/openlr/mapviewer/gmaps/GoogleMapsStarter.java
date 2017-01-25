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
package openlr.mapviewer.gmaps;

import static openlr.mapviewer.gmaps.scriptlets.ScriptConstants.NEW_LINE;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JOptionPane;

import openlr.location.Location;
import openlr.map.GeoCoordinates;
import openlr.mapviewer.MapViewer;
import openlr.mapviewer.MapViewerException;
import openlr.mapviewer.gmaps.handlers.LocationHandler;
import openlr.mapviewer.gmaps.handlers.LocationHandlerFactory;
import openlr.mapviewer.properties.MapDrawingProperties;
import openlr.mapviewer.properties.OtherProperties;
import openlr.mapviewer.utils.bbox.BoundingBox;
import openlr.mapviewer.utils.bbox.BoundingBoxCalculator;

import org.apache.log4j.Logger;

/** 
 * This class shows a {@link Location} in Google Maps.<br>
 * It creates an HTML file containing JavaScript code that connects to Google 
 * Maps via JavaScript API. <br>
 * After creation of the file is opened it using the system users web browser. 
 * <br>
 * See Google JavaScipt API:<br>
 * <a href="http://code.google.com/intl/en/apis/maps/documentation/javascript/overlays.html">http://code.google.com/intl/en/apis/maps/documentation/javascript/overlays.html</a>
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class GoogleMapsStarter {
	
	/**
	 * A regular expression that matches the script position inside the output template.
	 */
	private static final String REGEX_OUTPUT_SCRIPT = "\\{script\\}";

	/**
	 * A regular expression that matches the title position inside the output template.
	 */
	private static final String REGEX_OUTPUT_TITLE = "\\{title\\}";
	
	/**
	 * The request message for the browser command.
	 */
	private static final String REQUEST_BROWSER_EXECUTABLE = 
		"<html>Please enter the command or path to the executable starting your "
		+ "web browser, e.g. \"explorer\" on windows!<br> You will be able to "
		+ "change this setting via menu \"File/Options/Others\" later.</html>";

	/** The Constant logger. */
	private static final Logger LOG = Logger.getLogger(GoogleMapsStarter.class);	
	
	/**
	 * The identifier of the instance of google.maps.Map inside the script.
	 */
	public static final String IDENTIFIER_MAP_OBJECT = "map";

	/**
	 * Opens the web browser displaying the given location in Google Maps.
	 * 
	 * @param location
	 *            The location to display.
	 * @throws MapViewerException
	 *             If an error occurs creating them temporary HTML file or
	 *             opening the web browser.
	 */
    public final void show(final Location location) throws MapViewerException {
    	
    	try {
			final String template = readTemplate();
			
			String outputData = template.replaceFirst(REGEX_OUTPUT_TITLE, 
					"MapViewer - Location ID \\\"" + location.getID() + "\\\"");
			
			LocationHandler locHandler = determineLocationHandler(location);
			
			StringBuilder scriptCode = new StringBuilder();
            scriptCode.append(createInitialization(BoundingBoxCalculator
                    .calculateBoundingBox(location)));
			scriptCode.append(locHandler.getDrawingCommands());
	
			outputData = outputData.replaceFirst(REGEX_OUTPUT_SCRIPT, scriptCode.toString());
			
			startBrowser(writeOutputFile(outputData));
			
		} catch (IOException ioe) {
			throw new MapViewerException(ioe);
		} catch (URISyntaxException e) {
			throw new MapViewerException(e);
		}
    }

	/**
	 * Starts the web browser with the given HTML file as argument.<br>
	 * If no special browser command is set in the MapViewer properties it is 
	 * tried to open the users default browser via {@link Desktop} API. If
	 * this is not supported or a browser command is set, the browser is opened
	 * via system command.
	 * 
	 * @param html
	 *            The file to show in the browser.
	 * @throws URISyntaxException If the absolute path to the given file is not
	 * translatable to an URI.
	 * @throws IOException If an error occurred running the browser via system 
	 * process.
	 */
	private void startBrowser(final File html) throws URISyntaxException, IOException {

		String browserCommand = MapViewer.PROPERTIES.getProperty(
				OtherProperties.BROWSER_COMMAND.key());

		if (browserCommand == null) {
			// the preferred way: the Desktop API
			boolean desktopAvailable = false;
			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				if (desktop.isSupported(Desktop.Action.BROWSE)) {
					String path = html.getAbsolutePath();
					if (path.indexOf('\\') > 0) {
						// if we are on Windows we have to tilt the slashes for a valid URL
						path = path.replaceAll("\\\\", "/");
					}
					if (LOG.isDebugEnabled()) {
					LOG.debug("Opening " + path + " in desktop default browser.");
					}
					// we use this URI constructor with good cause- it encodes 
					// the path string which can have strange characters in it
					desktop.browse(new URI("file", "///" + path, null));
					desktopAvailable = true;
				}
			}

			if (!desktopAvailable) {
				startBrowserAsSystemCommand(null, html);
			}
		} else {
			// if there is a user specific browser command defined- use it
			startBrowserAsSystemCommand(browserCommand, html);
		}
	}

	    /**
     * Runs the web browser as a system process.
     * 
     * @param browserCommand
     *            The browser command, optional if it is <code>null
     * </code> the user will get an input request for it (which is then stored
     *            in {@link openlr.mapviewer.properties.MapViewerProperties
     *            MapViewerProperties} permanently.
     * @param html
     *            The HTML file to open in the browser.
     * @throws IOException
     *             In case of an error running the browser command (maybe
     *             entered during this method) with the given file as argument.
     */
	private void startBrowserAsSystemCommand(final String browserCommand,
			final File html) throws IOException {

		String command = browserCommand;
		String browserCommandEntered = null;
		if (browserCommand == null) {
			browserCommandEntered = JOptionPane.showInputDialog(
					REQUEST_BROWSER_EXECUTABLE);
			if (browserCommandEntered != null) {
				command = browserCommandEntered;
			}
		}

		if (command != null) {
			boolean seamsSuccessful = true;
			try {
				// The constructor with the command array performs the quoting of the path for us!
				Runtime.getRuntime().exec(new String[]{command, html.getAbsolutePath()});
			} catch (IOException e) {
				seamsSuccessful = false;
				LOG.error("Unable to lauch browser via command " + command 
						+ " " + html.getAbsolutePath(), e);
				throw e;
			} finally {
				// store the newly set browser command
				if (browserCommandEntered != null && seamsSuccessful) {
					MapViewer.PROPERTIES.setProperty(OtherProperties.BROWSER_COMMAND.key(),
							browserCommandEntered);
				}
			}
		}
	}

	/**
	 * Writes the output file containing the HTML and JavaScript code.
	 * 
	 * @param html
	 *            The content to write.
	 * @return The file object if the file could successfully be created.
	 * @throws IOException
	 *             If an error occurred creating the file.
	 */
    private File writeOutputFile(final String html) throws IOException {
        File out = File.createTempFile("mapviewerGMap", ".html");
        FileWriter writer = new FileWriter(out);
        try {
            writer.write(html);
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                LOG.warn("Error closing stream to written Google maps output "
                        + "file.", e);
            }
        }
        return out;
    }
    
    /**
     * Creates the initial commands of the JavaScript.
     * 
     * @param bbox
     *            The bounding box enclosing the location.
     * @return The string
     */
    private String createInitialization(final BoundingBox bbox) {

        GeoCoordinates lowerLeft = bbox.getLowerLeft();
        GeoCoordinates upperRight = bbox.getUpperRight();

        return "var mapOptions = {" + NEW_LINE
                + "  mapTypeId: google.maps.MapTypeId.ROADMAP" + NEW_LINE
                + "};" + NEW_LINE + "var " + IDENTIFIER_MAP_OBJECT
                + " = new google.maps.Map("
                + "document.getElementById(\"map_canvas\"), mapOptions);"
                + NEW_LINE + NEW_LINE + "var sw = new google.maps.LatLng("
                + lowerLeft.getLatitudeDeg() + ","
                + lowerLeft.getLongitudeDeg() + "); " + NEW_LINE
                + "var ne = new google.maps.LatLng("
                + upperRight.getLatitudeDeg() + ","
                + upperRight.getLongitudeDeg() + "); " + NEW_LINE
                + " var bounds = new google.maps.LatLngBounds(sw, ne); "
                + NEW_LINE + "map.fitBounds(bounds); " + NEW_LINE;
    }

	/**
	 * Reads the HMTL template for the output. This template contains two place
	 * holders: <br>
	 * <ul>
	 * <li>The title of the page, identifiable via {@link #REGEX_OUTPUT_TITLE}</li>
	 * <li>The position of the script content, identifiable via
	 * {@link #REGEX_OUTPUT_SCRIPT}</li>
	 * </ul>
	 * <br>
	 * 
	 * @return The string containing the template for the output file.
	 * @throws IOException
	 *             In case of an error reading the template.
	 */
	private String readTemplate() throws IOException {

		URL url = Thread.currentThread().getContextClassLoader().getResource(
				"openlr/mapViewer/gmaps/googlemaps_template.html");

		StringBuilder content = new StringBuilder();

		URLConnection yc = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(yc
				.getInputStream()));

		String inputLine;

        try {
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine).append(NEW_LINE);
            }
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                LOG.warn("Error closing reader for Google map template file.", e);
            }
        }

		return content.toString();
	}

    /**
     * Delivers the appropriate {@link LocationHandler} for the given location.
     * 
     * @param location
     *            The location to display.
     * @return The location handler that generates JavaScript code for the given
     *         type of location.
     */
    private LocationHandler determineLocationHandler(final Location location) {

        String selectedLocColor = MapViewer.PROPERTIES
                .getProperty(MapDrawingProperties.LOCATION_SELECTED.key());

        return new LocationHandlerFactory(selectedLocColor)
                .process(location);
    }
}
