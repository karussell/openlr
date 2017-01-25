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
package openlr.geomap.tools;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

import openlr.geomap.AdditionalMapPanePaintTask;
import openlr.geomap.JMapPane;
import openlr.geomap.MapMouseEvent;
import openlr.map.GeoCoordinates;
import openlr.map.utils.GeometryUtils;

/**
 * The MeasureTool is used to measure distances within the map. When this cursor
 * tool is attached to the {@link JMapPane} it automatically starts its job.
 * When it's intended to stop the tool it is required to call
 * {@link #stopMeasurement()} from outside. The tool then unregisters from
 * painting its graphics above the map pane.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class Measure extends CursorTool {

    /** The measure cursor. */
    private static final Cursor CURSOR = new Cursor(Cursor.CROSSHAIR_CURSOR);
    

    /** The start coordinate. */
    private GeoCoordinates startCoord;

    /**
     * The object that processes the output.
     */
    private final MeasurementReceiver output;

    /**
     * Flag that stores whether the graphics processor is currently assigned to
     * the map pane.
     */
    private boolean graphicsProcessorActive = false;

    /**
     * A reference to the map pane, will first be initialized if there was one
     * measure event
     */
    private JMapPane mPane;

    /**
     * The paint tasks that draws the measurement tool onto the map
     */
    private final DrawMeasureLine paintJob; 

    /**
     * Creates a new instance of the measure tool
     * 
     * @param receiver
     *            The receiver of the measurement information
     * @param toolColor
     *            The color to use for the measure tool draw to the map
     */
    public Measure(final MeasurementReceiver receiver, final Color toolColor) {
        output = receiver;
        paintJob = new DrawMeasureLine(toolColor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseClicked(final MapMouseEvent event) {
        startMeasurement(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseMoved(final MapMouseEvent event) {
        GeoCoordinates end = event.getCoordinate();
        if (startCoord != null) {

            double dist = GeometryUtils.distance(startCoord.getLongitudeDeg(),
                    startCoord.getLatitudeDeg(), end.getLongitudeDeg(),
                    end.getLatitudeDeg());

            double bearing = GeometryUtils.bearing(
                    startCoord.getLongitudeDeg(), startCoord.getLatitudeDeg(),
                    end.getLongitudeDeg(), end.getLatitudeDeg());

            if (output != null) {
                output.newMeasurement(startCoord, end, dist, bearing);
            }
            
            paintJob.currentEndCoordinate = end;

            event.getMapPane().repaint();
        }
    }

    /**
     * Sets new measurement.
     * 
     * @param event
     *            The related map mouse event
     */
    private void startMeasurement(final MapMouseEvent event) {
        mPane = event.getMapPane();

        startCoord = event.getCoordinate();

        if (!graphicsProcessorActive) {
            mPane.addAdditionalMapPanePaintTask(paintJob);
            graphicsProcessorActive = true;
        }
    }

    /**
     * Reset measurement values.
     */
    private void stopMeasurement() {
        if (mPane != null) {
          mPane.removeAdditionalMapPanePaintTask(paintJob);
        }
        graphicsProcessorActive = false;
        startCoord = null;
    }

    /**
     * Get the mouse cursor for this tool.
     * 
     * @return the cursor
     */
    @Override
    public final Cursor getCursor() {
        return CURSOR;
    }

    /**
     * Stops the measuring
     */
    @Override
    public final void deactivate() {
        stopMeasurement();
    }

    /**
     * Implements the drawing of the measure tool to the map.
     */
    private final class DrawMeasureLine implements AdditionalMapPanePaintTask {
        
        /**
         * Color of the measurement tool drawn on the map
         */
        private final Color color;

        /**
         * The current end coordinate of a measurement
         */
        private GeoCoordinates currentEndCoordinate;

        /** The Constant POINT_OFFSET. */
        private static final int POINT_OFFSET = 3;
        
        /**
         * Creates the paint job.
         * @param col The color to draw the measurement line
         */
        public DrawMeasureLine(final Color col) {
            this.color =  col;
        }

        /**
         * Draws the measure line and start point above the map graphics.
         * 
         * @param g
         *            {@inheritDoc}
         * @param screenSize
         *            {@inheritDoc}
         */
        @Override
        public void paintComponent(final Graphics2D g,
                final Dimension screenSize) {
            if (startCoord != null && currentEndCoordinate != null) {
                Color c = g.getColor();
                
                // always re-calculate the pixel coordinates 
                // since the viewport of the map can have moved
                Point startPoint = mPane.getTransform().getPixel(
                        startCoord.getLongitudeDeg(),
                        startCoord.getLatitudeDeg());
                Point endPoint = mPane.getTransform().getPixel(
                        currentEndCoordinate.getLongitudeDeg(),
                        currentEndCoordinate.getLatitudeDeg());
                
                g.setColor(color);
                int graphicsStartY = startPoint.y;
                int graphicsEndY = endPoint.y;
                g.fillOval(startPoint.x - POINT_OFFSET, graphicsStartY - POINT_OFFSET,
                        2 * POINT_OFFSET, 2 * POINT_OFFSET);
                g.drawLine(startPoint.x, graphicsStartY, endPoint.x, graphicsEndY);
                g.setColor(c);
            }
        }
    }

}
