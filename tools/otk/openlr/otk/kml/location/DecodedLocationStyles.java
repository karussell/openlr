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

import openlr.otk.kml.StylesCollection;
import de.micromata.opengis.kml.v_2_2_0.Style;

/**
 * A setup of prepared styles for formatting decode locations.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class DecodedLocationStyles extends StylesCollection {
    
    /**
     * default serial id
     */
    private static final long serialVersionUID = 1L;    

    /**
     * Color used for decoded objects in KML.
     */
    private static final String KML_DECODER_COLOR = "dd00ffff";

    /** The Constant KML_DECODER_SCALE. */
    private static final double KML_DECODER_SCALE = 0.5d;

    /** The Constant KML_DECODER_WIDTH. */
    private static final double KML_DECODER_WIDTH = 10.0d;

    /**
     * Creates a new instance
     */
    public DecodedLocationStyles() {
        final Style decoderLineStyle = new Style().withId(LocationStyleIdentifier.LINE.getID());
        decoderLineStyle.createAndSetLineStyle().withColor(KML_DECODER_COLOR)
                .withWidth(KML_DECODER_WIDTH);

        add(decoderLineStyle);
        final Style decoderNodeStyle = new Style().withId(LocationStyleIdentifier.POINT.getID());
        decoderNodeStyle
                .createAndSetIconStyle()
                .withColor(KML_DECODER_COLOR)
                .withScale(KML_DECODER_SCALE)
                .createAndSetIcon()
                .withHref(
                        "http://maps.google.com/mapfiles/kml/shapes/road_shield3.png");

        add(decoderNodeStyle);

        final Style decoderStartOffsetStyle = new Style()
                .withId(LocationStyleIdentifier.POSITIVE_OFFSET.getID());
        decoderStartOffsetStyle
                .createAndSetIconStyle()
                .withColor(KML_DECODER_COLOR)
                .withScale(1.0d)
                .createAndSetIcon()
                .withHref(
                        "http://maps.google.com/mapfiles/kml/shapes/placemark_circle.png");

        add(decoderStartOffsetStyle);

        final Style decoderEndOffsetStyle = new Style()
                .withId(LocationStyleIdentifier.NEGATIVE_OFFSET.getID());
        decoderEndOffsetStyle
                .createAndSetIconStyle()
                .withColor(KML_DECODER_COLOR)
                .withScale(1.0d)
                .createAndSetIcon()
                .withHref(
                        "http://maps.google.com/mapfiles/kml/shapes/placemark_square.png");

        add(decoderEndOffsetStyle);
    }
}
