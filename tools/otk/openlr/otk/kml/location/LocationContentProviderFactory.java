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
 **/

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
package openlr.otk.kml.location;

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
import openlr.otk.kml.ContentProvider;
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
public class LocationContentProviderFactory extends
        LocationProcessor<ContentProvider<? extends Location>> {

    @Override
    public final ContentProvider<? extends Location> process(
            final LineLocation location) {
        return new LineLocationFeatures(location);
    }

    @Override
    public final ContentProvider<? extends Location> process(
            final GeoCoordLocation location) {

        return new GeoCoordinatesFeatures(location);
    }

    @Override
    public final ContentProvider<? extends Location> process(
            final PointAlongLocation location) {

        return new PointAlongLineLocationFeatures(location);
    }

    @Override
    public final ContentProvider<? extends Location> process(
            final PoiAccessLocation location) {

        return new PoiWithAccessLocationFeatures(location);
    }

    @Override
    public final ContentProvider<? extends Location> process(
            final RectangleLocation location) {

        return new RectangleLocationFeatures(location);
    }

    @Override
    public final ContentProvider<? extends Location> process(
            final PolygonLocation location) {

        return new PolygonLocationFeatures(location);
    }

    @Override
    public final ContentProvider<? extends Location> process(
            final CircleLocation location) {

        return new CircleLocationFeatures(location);
    }

    @Override
    public final ContentProvider<? extends Location> process(
            final GridLocation location) {

        return new GridLocationFeatures(location);
    }

    @Override
    public final ContentProvider<? extends Location> process(
            final ClosedLineLocation location) {

        return new ClosedLineLocationFeatures(location);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public final ContentProvider<? extends Location> processUnknown(
            final Location locationReference) {
        throw new IllegalArgumentException(
                "Kml content provider factory called with location reference of type "
                        + locationReference.getLocationType());
    }

}
