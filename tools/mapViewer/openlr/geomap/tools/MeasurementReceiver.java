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
package openlr.geomap.tools;

import openlr.map.GeoCoordinates;

/**
 * An interface defining an output processors that can be attached to the {@link Measure}
 * tool. This object then gets called with the measurement output every time
 * it changes.
 */
public interface MeasurementReceiver {

    /**
     * This method is called every time the user changes the selection of a
     * measurement.
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
    void newMeasurement(final GeoCoordinates start, GeoCoordinates end,
            double distance, double bearing);
}
