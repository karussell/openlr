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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import openlr.location.Location;
import openlr.map.GeoCoordinates;
import openlr.map.Line;

/**
 * The handler for POI-with-access-point locations.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
class PoiWithAccessPointHandler extends LinesBasedLocationHandler {

    /**
     * Creates a new POI-with-access-point handler.
     * 
     * @param location
     *            The location to draw.
     */
    public PoiWithAccessPointHandler(final Location location) {
        super(location);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final List<Line> getLocationLines() {
        return Arrays.asList(getLocation().getPoiLine());
    }

    /**
     * Adds the POI to the coordinates considered for calculating the bounding
     * box.
     * 
     * @param additions
     *            The list to add additional geo-coordinates to
     */
    @Override
    protected final void putAdditionalCoordinatesForBoundingBox(
            final Collection<GeoCoordinates> additions) {
        additions.add(getLocation().getPointLocation());
    }
}
