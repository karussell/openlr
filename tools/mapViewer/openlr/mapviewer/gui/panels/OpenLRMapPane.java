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

import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import openlr.geomap.JMapDataRepository;
import openlr.geomap.JMapPane;
import openlr.map.GeoCoordinates;
import openlr.map.MapDatabase;
import openlr.mapviewer.MapViewer;
import openlr.mapviewer.gui.layer.MapLayerStore;
import openlr.mapviewer.gui.layer.MapLayersObserver;
import openlr.mapviewer.properties.MapViewerProperties;
import openlr.mapviewer.properties.MapViewerPropertiesObserver;
import openlr.mapviewer.utils.bbox.BoundingBox;

/**
 * The OpenLRMapPane extends the JMapPane with drawing a scale at the bottom of
 * the map pane and drawing a measured line while measuring is enabled.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class OpenLRMapPane implements MapLayersObserver,
        MapViewerPropertiesObserver {
	
	/** The map bb. */
	private final Rectangle2D.Double mapBB;
	
	/**
	 * The encapsulated but hidden JMapPane
	 */
	private final JMapPane jMapPane;
	
    /**
     * The tool manager that channels all the requests to register or remove to
     * the map graphics.
     */
    private final MapToolManager toolManager; 

	
	/**
	 * Instantiates a new open lr map pane.
	 *
	 * @param map the map
	 * @param store the store
	 * @param repo the map data repository
	 */
	public OpenLRMapPane(final MapDatabase map, final MapLayerStore store, final JMapDataRepository repo) {
		jMapPane = new JMapPane(store.getMapLayerHandler(), repo);
		jMapPane.setScaleColor(MapViewer.PROPERTIES.getMapPaneScaleColor());
		toolManager = new MapToolManager(jMapPane);
		mapBB = map.getMapBoundingBox();
		store.addLayersChangeObserver(this);
		MapViewer.PROPERTIES.addObserver(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void update(final MapLayerStore o) {
		jMapPane.repaint();		
	}
	
	/**
	 * Reset map view.
	 */
    public final void resetMapView() {
        jMapPane.setViewport(mapBB.x, mapBB.y, mapBB.x + mapBB.width, mapBB.y
                + mapBB.height);
    }	
    
    /**
     * Delivers the tool manager that allows to add or remove tools to the map
     * graphics.
     * 
     * @return The tool manager
     */
    public final MapToolManager getToolManager() {
        return toolManager;
    }
    
    /**
     * Registers the text to display in a tool tip.
     * 
     * @param message
     *            The message to display
     */
    public final void setToolTipText(final String message) {
        jMapPane.setToolTipText(message);
    }

    /**
     * Saves the image of the current map graphics in PNG format.
     * 
     * @param file
     *            the file to write to
     * @throws IOException
     *             If an error occurs while saving the image
     */
    public final void saveImage(final File file) throws IOException {
    	jMapPane.setSize(jMapPane.getWidth(), jMapPane.getHeight());
        RenderedImage im = jMapPane.getBaseImage();
        ImageIO.write(im, "png", file);
    }   
    
    /**
     * Sets the new viewport of the map graphics. The map will display at least
     * the given bounding box. If the map pane is of a different aspect ration
     * than the given box there will be more viewport drawn in one dimension.
     * 
     * @param boundingBox
     *            the bounding box to set the viewport to
     */
    public final void setViewport(final BoundingBox boundingBox) {
        GeoCoordinates lowerLeft = boundingBox.getLowerLeft();
        GeoCoordinates upperRight = boundingBox.getUpperRight();
        jMapPane.setViewport(lowerLeft.getLongitudeDeg(),
                lowerLeft.getLatitudeDeg(), upperRight.getLongitudeDeg(),
                upperRight.getLatitudeDeg());
    }

    /**
     * Delivers the encapsulated {@link JMapPane}
     * 
     * @return the {@link JMapPane}
     */
    final JMapPane getJMapPane() {
        return jMapPane;
    }

    /**
     * Repaints if a color property changed
     * 
     * @param changedKey
     *            The key of the changed color property
     */
    @Override
    public final void updateProperty(final String changedKey) {
        if (MapViewerProperties.isMapDrawingProperty(changedKey)) {
            jMapPane.repaint();
        }
    }

}
