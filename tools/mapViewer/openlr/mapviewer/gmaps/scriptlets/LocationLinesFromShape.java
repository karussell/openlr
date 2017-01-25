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
package openlr.mapviewer.gmaps.scriptlets;

import static openlr.mapviewer.gmaps.scriptlets.ScriptConstants.NEW_LINE;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import openlr.map.GeoCoordinates;
import openlr.map.GeoCoordinatesImpl;
import openlr.map.InvalidMapDataException;
import openlr.map.Line;
import openlr.map.utils.GeometryUtils;

/** 
 * This class creates the JavaScript code for drawing a google.maps.Polyline to 
 * paint location lines using the {@link Line#getShapeCoordinates()} method to retrieve
 * the segments to paint.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class LocationLinesFromShape {

	/**
	 * The URL of the marker image used for the negative offset.
	 */
	private static final String IMAGE_NEG_OFFSET_MARKER_URL = "http://maps.google.com/mapfiles/kml/paddle/red-diamond.png";

	/**
	 *  The URL of the marker image used for the positive offset.
	 */
	private static final String IMAGE_POS_OFFSET_MARKER_URL = "http://maps.google.com/mapfiles/kml/paddle/grn-stars.png";

	/**
	 * The size of the marker images for the offsets.
	 */
	private static final int[] IMAGE_OFFSET_MARKER_SIZE = new int[] {28, 28 };

	/**
	 * The script code.
	 */
	private final String script;

	/**
	 * Delivers the JavaScript code of a <code>google.maps.Polyline</code> that
	 * represents the given collection of lines. <br>
	 * Throws UnsupportedOperationException If the {@link Line} object of the
	 * collection do not support {@link Line#getShapeCoordinates()}!
	 * 
	 * @param varID
	 *            The identifier of the JavaScript variable the Polyline object
	 *            is assigned to.
	 * @param locationLines
	 *            The list of lines to create the entries from.
	 * @param lineColorHtml
	 *            The HTML color code for the lines.
	 * @throws InvalidMapDataException 
	 */
	public LocationLinesFromShape(final String varID,
			final List<? extends Line> locationLines, final String lineColorHtml) throws InvalidMapDataException {

		script = createPolylineFromShape(varID, locationLines, lineColorHtml,
				0, 0);
	}

	/**
	 * Delivers the JavaScript code of a <code>google.maps.Polyline</code> that 
	 * represents the given collection of lines. The positive and the negative 
	 * offsets are marked by <code>google.maps.Marker</code>s <br>
	 * Throws UnsupportedOperationException If the {@link Line} object of the
	 * collection do not support {@link Line#getShapeCoordinates()}!
	 * 
	 * @param varID
	 *            The identifier of the JavaScript variable the Polyline object
	 *            is assigned to.
	 * @param locationLines
	 *            The list of lines to create the entries from.
	 * @param lineColorHtml
	 *            The HTML color code for the lines.
	 * @param pOff
	 *            The positive offset value
	 * @param nOff
	 *            The negative offset value
	 */
	public LocationLinesFromShape(final String varID,
			final List<? extends Line> locationLines,
			final String lineColorHtml, final int pOff, final int nOff) {

		assert (pOff >= 0);
		assert (nOff >= 0);
		
		script = createPolylineFromShape(varID, locationLines, lineColorHtml,
				pOff, nOff);
	}

	/**
	 * Delivers the JavaScript code of a google.maps.Polyline that represents
	 * the given collection of lines. <br>
	 * This method creates it from the shape of the lines delivered by
	 * {@link Line#getShapeCoordinates()}. Throws UnsupportedOperationException if
	 * {@link Line#getShapeCoordinates()} is not supported.
	 * 
	 * @param varibleName
	 *            The identifier of the JavaScript variable the Polyline object
	 *            is assigned to.
	 * @param lines
	 *            The list of lines to create the entries from.
	 * @param lineColorHtml
	 *            The HTML color code for the lines.
	 * @param pOff
	 *            The positive offset value
	 * @param nOff
	 *            The negative offset value
	 * @return The string containing the sequence of LatLn elements.
	 */
	private String createPolylineFromShape(final String varibleName,
			final List<? extends Line> lines, final String lineColorHtml, 
			final int pOff, final int nOff) {
		
		StringBuilder result = new StringBuilder();
		String arrayID = varibleName;

        GeoCoordinates[] offsets = addPolyLine(result, lines, arrayID, pOff,
                nOff, lineColorHtml);

		addOffsetMarkers(varibleName, result, offsets[0], offsets[1]);

		return result.toString();
	}

	    /**
     * Creates the entries string of the array of google.maps.LatLong objects
     * that is used to create the google.maps.Polyline for the line shape.
     * Calculates meanwhile the coordinates of the offset points if the given
     * offset values are > 0.
     * 
     * @param scriptCode
     *            The script code up to this point.
     * @param lines
     *            The lines to draw.
     * @param polylineVariableName
     *            The identifier of the JavaScript variable assigned to the Polyline
     * @param pOff
     *            The positive offset value.
     * @param nOff
     *            The negative offset value.
     * @return An array containing the offset coordinates {0: positive, 1:
     *         negative}.
     */
    private GeoCoordinates[] addPolyLine(final StringBuilder scriptCode,
            final List<? extends Line> lines,
            final String polylineVariableName, final int pOff, final int nOff,
            final String lineColor) {

        GeoCoordinates posOff = null;
        GeoCoordinates negOff = null;

        int negOffInverted = -1;
        boolean searchPosOff = pOff > 0;
        boolean searchNegOff = false;
        
        List<GeoCoordinates> allShapeCoordinates = new ArrayList<GeoCoordinates>();

        for (Iterator<? extends Line> iterator = lines.iterator(); iterator
                .hasNext();) {

            Line line = iterator.next();
            int cumulatedLength = 0;

            List<GeoCoordinates> shape = line.getShapeCoordinates();
            GeoCoordinates last = null;
            if (shape == null) {
                throw new UnsupportedOperationException(
                        "The location lines could "
                                + "not be calculated using the shape of the line. The "
                                + "line doesn't support getShapeCoordinates()");
            }

            allShapeCoordinates.addAll(shape);

            if (!iterator.hasNext() && (nOff > 0)) {
                // last line, check for negative offset!
                searchNegOff = true;
                negOffInverted = line.getLineLength() - nOff;
            }

            for (GeoCoordinates current : shape) {

                if ((searchPosOff || searchNegOff) && (last != null)) {

                    int distanceToLast = (int) GeometryUtils
                            .distance(last.getLongitudeDeg(),
                                    last.getLatitudeDeg(),
                                    current.getLongitudeDeg(),
                                    current.getLatitudeDeg());
                    cumulatedLength += distanceToLast;

                    if (searchPosOff
                            && isSegmentContainingOffset(pOff, cumulatedLength,
                                    distanceToLast)) {

                        posOff = calculateOffset(cumulatedLength - pOff, last,
                                current, distanceToLast);

                        searchPosOff = false;
                    }

                    if (searchNegOff
                            && isSegmentContainingOffset(negOffInverted,
                                    cumulatedLength, distanceToLast)) {

                        negOff = calculateOffset(cumulatedLength
                                - negOffInverted, last, current, distanceToLast);

                        searchNegOff = false;
                    }
                }

                last = current;
            }
        }

        scriptCode.append(new Polyline(allShapeCoordinates,
                polylineVariableName, lineColor).toString());

        return new GeoCoordinates[] {posOff, negOff};
    }

	/**
	 * Creates the code of a google.maps.Marker for each of the offsets that is
	 * not <code>null</code>.
	 * 
	 * @param varPrefix
	 *            A prefix for the marker variable.
	 * @param scriptCode
	 *            The script content up to now.
	 * @param posOff
	 *            The coordinates of the positive offset.
	 * @param negOff
	 *            The coordinates of the negative offset.
	 */
	private void addOffsetMarkers(final String varPrefix,
			final StringBuilder scriptCode, final GeoCoordinates posOff,
			final GeoCoordinates negOff) {

		if (posOff != null) {
			String iconVar = "posOffImg";
			scriptCode.append(
					new MarkerImage(iconVar, IMAGE_POS_OFFSET_MARKER_URL, null,
							IMAGE_OFFSET_MARKER_SIZE)).append(NEW_LINE);
			scriptCode.append(new Marker("positve Offset",
					varPrefix + "PosOff", posOff, iconVar));
		}
		if (negOff != null) {
			String iconVar = "negOffImg";
			scriptCode.append(
					new MarkerImage(iconVar, IMAGE_NEG_OFFSET_MARKER_URL, null,
							IMAGE_OFFSET_MARKER_SIZE)).append(NEW_LINE);
			scriptCode.append(new Marker("negative Offset", varPrefix
					+ "NegOff", negOff, iconVar));
		}
	}

	/**
	 * Little helper method that checks if the given
	 * <code>offsetFromLineStart</code> is contained in the current segment
	 * bounded by <segment start> and <accumulated length>.
	 * 
	 * @param offsetFromLineStart
	 *            The offset to be checked defined to be measured from the start
	 *            of the current line (i.e. negative offset has to be inverted).
	 * @param reachedLineDistance
	 *            The current accumulated length reached while processing the
	 *            segments of the line.
	 * @param segmentLength
	 *            The length of the current segment.
	 * @return <code>true</code> if the current segment covers the offset point.
	 */
	private boolean isSegmentContainingOffset(final int offsetFromLineStart,
			final int reachedLineDistance, final int segmentLength) {

		return (reachedLineDistance >= offsetFromLineStart)
				&& (reachedLineDistance - segmentLength <= offsetFromLineStart);
	}

	/**
	 * Calculates the GEO-coordinates of the offset point as a point on the
	 * current segment line.
	 * 
	 * @param offsetFromLineStart
	 *            The offset to be checked defined to be measured from the start
	 *            of the current line (i.e. negative offset has to be inverted).
	 * @param startCoordinate
	 *            The start coordinate of the current segment.
	 * @param endCoordinate
	 *            The start coordinate of the current segment.
	 * @param segmentLength
	 *            The length of the current path segment.
	 * @return The GEO-coordinates of the offset point.
	 */
    private GeoCoordinates calculateOffset(final int offsetFromLineStart,
            final GeoCoordinates startCoordinate,
            final GeoCoordinates endCoordinate, final int segmentLength) {

        // the overrun percentage
        double part = ((double) offsetFromLineStart) / segmentLength;

        // REDUCE the overrun from the end of the current segment
        double longOff = endCoordinate.getLongitudeDeg()
                - part
                * (endCoordinate.getLongitudeDeg() - startCoordinate
                        .getLongitudeDeg());
        double latOff = endCoordinate.getLatitudeDeg()
                - part
                * (endCoordinate.getLatitudeDeg() - startCoordinate
                        .getLatitudeDeg());

        return GeoCoordinatesImpl.newGeoCoordinatesUnchecked(longOff, latOff);
    }
	
	/**
	 * @return The JavaScript code drawing the location lines.
	 */
	@Override
	public final String toString() {
		return script;
	}
	
}
