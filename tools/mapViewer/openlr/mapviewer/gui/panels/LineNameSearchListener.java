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

import java.awt.Container;
import java.awt.Cursor;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import openlr.map.Line;
import openlr.map.MapDatabase;
import openlr.mapviewer.gui.MapViewerGui;
import openlr.mapviewer.gui.ShutDownManager;
import openlr.mapviewer.gui.ShutDownManager.ShutDownAction;
import openlr.mapviewer.linesearch.gui.LineSearchComboBox;
import openlr.mapviewer.linesearch.model.LineNameModel;
import openlr.mapviewer.linesearch.model.LineNameModelException;

/**
 * This class implements the activation of the line name search functionality.
 * It also handles the visualization of the initialization progress by
 * displaying a progress bar.
 */
final class LineNameSearchListener implements ItemListener {

    /**
     * A map of all yet initialized {@link LineNameModel}s
     */
    private static final Map<String, LineNameModel> LINE_NAME_MODELS = new HashMap<String, LineNameModel>();

    /**
     * A reference to the map database
     */
    private final MapDatabase map;

    /**
     * The line name search box
     */
    private final LineSearchComboBox lineNameBox;

    /**
     * Creates a new instance of the initializer
     * 
     * @param lineSearchBox
     *            A reference to line search box. <b>It is expected that the
     *            line search box is child of a container in the UI that acts as
     *            a placeholder for the required space!</b> The initializer will
     *            remove the search box from its parent and add a progress bar
     *            instead. After successful initialization the
     *            {@link LineSearchComboBox} will be exchanged again with the
     *            progress bar, i.e. re-attached as the single child to its
     *            former parent.
     * @param mapDatabase
     *            A reference to the map database
     */
    LineNameSearchListener(final LineSearchComboBox lineSearchBox,
            final MapDatabase mapDatabase) {
        map = mapDatabase;
        lineNameBox = lineSearchBox;
    }

    /**
     * Implements the handling of the activation button of the line name search
     * 
     * @param e
     *            The related event
     */
    @Override
    public void itemStateChanged(final ItemEvent e) {

        if (e.getStateChange() == ItemEvent.SELECTED) {
            if (!lineNameBox.isInitialized()) {
                initLineNameSearch();
            }
            lineNameBox.setEnabled(true);
            lineNameBox.requestFocus();
        } else {
            lineNameBox.setEnabled(false);
        }

    }

    /**
     * Starts a thread that asynchronously initializes the line name search if
     * necessary.
     */
    private void initLineNameSearch() {

        Thread thread = new Thread() {

            @Override
            public void run() {
                initLineNameSearchImpl();
            }
        };

        thread.start();
    }

    /**
     * Initializes the line name search if necessary, i.e. if no line name model
     * was created before for the related map.
     */
    private void initLineNameSearchImpl() {

        final Container container = lineNameBox.getParent();
        final Cursor cursorBefore = MapViewerGui
                .getApplicationCursor(container);

        MapViewerGui.setApplicationCursor(container, MapViewerGui.WAIT_CURSOR);

        try {

            displayProgressBar(container);

            try {
                LineNameModel model = getOrCreateLineNameModel(map);
                lineNameBox.setLineNameModel(model);
            } catch (LineNameModelException e) {

                JOptionPane.showMessageDialog(null,
                        "Error indexing all network line names. Details: " + e,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

            displayLineSearchBox(container);

            lineNameBox.requestFocus();

        } finally {
            MapViewerGui.setApplicationCursor(container, cursorBefore);

        }
    }

    /**
     * Places the progress bar inside the given container.
     * 
     * @param container
     *            The container to put the progress bar in
     */
    private void displayProgressBar(final Container container) {

        container.removeAll();

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString("Indexing all network line names");

        container.add(progressBar);
        container.getParent().invalidate();
        container.validate();
    }

    /**
     * Places the line search box inside the given container.
     * 
     * @param container
     *            The container to put the line search box in
     */
    private void displayLineSearchBox(final Container container) {
        container.removeAll();
        container.add(lineNameBox);
        container.getParent().invalidate();
        container.repaint();
    }

    /**
     * Delivers a line name model for the map of the given index. If no
     * {@link LineNameModel} was initialized for this map so far a new model is
     * set up. This can be a long running process!
     * 
     * @param mapDatabase
     *            The map database the model shall be retrieved for
     * @return The initialized line name model
     * @throws LineNameModelException
     *             If an exception occurs during setup of the model
     */
    private synchronized LineNameModel getOrCreateLineNameModel(
            final MapDatabase mapDatabase) throws LineNameModelException {

        String identifier = String.valueOf(mapDatabase.hashCode());
        LineNameModel model = LINE_NAME_MODELS.get(identifier);

        if (model == null) {

            final LineNameModel newModel = new LineNameModel(identifier);
            try {

                Iterator<Line> allLines = mapDatabase.getAllLines();
                while (allLines.hasNext()) {
                    newModel.addToBatch(allLines.next());
                }

                newModel.index();

                LINE_NAME_MODELS.put(identifier, newModel);

                model = newModel;

            } finally {

                ShutDownManager.getInstance().addShutDownAction(
                        new ShutDownAction() {

                            @Override
                            public void shutDown() {
                                newModel.cleanup();
                            }
                        });
            }
        }

        return model;
    }
}
