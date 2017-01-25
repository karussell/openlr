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
package openlr.mapviewer.gui.panels;

import java.util.ArrayList;
import java.util.List;

import openlr.location.Location;
import openlr.map.GeoCoordinates;
import openlr.map.GeoCoordinatesImpl;
import openlr.map.Line;
import openlr.mapviewer.utils.bbox.BoundingBox;
import openlr.mapviewer.utils.bbox.BoundingBoxCalculator;

/**
 * This class implements calculations of standard zoom levels on the map
 * graphics. It calculates bounding boxes for various kinds of input objects.
 * These bounding boxes can be used to update the viewport on the map pane.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public final class StandardZoomCalculator {

    /**
     * Specifies margin around a initially zoomed location. It defines the
     * percentage to add to the biggest dimension (width or height) of the
     * location.
     */
    private static final double INITIAL_ZOOM_BORDER = .2;

    /**
     * Defines the minimum zoom level when initially zooming to a point.The
     * value defines the span between GEO-coordinates that shall be visible.
     */
    private static final double INITIAL_ZOOM_TO_POINT = .005;

    /**
     * Defines the minimum zoom level when initially zooming to a location.The
     * value defines the span between GEO-coordinates that shall be visible.
     */
    private static final double MIN_ZOOM_TO_LOCATION = .02;

    /**
     * Disabled constructor
     */
    private StandardZoomCalculator() {
    }

    /**
     * Zoom in a standard ratio to a point and centers the map view around it.
     * The standard zoom takes a margin and a minimum spatial viewport into
     * account.
     * 
     * @param coordinate
     *            the coordinate to zoom to.
     * @return the calculated bounding box
     */
    public static BoundingBox standardZoomTo(final GeoCoordinates coordinate) {

        double urX = coordinate.getLongitudeDeg() + INITIAL_ZOOM_TO_POINT / 2;
        double urY = coordinate.getLatitudeDeg() + INITIAL_ZOOM_TO_POINT / 2;

        double llX = coordinate.getLongitudeDeg() - INITIAL_ZOOM_TO_POINT / 2;
        double llY = coordinate.getLatitudeDeg() - INITIAL_ZOOM_TO_POINT / 2;

        GeoCoordinates ur = GeoCoordinatesImpl.newGeoCoordinatesUnchecked(urX,
                urY);
        GeoCoordinates ll = GeoCoordinatesImpl.newGeoCoordinatesUnchecked(llX,
                llY);

        return new BoundingBox(ll, ur);
    }

    /**
     * Zooms the map graphics to a standard level that displays all the given
     * lines and centers the map view around it. The standard zoom takes a
     * margin and a minimum spatial viewport into account.
     * 
     * @param lines
     *            The lines that shall be zoomed to.
     * @return the calculated bounding box
     */
    public static BoundingBox standardZoomTo(final Line... lines) {

        if (lines != null) {

            List<GeoCoordinates> allTheShapes = new ArrayList<GeoCoordinates>();

            for (Line line : lines) {
                allTheShapes.addAll(line.getShapeCoordinates());
            }
            BoundingBox bb = BoundingBoxCalculator
                    .calculateBoundingBoxAroundCoordinates(allTheShapes);

            return calcStandardZoomToBox(bb, MIN_ZOOM_TO_LOCATION);
        } else {
            throw new IllegalArgumentException(
                    "No line(s) provided for standard zoom calculation.");
        }

    }

    /**
     * Zooms the map graphics to a standard level that displays all the given
     * locations and centers the map view around it. The standard zoom takes a
     * margin and a minimum spatial viewport into account.
     * 
     * @param locations
     *            The locations that shall be zoomed to.
     * @return the calculated bounding box
     */
    public static BoundingBox standardZoomTo(final Location... locations) {

        if (locations != null && locations.length > 0) {

            BoundingBox grownBox = BoundingBoxCalculator
                    .calculateBoundingBox(locations[0]);

            for (int i = 1; i < locations.length; i++) {

                grownBox = BoundingBoxCalculator
                        .calculateBoundingBox(locations[i]);
            }

            return calcStandardZoomToBox(grownBox, MIN_ZOOM_TO_LOCATION);
        } else {
            throw new IllegalArgumentException(
                    "No location(s) provided for standard zoom calculation.");
        }
    }

    /**
     * Zooms the map graphics to a standard level that displays the specified
     * bounding box and centers the map view around it. The standard zoom takes
     * a margin and a minimum spatial viewport into account.
     * 
     * @param boundingBox
     *            The bounding box that shall be zoomed to.
     * @return the calculated bounding box
     */
    public static BoundingBox standardZoomTo(final BoundingBox boundingBox) {

        return calcStandardZoomToBox(boundingBox, MIN_ZOOM_TO_LOCATION);
    }

    /**
     * Calculates an {@link Envelope2D} defining the required zoom area taking
     * the specified minimum visible dimension into account. The result box
     * includes an additional percentage margin around the target box specified
     * in {@link #INITIAL_ZOOM_BORDER}.
     * 
     * @param bbox
     *            The bounding box to zoom to.
     * @param minSpan
     *            The minimum visible area. The value defines the span of
     *            GEO-coordinates that shall be visible around the target.
     * @return Returns the calculated bounding box around the target including
     *         standard border and minimum span.
     */
    private static BoundingBox calcStandardZoomToBox(final BoundingBox bbox,
            final double minSpan) {

        double widthToAdd;
        double heightToAdd;

        GeoCoordinates lowerLeft = bbox.getLowerLeft();
        GeoCoordinates upperRight = bbox.getUpperRight();

        double width = upperRight.getLongitudeDeg()
                - lowerLeft.getLongitudeDeg();
        double height = upperRight.getLatitudeDeg()
                - lowerLeft.getLatitudeDeg();

        double minSpanLessBorder = minSpan - minSpan * INITIAL_ZOOM_BORDER;

        if (width < minSpanLessBorder && height < minSpanLessBorder) {
            // Bounding box to display is fully inside the minimum zoom area
            // (and border) -> expand to standard zoom box
            widthToAdd = minSpan - width;
            heightToAdd = minSpan - height;
        } else {
            // add the border
            widthToAdd = width * INITIAL_ZOOM_BORDER;
            heightToAdd = height * INITIAL_ZOOM_BORDER;
        }

        GeoCoordinates newUpperCorner = GeoCoordinatesImpl
                .newGeoCoordinatesUnchecked(upperRight.getLongitudeDeg()
                        + widthToAdd / 2, upperRight.getLatitudeDeg()
                        + heightToAdd / 2);
        GeoCoordinates newLowerCorner = GeoCoordinatesImpl
                .newGeoCoordinatesUnchecked(lowerLeft.getLongitudeDeg()
                        - widthToAdd / 2, lowerLeft.getLatitudeDeg()
                        - heightToAdd / 2);

        return new BoundingBox(newLowerCorner, newUpperCorner);
    }
}
