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
package openlr.mapviewer.location;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import openlr.LocationType;
import openlr.decoder.OpenLRDecoderProcessingException;
import openlr.decoder.worker.coverage.ClosedLineCoverage;
import openlr.geomap.MapLayer;
import openlr.location.Location;
import openlr.location.LocationFactory;
import openlr.location.data.AffectedLines;
import openlr.location.data.Orientation;
import openlr.location.data.SideOfRoad;
import openlr.map.GeoCoordinates;
import openlr.map.InvalidMapDataException;
import openlr.map.Line;
import openlr.map.MapDatabase;
import openlr.mapviewer.maplayer.ClosedLineMapLayer;
import openlr.mapviewer.utils.LineNameResolver;

/**
 * The Class LocationTypeClosedLine.
 */
public class LocationTypeClosedLine extends LocationTypeArea {

	/** The location. */
	private List<Line> lines = new ArrayList<Line>();


	/**
	 *  Check if the closed line is valid.
	 * 
	 * @return true if the closed line is valid
	 */
	private boolean isClosedLineValid() {
		if (lines.isEmpty() || lines.size() < 2) {
			return false;
		}
		Line firstLine = lines.get(0);
		Line lastLine = lines.get(lines.size() - 1);
		if (lastLine.getEndNode().getID() == firstLine.getStartNode().getID()) {
			return true;
		}
		return false;
	}

	/**
	 *  Adds the line to the closed line location or removes it
	 * if it was the last line of the current selection. Observer of this class
	 * will be informed about the location change.
	 * 
	 * @param line
	 *            the line
	 * 
	 */
	private void addOrRemoveClosedLine(final Line line) {
		if (line == null) {
			lastAction = "";
			
		} else if (lines.isEmpty()) {
			lines.add(line);
			lastAction = "line added";
		} else if (LocationHelper.checkConnectionEnd(lines, line)) {
			// Add line to the end of the list
			lines.add(line);
			lastAction = "line added (end)";
		} else if (LocationHelper.isLastLine(lines, line)) {
			// that mean the user has selected the last line --> in this case it
			// will be removed
			lines.remove(lines.size() - 1);
			lastAction = "line removed (end)";
		} else if (LocationHelper.checkConnectionStart(lines, line)) {

			// add line to the first position of the list
			lines.add(0, line);
			lastAction = "line added (front)";
		} else if (LocationHelper.isFirstLine(lines, line)) {
			// The user has selected the first line --> in this case it will be
			// removed
			lines.remove(0);
			lastAction = "line removed (start)";
		} else {
			lastAction = "not connected";
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
    public final Location createLocation(final String identifier)
            throws LocationIncompleteException {

        if (isClosedLineValid()) {
            Location loc = LocationFactory.createClosedLineLocation(
                    identifier, lines);
            return loc;
        } else {
            throw new LocationIncompleteException(
                    "Location contains no closed path");
        }
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final List<String> getLocationInfo() {
		List<String> info = new ArrayList<String>();

		int count = 1;

		int locLength = 0;
		for (Line l : lines) {
			String s = Long.toString(l.getID());
			String s1 = "";
			Map<Locale, List<String>> names = l.getNames();
			if (names != null && !names.isEmpty()) {
				s1 = LineNameResolver.resolveLineName(names);
			}
			info.add(count + ") " + s + " (" + s1 + ")");
			count++;
			locLength += l.getLineLength();
		}
		if (!lines.isEmpty()) {
			info.add("Length of location: " + locLength + "m");
		}

		if (areaLocCoveredLines != null && !areaLocCoveredLines.isEmpty()) {
			info.add("+++++ covered Lines ++++++++++ ");
			count = 1;
			for (Line l : areaLocCoveredLines) {
				info.add(count + ") Line ID: " + l.getID());
				count++;
			}
		}

		if (areaLocIntersectedLines != null
				&& !areaLocIntersectedLines.isEmpty()) {
			info.add("+++++ Intersected Lines ++++++++++ ");
			count = 1;
			for (Line l : areaLocIntersectedLines) {
				info.add(count + ") Line ID: " + l.getID());
				count++;
			}
		}
		return info;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final MapLayer getLocationMapLayer() {
		return new ClosedLineMapLayer(lines);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean supportsRowsColumns() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setRowsColumns(final int rows, final int columns) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int getColumns() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int getRows() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean supportsUpperRightCoord() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setUpperRightCoord(final GeoCoordinates ur) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final GeoCoordinates getUpperRightCoord() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean supportsLowerLeftCoord() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setLowerLeftCoord(final GeoCoordinates ll) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final GeoCoordinates getLowerLeftCoord() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean supportsRadius() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setRadius(final long radius) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean supportsLine() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void addOrRemoveLine(final Line l) {
		addOrRemoveClosedLine(l);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean supportsOrientation() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setOrientation(final Orientation o) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean supportsSideOfRoad() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setSideOfRoad(final SideOfRoad s) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setLocation(final Location loc) {
		if (loc.getLocationType() == LocationType.CLOSED_LINE) {
			lines.clear();
			lines.addAll(loc.getLocationLines());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean supportsGeoCoordinatePoi() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setGeoCoordinatePoi(final double lon, final double lat)
			throws InvalidMapDataException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean supportsNegOffset() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setNegOffset(final int no) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setPosOffset(final int po) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean supportsPosOffset() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean offsetsSelectable() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setNegOffset(final double lon, final double lat)
			throws InvalidMapDataException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setPosOffset(final double lon, final double lat)
			throws InvalidMapDataException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Orientation getOrientation() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final SideOfRoad getSideOfRoad() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
    final void updateCoverage(final MapDatabase map)
            throws InvalidMapDataException, OpenLRDecoderProcessingException {
		areaLocCoveredLines.clear();
		areaLocIntersectedLines.clear();
        if (!lines.isEmpty() && isClosedLineValid()) {
            ClosedLineCoverage coverage = new ClosedLineCoverage(lines);
            AffectedLines affected = coverage.getAffectedLines(map);
            areaLocCoveredLines.addAll(affected.getCoveredLines());
            areaLocIntersectedLines.addAll(affected.getIntersectedLines());
        }
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int getPosOffset() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int getNegOffset() {
		throw new UnsupportedOperationException();
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    public LocationType getLocationType() {
        return LocationType.CLOSED_LINE;
    }	

}
