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
package openlr.otk.kml.locationreference;

import openlr.otk.kml.ContentProvider;
import openlr.rawLocRef.RawCircleLocRef;
import openlr.rawLocRef.RawClosedLineLocRef;
import openlr.rawLocRef.RawGeoCoordLocRef;
import openlr.rawLocRef.RawGridLocRef;
import openlr.rawLocRef.RawLineLocRef;
import openlr.rawLocRef.RawLocationReference;
import openlr.rawLocRef.RawPoiAccessLocRef;
import openlr.rawLocRef.RawPointAlongLocRef;
import openlr.rawLocRef.RawPolygonLocRef;
import openlr.rawLocRef.RawRectangleLocRef;
import openlr.utils.locref.LocationReferenceProcessor;

/**
 * A content provider factory that creates content providers for location
 * reference objects.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public final class LocationReferenceContentProviderFactory
        extends
        LocationReferenceProcessor<ContentProvider<? extends RawLocationReference>> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ContentProvider<? extends RawLocationReference> process(
            final RawLineLocRef locRefToWrite) {
        return new LRPBasedLocationReferenceFeatures<RawLineLocRef>(
                locRefToWrite);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContentProvider<? extends RawLocationReference> process(
            final RawGeoCoordLocRef locRefToWrite) {
        return new GeoCoordinateLocationReferenceFeatures(locRefToWrite);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContentProvider<? extends RawLocationReference> process(
            final RawPointAlongLocRef locRefToWrite) {
        return new PointAlongLocationReferenceFeatures(locRefToWrite);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContentProvider<? extends RawLocationReference> process(
            final RawPoiAccessLocRef locRefToWrite) {
        return new PoiWithAccessLocationReferenceFeatures(locRefToWrite);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContentProvider<? extends RawLocationReference> process(
            final RawRectangleLocRef locRefToWrite) {
        return new RectangleBasedLocationReferenceFeatures<RawRectangleLocRef>(
                locRefToWrite);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContentProvider<? extends RawLocationReference> process(
            final RawPolygonLocRef locRefToWrite) {
        return new PolygonLocationReferenceFeatures(locRefToWrite);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContentProvider<? extends RawLocationReference> process(
            final RawCircleLocRef locRefToWrite) {
        return new CircleLocationReferenceFeatures(locRefToWrite);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContentProvider<? extends RawLocationReference> process(
            final RawGridLocRef locRefToWrite) {
        return new GridLocationReferenceFeatures(locRefToWrite);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContentProvider<? extends RawLocationReference> process(
            final RawClosedLineLocRef locRefToWrite) {
        return new LRPBasedLocationReferenceFeatures<RawClosedLineLocRef>(
                locRefToWrite);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ContentProvider<? extends RawLocationReference> processUnknown(
            final RawLocationReference locationReference) {
        throw new IllegalArgumentException("Kml content provider factory called with location reference of type " 
            + locationReference.getLocationType());
    }
    

}
