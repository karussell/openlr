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
package openlr.mapviewer.gui.panels.toolbar;

import openlr.geomap.tools.MeasurementReceiver;
import openlr.map.GeoCoordinates;
import openlr.mapviewer.gui.panels.StatusBar;
import openlr.mapviewer.utils.Formatter;

/**
 * This {@link MeasurementReceiver} prints the measurement values on the status
 * bar of the MapViewer application.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
final class MeasureStatusPrinter implements MeasurementReceiver {

    /**
     * A reference to the status bar
     */
    private final StatusBar stateBar;

    /**
     * Creates a new status printer
     * 
     * @param statusBar
     *            A reference to the status bar
     */
    public MeasureStatusPrinter(final StatusBar statusBar) {
        stateBar = statusBar;
    }

    /**
     * Prints the measure values to the status bar of the map viewer
     * application.
     * 
     * @param start
     *            The start coordinate
     * @param end
     *            The end The end coordinate
     * @param distance
     *            The distance between start and end
     * @param bearing
     *            The bearing between the line from start to end and North
     *            direction
     */
    @Override
    public void newMeasurement(final GeoCoordinates start,
            final GeoCoordinates end, final double distance,
            final double bearing) {

        StringBuilder sb = new StringBuilder();
        sb.append("Start: [")
                .append(Formatter.COORD_FORMATTER.format(start
                        .getLongitudeDeg()))
                .append("/")
                .append(Formatter.COORD_FORMATTER.format(start.getLatitudeDeg()))
                .append("]");

        sb.append(" - Current: [")
                .append(Formatter.COORD_FORMATTER.format(end.getLongitudeDeg()))
                .append("/")
                .append(Formatter.COORD_FORMATTER.format(end.getLatitudeDeg()))
                .append("]");
        sb.append(" - Distance: ").append(
                Formatter.DIST_FORMATTER.format(distance));
        sb.append(" - Bearing: ").append(
                Formatter.BEARING_FORMATTER.format(bearing));

        stateBar.setMessage(sb.toString());
    }
}
