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
package openlr.mapviewer.utils.bbox;

import openlr.location.Location;
import openlr.map.GeoCoordinates;
import openlr.map.InvalidMapDataException;
import openlr.map.utils.GeometryUtils;

/**
 * This class creates the JavaScript code to draw a grid location.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
class GridLocationHandler implements BoundingBoxCalculatorWorker {

    /**
     * The location to draw.
     */
    private final Location loc;


    /**
     * Creates a new instance.
     * 
     * @param location
     *            The location to draw
     */
    public GridLocationHandler(final Location location) {
        loc = location;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public final BoundingBox getBoundingBox() {
        GeoCoordinates lowerLeft = loc.getLowerLeftPoint();
        GeoCoordinates upperRight = loc.getUpperRightPoint();
        try {
            GeoCoordinates scaledUpperRight = GeometryUtils
                    .scaleUpperRightCoordinate(lowerLeft.getLongitudeDeg(),
                            lowerLeft.getLatitudeDeg(),
                            upperRight.getLongitudeDeg(),
                            upperRight.getLatitudeDeg(),
                            loc.getNumberOfColumns(), loc.getNumberOfRows());

            return new BoundingBox(lowerLeft, scaledUpperRight);
        } catch (InvalidMapDataException e) {
            throw new IllegalStateException(
                    "Could not calculate center upper right point of the grid",
                    e);
        }
    }
}
