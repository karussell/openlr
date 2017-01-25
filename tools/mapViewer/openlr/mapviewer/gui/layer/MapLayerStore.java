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
package openlr.mapviewer.gui.layer;

import openlr.geomap.MapLayer;
import openlr.geomap.MapLayerHandler;
import openlr.map.FormOfWay;
import openlr.map.FunctionalRoadClass;
import openlr.mapviewer.MapViewerException;
import openlr.mapviewer.utils.observer.ObserverManager;
import cern.colt.map.AbstractIntObjectMap;
import cern.colt.map.OpenIntObjectHashMap;

/**
 * The MapLayerStore handles all map layers which can be drawn in the map pane.
 * Layers may be shown or removed and colors may be changed. The creation of
 * these layers are reported to the map load progress info dialog.
 * 
 * The following layers exist:
 * 
 * <ul>
 * <li>empty - shows nothing</li>
 * <li>search - shows the result of a search (node, line or coordinate)</li>
 * <li>highlight - shows the highlighted line beneath the mouse cursor</li>
 * <li>frc - set of layers according to the functional road class</li>
 * <li>fow - set of layers according to the form of way</li>
 * <li>location - shows the selected location</li>
 * <li>node - shows all nodes</li>
 * <li>line - shows all lines</li>
 * </ul>
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class MapLayerStore {

	/**
	 * The Enum MapFilter.
	 */
	public enum MapFilter {

		/** The FOW. */
		FOW,

		/** The FRC. */
		FRC;
	}

	/** The Constant frcLayers. */
	private final AbstractIntObjectMap frcLayers;

	/** The Constant fowLayers. */
	private final AbstractIntObjectMap fowLayers;

	/** The node layer. */
	private final MapLayer nodeLayer;

	/** The line layer. */
	private final MapLayer lineLayer;

	/** The search layer. */
	private MapLayer searchLayer;

	/** The location layer. */
	private MapLayer locationLayer;

	/** The location layer. */
	private MapLayer highlightLayer;

	/** The locations layer. */
	private MapLayer locationsLayer;

	/** The line layer for covered line of an area location. */
	private MapLayer coveredLinesAreaLocLayer;

	/** The handler. */
	private final MapLayerHandler handler;
	
    /**
     * Stores the currently active location change observers
     */
    private final ObserverManager<MapLayersObserver, MapLayerStore> changeObservers;

	/**
	 * Creates a new map store. It creates all layers which can be drawn and it
	 * initializes all layer data. The progress in creating the layers will be
	 * reported to the map load progress info dialog.
	 * 
	 * @param lines
	 *            the lines
	 * @param nodes
	 *            the nodes
	 * @param frcs
	 *            the frcs
	 * @param fows
	 *            the fows
	 * @throws MapViewerException
	 *             if map loading failed
	 */
	public MapLayerStore(final MapLayer lines, final MapLayer nodes,
			final OpenIntObjectHashMap frcs, final OpenIntObjectHashMap fows)
			throws MapViewerException {
	    
	    changeObservers = new ObserverManager<MapLayersObserver, MapLayerStore>(this);
		handler = new MapLayerHandler();
		lineLayer = lines;
		nodeLayer = nodes;
		frcLayers = frcs.copy();
		fowLayers = fows.copy();
		handler.register(lineLayer);
		handler.register(nodeLayer);
		for (int count = 0; count < fowLayers.size(); count++) {
			MapLayer ml = (MapLayer) fowLayers.get(count);
			handler.register(ml);
		}
		for (int count = 0; count < frcLayers.size(); count++) {
			MapLayer ml = (MapLayer) frcLayers.get(count);
			handler.register(ml);
		}
	}

	/**
	 * Sets the search layer.
	 * 
	 * @param s
	 *            the search layer
	 */
	public final void setSearchLayer(final MapLayer s) {
		removeSearchLayer();
		searchLayer = s;
		handler.register(searchLayer);
		searchLayer.setVisible(true);		
		changeObservers.notifyObservers();
	}

	/**
	 * Removes the search layer layer.
	 */
    public final void removeSearchLayer() {
        if (searchLayer != null) {
            handler.unregister(searchLayer);
            searchLayer = null;          
            changeObservers.notifyObservers();
        }
    }

	/**
	 * Sets the location layer.
	 * 
	 * @param s
	 *            the location layer
	 */
	public final void setLocationLayer(final MapLayer s) {
		removeLocationLayer();
		locationLayer = s;
		locationLayer.setVisible(true);
		handler.register(locationLayer);		
		changeObservers.notifyObservers();
	}

	/**
	 * Sets a new highlight layer.
	 * 
	 * @param s
	 *            the new highlight layer
	 */
	public final void setHighlightLayer(final MapLayer s) {
		removeHighlightLayer();
		highlightLayer = s;
		handler.register(highlightLayer);
		highlightLayer.setVisible(true);	
		changeObservers.notifyObservers();
	}

	/**
	 * Sets the locations layer.
	 * 
	 * @param s
	 *            the new locations layer
	 */
	public final void setLocationsLayer(final MapLayer s) {
		removeLocationsLayer();
		locationsLayer = s;
		locationsLayer.setVisible(true);
		handler.register(locationsLayer);	
		changeObservers.notifyObservers();
	}

	/**
	 * Removes the locations layer.
	 */
	public final void removeLocationsLayer() {
		if (locationsLayer != null) {
			locationsLayer.setVisible(false);
			handler.unregister(locationsLayer);
			locationsLayer = null;			
			changeObservers.notifyObservers();
		}
	}

    /**
     * Activates the standard layers. Removes all filter layers, activated the
     * line layer, deactivates the node layer.
     */
	public final void resetFilter() {
	    for (int i = 0; i <  frcLayers.size(); i++) {
	        ((MapLayer) frcLayers.get(i)).setVisible(false);
	    }
	    for (int i = 0; i <  fowLayers.size(); i++) {
	        ((MapLayer) fowLayers.get(i)).setVisible(false);
	    }
	    nodeLayer.setVisible(false);
		lineLayer.setVisible(true);		
		changeObservers.notifyObservers();
	}

	/**
	 * Activate selected frc layers.
	 * 
	 * @param status
	 *            the frc status
	 */
	public final void activateFRCLayers(final OpenIntObjectHashMap status) {
	    resetFilter();
	    lineLayer.setVisible(false);
		int countFRC = FunctionalRoadClass.getFRCs().size();
		for (int count = 0; count < countFRC; count++) {
			if ((Boolean) status.get(count)) {
				MapLayer ml = (MapLayer) frcLayers.get(count);
				ml.setVisible(true);
			}
		}		
		changeObservers.notifyObservers();
	}

	/**
	 * Activate selected fow layers.
	 * 
	 * @param status
	 *            the fow layer status
	 */
	public final void activateFOWLayers(final OpenIntObjectHashMap status) {
	    resetFilter();
	    lineLayer.setVisible(false);
		int countFOW = FormOfWay.getFOWs().size();
		for (int count = 0; count < countFOW; count++) {
			if ((Boolean) status.get(count)) {
				MapLayer ml = (MapLayer) fowLayers.get(count);
				ml.setVisible(true);
			}
		}		
		changeObservers.notifyObservers();
	}

	/**
	 * Activate filter layer (fow or frc according to the type) with id id.
	 * 
	 * @param type
	 *            the filter type
	 * @param id
	 *            the layer id
	 */
	public final void activateFilterLayer(final MapFilter type, final int id) {
		MapLayer layer = null;
		if (type == MapFilter.FRC) {
			layer = (MapLayer) frcLayers.get(id);
		} else if (type == MapFilter.FOW) {
			layer = (MapLayer) fowLayers.get(id);
		} else {
		    throw new IllegalArgumentException("Unexpected filter type " + type);
		}
		layer.setVisible(true);		
		changeObservers.notifyObservers();
	}

	/**
	 * Deactivate filter layer (fow or frc according to the type) with id id.
	 * 
	 * @param type
	 *            the filter type
	 * @param id
	 *            the layer id
	 */
	public final void deactivateFilterLayer(final MapFilter type, final int id) {
		MapLayer layer = null;
		if (type == MapFilter.FRC) {
			layer = (MapLayer) frcLayers.get(id);
		} else if (type == MapFilter.FOW) {
			layer = (MapLayer) fowLayers.get(id);
        } else {
            throw new IllegalArgumentException("Unexpected filter type " + type);
        }
		layer.setVisible(false);		
		changeObservers.notifyObservers();
	}

	/**
	 * Show node layer.
	 */
	public final void showNodeLayer() {
		nodeLayer.setVisible(true);
		handler.register(nodeLayer);		
		changeObservers.notifyObservers();
	}

	/**
	 * Hide node layer.
	 */
	public final void hideNodeLayer() {
		nodeLayer.setVisible(false);
		handler.unregister(nodeLayer);		
		changeObservers.notifyObservers();
	}

	/**
	 * Sets the covered lines area loc layer.
	 * 
	 * @param s
	 *            the new covered lines area loc layer
	 */
	public final void setCoveredLinesAreaLocLayer(final MapLayer s) {
		removeCoveredLinesAreaLocLayer();
		coveredLinesAreaLocLayer = s;
		handler.register(coveredLinesAreaLocLayer);		
		changeObservers.notifyObservers();
	}

	/**
	 * Removes the highlight layer.
	 */
	public final void removeHighlightLayer() {
		if (highlightLayer != null) {
			highlightLayer.setVisible(false);
			handler.unregister(highlightLayer);
			highlightLayer = null;			
			changeObservers.notifyObservers();
		}
	}

	/**
	 * Gets the map layer handler.
	 * 
	 * @return the map layer handler
	 */
	public final MapLayerHandler getMapLayerHandler() {
		return handler;
	}

	/**
	 * Removes the covered lines area loc layer.
	 */
	public final void removeCoveredLinesAreaLocLayer() {
		handler.unregister(coveredLinesAreaLocLayer);
		coveredLinesAreaLocLayer = null;		
		changeObservers.notifyObservers();

	}

	/**
	 * Removes the location layer.
	 */
	public final void removeLocationLayer() {
		handler.unregister(locationLayer);
		locationLayer = null;		
		changeObservers.notifyObservers();
	}
	
    /**
     * Adds an observer for changes in the map layers. This will
     * lead to a hard reference from this object to the listener. For memory
     * purposes clients should therefore take care to remove the observer
     * properly if they are intended to shut down!
     * 
     * @param observer
     *            The observer to add
     */
    public final void addLayersChangeObserver(
            final MapLayersObserver observer) {
        changeObservers.addObserver(observer);
    }

    /**
     * Removes a registered observers.
     * 
     * @param observer
     *            The observer instance to remove
     */
    public final void removeLayesChangeObserver(
            final MapLayersObserver observer) {
        changeObservers.removeObserver(observer);
    }   	

}
