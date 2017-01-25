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
package openlr.mapviewer.gui.tools;

import openlr.geomap.MapMouseAdaptor;
import openlr.geomap.MapMouseEvent;
import openlr.map.GeoCoordinates;
import openlr.map.Line;
import openlr.map.utils.GeometryUtils;
import openlr.mapviewer.MapsHolder;
import openlr.mapviewer.gui.panels.StatusBar;
import openlr.mapviewer.maplayer.FocusLineMapLayer;
import openlr.mapviewer.utils.FeatureInfo;
import openlr.mapviewer.utils.FindClosestLine;
import openlr.mapviewer.utils.Formatter;

/**
 * The MouseLineInfo is used to highlight the line beneath the mouse cursor and
 * to present information about the highlighted line.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class MouseLineInfo extends MapMouseAdaptor {

	/** The maps holder. */
	private final MapsHolder mapsHolder;

	/** The map index. */
	private final MapsHolder.MapIndex mapIndex;

	/** The status bar. */
	private final StatusBar statusBar;

	/** The is active. */
	private boolean isActive = true;
	
	/** The current focus. */
	private Line currentFocus;

	/**
	 * The Constructor.
	 * 
	 * @param mHolder
	 *            the m holder
	 * @param mapIdx
	 *            the map idx
	 * @param sBar
	 *            the s bar
	 */
	public MouseLineInfo(final MapsHolder mHolder,
			final MapsHolder.MapIndex mapIdx, final StatusBar sBar) {
		mapsHolder = mHolder;
		statusBar = sBar;
		mapIndex = mapIdx;
	}
	
	/**
	 * Gets the current focus.
	 *
	 * @return the current focus
	 */
	public final Line getCurrentFocus() {
		return currentFocus;
	}

	/**
	 * On mouse moved.
	 * 
	 * @param ev
	 *            the ev
	 */
	@Override
    public final void mouseMoved(final MapMouseEvent ev) {
		if (isActive) {
		    GeoCoordinates coord = ev.getCoordinate();
			StringBuilder sb = new StringBuilder();
			sb.append("mouse position: ");
			sb.append(Formatter.COORD_FORMATTER.format(coord.getLongitudeDeg()))
					.append("/");
			sb.append(Formatter.COORD_FORMATTER.format(coord.getLatitudeDeg()));
			sb.append("  [").append(ev.getPanelX()).append("/")
					.append(ev.getPanelY()).append("]");
			statusBar.setMessage(sb.toString());
			if (!GeometryUtils.checkCoordinateBounds(coord.getLongitudeDeg(),
					coord.getLatitudeDeg())) {
				mapsHolder.getMapLayerStore(mapIndex).removeHighlightLayer();
				return;
			}
			Line closestLine = FindClosestLine.determineClosestLine(
					mapsHolder.getMapDatabase(mapIndex), coord.getLongitudeDeg(),
					coord.getLatitudeDeg());
			if (closestLine != null) {
				FocusLineMapLayer focusLayer = new FocusLineMapLayer(
						closestLine);
				String infoText = FeatureInfo.createLineInfo(closestLine, true);
				mapsHolder.getMapPane(mapIndex).setToolTipText(infoText);
				mapsHolder.getMapLayerStore(mapIndex).setHighlightLayer(
						focusLayer);
				currentFocus = closestLine;
			} else {
				mapsHolder.getMapLayerStore(mapIndex).removeHighlightLayer();
				mapsHolder.getMapPane(mapIndex).setToolTipText("");
				currentFocus = null;
			}
		}
	}

	
	/**
	 * On mouse exited.
	 * 
	 * @param ev
	 *            the ev
	 */
    @Override
    public final void mouseExited(final MapMouseEvent ev) {
		statusBar.clear();
		mapsHolder.getMapLayerStore(mapIndex).removeHighlightLayer();
		currentFocus = null;
	}

}
