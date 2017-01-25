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
 *  Copyright (C) 2009-12 TomTom International B.V.
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
package openlr.mapviewer.gmaps.handlers;

import java.util.Arrays;
import java.util.List;

import openlr.location.CircleLocation;
import openlr.location.ClosedLineLocation;
import openlr.location.GeoCoordLocation;
import openlr.location.GridLocation;
import openlr.location.LineLocation;
import openlr.location.Location;
import openlr.location.PoiAccessLocation;
import openlr.location.PointAlongLocation;
import openlr.location.PolygonLocation;
import openlr.location.RectangleLocation;
import openlr.map.Line;
import openlr.utils.location.LocationProcessor;

/**
 * A content provider factory that creates content providers for location
 * objects.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class LocationHandlerFactory extends
        LocationProcessor<LocationHandler> {

    /**
     * The color to draw lines with
     */
    private final String lineLineColor;

    /**
     * Creates the factory
     * 
     * @param selectedLocColor
     *            The color to draw lines with
     */
    public LocationHandlerFactory(final String selectedLocColor) {
        this.lineLineColor = selectedLocColor;
    }

    @Override
    public final LocationHandler process(
            final LineLocation location) {
        return new LinesBasedLocationHandler(location, lineLineColor) {

            @Override
            protected List<Line> getLocationLines() {
                return location.getLocationLines();
            }
        };
    }

    @Override
    public final LocationHandler process(
            final GeoCoordLocation location) {

        return new GeoCoordinateHandler(location);
    }

    @Override
    public final LocationHandler process(
            final PointAlongLocation location) {

        return new LinesBasedLocationHandler(location, lineLineColor) {

            @Override
            protected List<Line> getLocationLines() {
                return Arrays.asList(location.getPoiLine());
            }         
        };
    }

    @Override
    public final LocationHandler process(
            final PoiAccessLocation location) {

        return new PoiWithAccessPointHandler(location, lineLineColor);
    }

    @Override
    public final LocationHandler process(
            final RectangleLocation location) {

        return new CornerPointLocationHandler(location, lineLineColor);
    }

    @Override
    public final LocationHandler process(
            final PolygonLocation location) {

        return new CornerPointLocationHandler(location, lineLineColor);
    }

    @Override
    public final LocationHandler process(
            final CircleLocation location) {

        return new CircleLocationHandler(location, lineLineColor);
    }

    @Override
    public final LocationHandler process(
            final GridLocation location) {

        return new GridLocationHandler(location, lineLineColor);
    }

    @Override
    public final LocationHandler process(
            final ClosedLineLocation location) {

        return new LinesBasedLocationHandler(location, lineLineColor) {

            @Override
            protected List<Line> getLocationLines() {
                return location.getLocationLines();
            }
        };
    }
    
    @Override
    public final LocationHandler processUnknown(
            final Location locationReference) {
        throw new IllegalArgumentException(
                "Location handler factory called with location reference of type "
                        + locationReference.getLocationType());
    } 

}
