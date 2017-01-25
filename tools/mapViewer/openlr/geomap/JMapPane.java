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
package openlr.geomap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import openlr.geomap.tools.CursorTool;
import openlr.geomap.transform.JMapTransformMercator;

/**
 * The Class JMapPane.
 */
public class JMapPane extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8780374920366100134L;

	/** The img canvas. */
	private Image imgCanvas;

	/** The current transform. */
	private JMapTransformMercator currentTransform;
	
    /**
     * The currently active cursor tool.
     */
    private CursorTool activeCursorTool;

    /**
     * The set of additional paint tasks
     */
    private Set<AdditionalMapPanePaintTask> paintTasks = Collections
            .synchronizedSet(new HashSet<AdditionalMapPanePaintTask>());

	/** The handler. */
    private final MapLayerHandler handler;

	/** The image x. */
	private int imageX = 0;

	/** The position of the drawn map image in AWT coordinate space (source top left)! */
	private int imageY = 0;
	
	/** The longitude of the lower left coordinate. */
	private double llLon;

	/** The latitude of the lower left coordinate. */
	private double llLat;

	/** The longitude of the upper right coordinate. */
	private double urLon;

	/** The latitude of the upper right coordinate. */
	private double urLat;
	
	private final JMapDataRepository mapDataRepo;
	
	/**
	 * The color for drawing the scale with default value
	 */
	private Color scaleColor = Color.RED;

	/**
	 * Instantiates a new j map pane.
	 * 
	 * @param h
	 *            the h
	 */
    public JMapPane(final MapLayerHandler h, final JMapDataRepository mapData) {
        handler = h;
        addComponentListener(new UpdateTransformationOnResizeListener(this));

        ZoomAndDragMouseListener zoomAndDragListener = new ZoomAndDragMouseListener(
                this);
        addMapMouseListener(zoomAndDragListener);
        mapDataRepo = mapData;
        
        Rectangle2D defaultViewport = mapData.getMapRectangle();
        llLon = defaultViewport.getMinX();
        llLat = defaultViewport.getMinY();
        urLon = defaultViewport.getMaxX();
        urLat = defaultViewport.getMaxY();
        currentTransform = new JMapTransformMercator(getBounds(), llLon, llLat,
                urLon, urLat);
    }
	
	/**
	 * Gets the center y.
	 *
	 * @return the center y
	 */
	final double getCenterY() {
		return llLat + ((urLat - llLat) / 2);
	}
	
	/**
	 * Gets the min x.
	 *
	 * @return the min x
	 */
	final double getMinX() {
		return llLon;
	}
	
	/**
	 * Gets the max x.
	 *
	 * @return the max x
	 */
	final double getMaxX() {
		return urLon;
	}
	

    /**
     * Sets the new viewport of the map graphics. The map will display at least
     * the given bounding box. If the map pane is of a different aspect ration
     * than the given box there will be more viewport drawn in one dimension.
     * 
     * @param lowerLeftLong
     *            the longitude value of the lower left
     * @param lowerLeftLat
     *            the latitude value of the lower left
     * @param upperRightLon
     *            the longitude value of the upper right
     * @param upperRightLat
     *            the longitude value of the upper right
     */
    public final void setViewport(final double lowerLeftLong,
            final double lowerLeftLat, final double upperRightLon,
            final double upperRightLat) {
        
        Rectangle boundsScreen = getBounds();
        
        if (boundsScreen.isEmpty()) {

            // the screen has size zero (yet), just store the requested viewport
            llLon = lowerLeftLong;
            urLon = upperRightLon;
            llLat = lowerLeftLat;
            urLat = upperRightLat;            
            
        } else {
            
            currentTransform = new JMapTransformMercator(boundsScreen,
                    lowerLeftLong, lowerLeftLat, upperRightLon, upperRightLat);

            // we have to recalculate our stored world coordinates since the
            // effective viewport can have larger bounds than the requested input
            Point2D.Double lowerLeftPoint = currentTransform.getGeoCoordinate(0, 0);
            Point2D.Double upperRightPoint = currentTransform.getGeoCoordinate(
                    boundsScreen.width, boundsScreen.height);

            llLon = lowerLeftPoint.x;
            urLon = upperRightPoint.x;
            llLat = lowerLeftPoint.y;
            urLat = upperRightPoint.y;
            
            repaint();            
        }
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void paint(final Graphics g) {
		Dimension size = getSize();
        imgCanvas = createImage(size.width, size.height);
		Graphics2D ig = (Graphics2D) imgCanvas.getGraphics();

		ig.setBackground(Color.WHITE);
		// apply the white background filling
		ig.clearRect(0, 0, size.width, size.height);
		
		// apply geomap transform (coordinate source in the lower left)
		AffineTransform affineTransform = new AffineTransform();
		affineTransform.translate(0, getHeight());
		affineTransform.scale(1, -1);
		ig.setTransform(affineTransform);
		ig.setPaintMode();
		handler.drawLayer(ig, currentTransform, mapDataRepo);
		Scale.drawScale(ig, this, scaleColor);

		paintAdditions(ig);
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, size.width, size.height);
		g.drawImage(imgCanvas, imageX, imageY, this);
	}
	
    /**
     * Draws additional paint tasks to the given graphics instance
     * @param g The graphics to paint on
     */
    private void paintAdditions(final Graphics2D g) {
        
        synchronized (paintTasks) {
            
            for (AdditionalMapPanePaintTask proc : paintTasks) {
                proc.paintComponent(g, getSize());
            }
        }
    }
	
    /**
     * Moves image of the map graphics by the given deltas. <strong>The values
     * are interpreted on a coordinate system having the source (0,0) in the
     * lower left.</strong>
     * 
     * @param deltaX
     *            the delta in horizontal direction
     * @param deltaY
     *            the delta in vertical direction
     */
	public final void moveImage(final int deltaX, final int deltaY) {
		imageX = deltaX;
        // invert! because imageY is used in AWT coordinate space with source
        // top left
		imageY = -deltaY;
		repaint();
	}

	/**
	 * Gets the base image.
	 * 
	 * @return the base image
	 */
	public final BufferedImage getBaseImage() {
		final BufferedImage buffImg = new BufferedImage(
				imgCanvas.getWidth(null), imgCanvas.getHeight(null),
				BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2 = buffImg.createGraphics();
		g2.drawImage(imgCanvas, null, null);
		g2.dispose();
		return buffImg;
	}

	/**
	 * Updates the world-to-screen transformation to the given new screen bounds
	 */
    private void updateTransformToResizedPane() {
        // just request the current viewport again, we know that setViewport()
        // takes the current screen size into account
        setViewport(llLon, llLat, urLon, urLat);        
    }

	/**
	 * A component adapter tracking the map pane. It starts the recalculation
	 * of the world-to-screen calculation every time the map pane is resized.
	 */
    private static final class UpdateTransformationOnResizeListener extends
            ComponentAdapter {

		/** The panel. */
		private final JMapPane panel;

		/**
		 * Instantiates a new map panel listener.
		 * 
		 * @param p
		 *            the p
		 */
		public UpdateTransformationOnResizeListener(final JMapPane p) {
			panel = p;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void componentResized(final ComponentEvent e) {
			panel.updateTransformToResizedPane();
		}
	}

	/**
	 * Gets the transform.
	 *
	 * @return the transform
	 */
	public final JMapTransformMercator getTransform() {
		return currentTransform;
	}
	
	/**
	 * This sets the color to draw the scale
	 * @param color The color to draw the scale
	 */
	public final void setScaleColor(final Color color) {
	    scaleColor = color;
	}
	

    /**
     * Sets the cursor tool and replaces the former one. The former cursor tool
     * will get called by its {@link CursorTool#deactivate()} method if the new
     * tool instance is not equal to the former one.
     * 
     * @param newTool
     *            The cursor tool to set.
     */
    public final void setCursorTool(final CursorTool newTool) {

        CursorTool old = activeCursorTool;

        boolean unsetFormer = (old != null);

        if (newTool != null) {

            unsetFormer = unsetFormer && !old.equals(newTool);
            setCursor(newTool.getCursor());
            addMapMouseListener(newTool);
        } else {
            setCursor(null);
        }

        if (unsetFormer) {
            old.deactivate();
            removeMapMouseListener(old);
        }      
     
        activeCursorTool = newTool;
    }

    /**
     * Delivers the currently active cursor tool, can be {@code null}.
     * 
     * @return The active cursor tool
     */
    public final CursorTool getActiveCursorTool() {
        return activeCursorTool;
 }
    
    /**
     * Adds an {@link AdditionalMapPanePaintTask} to the list.
     * 
     * @param proc
     *            The processor
     */
    public final void addAdditionalMapPanePaintTask(
            final AdditionalMapPanePaintTask proc) {
        paintTasks.add(proc);
        repaint();
    }

    /**
     * Removes a registered {@link AdditionalMapPanePaintTask} from the list.
     * 
     * @param proc
     *            The post processor to remove.
     */
    public final void removeAdditionalMapPanePaintTask(
            final AdditionalMapPanePaintTask proc) {
        paintTasks.remove(proc);
        repaint();
    }	
	
    /**
     * Adds a map mouse listener that gets informed about mouse events on the
     * map graphics. This method should be preferred to
     * {@link #addMouseListener(MouseListener)} and
     * {@link #addMouseMotionListener(java.awt.event.MouseMotionListener)} since
     * it delivers the resolved geo coordinate of the mouse event besides the
     * pure pixel position.
     * 
     * @param listener
     *            The map mouse listener to add
     */
    public final void addMapMouseListener(final MapMouseListener listener) {
        MapMouseListenerAdapter adapter = new MapMouseListenerAdapter(this,
                listener);
        addMouseListener(adapter);
        addMouseMotionListener(adapter);
    }

    /**
     * Removes the given instance of {@link MapMouseListener} from the list of
     * active listeners.
     * 
     * @param listener
     *            The listener instance to remove
     */
    public final void removeMapMouseListener(final MapMouseListener listener) {

        // our mouse listeners wrapped in MapMouseListenerAdapter make it a bit
        // more complicated to find the corresponding one to remove
        for (MouseListener current : getMouseListeners()) {
            if (current instanceof MapMouseListenerAdapter) {
                if (((MapMouseListenerAdapter) current).getDelegate().equals(
                        listener)) {

                    removeMouseListener(current);
                    removeMouseMotionListener((MapMouseListenerAdapter) current);
                    break;
                }
            }
        }
    } 

}
