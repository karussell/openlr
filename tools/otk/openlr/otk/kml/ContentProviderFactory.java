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
package openlr.otk.kml;

import openlr.location.Location;
import openlr.otk.kml.location.LocationContentProviderFactory;
import openlr.otk.kml.locationreference.LocationReferenceContentProviderFactory;
import openlr.rawLocRef.RawLocationReference;

/**
 * This class defines the interface for factories creating content providers of
 * well known types.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 * @param <T>
 *            The type of objects the implementing factory processes
 */
public abstract class ContentProviderFactory<T> {

    /**
     * Creates a specific content provider that processes the given type of
     * object
     * 
     * @param contentObject
     *            The object a content provider is searched for
     * @return The concrete content provider
     */
    public abstract ContentProvider<? extends T> createContentProvider(
            T contentObject);

    /**
     * Delivers the appropriate content provider for the given location
     * reference type
     * 
     * @param locationReference
     *            The location reference to create the KML representation from
     * @return The content provider that processes the given type of location
     *         references
     */
    public static ContentProvider<? extends RawLocationReference> createLocationReferenceContentProvider(
            final RawLocationReference locationReference) {

        LocationReferenceContentProviderFactory factory = new LocationReferenceContentProviderFactory();
        return factory.process(locationReference);
    }

    /**
     * Delivers the appropriate content provider for the given location type
     * 
     * @param location
     *            The location to create the KML representation from
     * @return The content provider that processes the given type of location
     */
    public static ContentProvider<? extends Location> createLocationContentProvider(
            final Location location) {

        LocationContentProviderFactory factory = new LocationContentProviderFactory();
        return factory.process(location);
    }
}
