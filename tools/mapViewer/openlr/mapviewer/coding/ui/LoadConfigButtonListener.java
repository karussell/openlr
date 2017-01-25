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
package openlr.mapviewer.coding.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import openlr.mapviewer.gui.filechoose.FileChooserFactory;
import openlr.mapviewer.gui.utils.WrappedLabelLookAlikeTextArea;
import openlr.properties.OpenLRPropertiesReader;
import openlr.properties.OpenLRPropertyException;

import org.apache.commons.configuration.FileConfiguration;

/**
 * Load a stored configuration.
 */
final class LoadConfigButtonListener implements ActionListener {

    /**
     * The size of the text field displaying the error message
     */
    private static final Dimension PREFERRED_SIZE_ERROR_TEXT_FIELD = new Dimension(
            300, 80);

    /**
     * The number of columns of the text field displaying the error message
     */
    private static final int NR_COLUMNS_ERROR_TEXT_FIELD = 33;

    /**
     * A reference to the dialog to set the loaded configuration
     */
    private final AbstractCodingOptionsDialog codeOptionsDialog;

    /**
     * A file chooser topic that identifies file choosers related to encoding or
     * decoding properties
     */
    private static final String FILE_CHOOSER_TOPIC_CODING_PROPERTIES = "CODING_PROPERTIES";

    /**
     * The factory to use for creating file choosers
     */
    private final FileChooserFactory fileChooserFactory;

    /**
     * Creates the listener
     * 
     * @param fcf
     *            The file chooser factory to use
     * @param codingOptionsDialog
     *            A reference to the dialog to set the loaded configuration
     */
    LoadConfigButtonListener(
            final AbstractCodingOptionsDialog codingOptionsDialog,
            final FileChooserFactory fcf) {

        this.codeOptionsDialog = codingOptionsDialog;
        fileChooserFactory = fcf;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {

        JFileChooser chooser = fileChooserFactory
                .createFileChooser(FILE_CHOOSER_TOPIC_CODING_PROPERTIES);
        chooser.setFileFilter(new XmlOrPropertiesFileFilter());
        
        String codingTypeString = codeOptionsDialog.getCodingType().name()
                .toLowerCase();
        chooser.setDialogTitle("Select " + codingTypeString
                + " properties file");
        int re = chooser.showOpenDialog(null);
        if (re == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                FileConfiguration config = OpenLRPropertiesReader
                        .loadPropertiesFromFile(file);
                this.codeOptionsDialog.setValues(config);

            } catch (OpenLRPropertyException e1) {

                WrappedLabelLookAlikeTextArea mapNameArea = new WrappedLabelLookAlikeTextArea(
                        1, NR_COLUMNS_ERROR_TEXT_FIELD);
                mapNameArea.setText("Error reading the " + codingTypeString
                        + " properties! " + e1);
                mapNameArea.setPreferredSize(PREFERRED_SIZE_ERROR_TEXT_FIELD);

                JOptionPane.showMessageDialog(null, mapNameArea,
                        "Properties error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /** 
     * A file filter allowing XML and Properties files
     */
    private final class XmlOrPropertiesFileFilter extends FileFilter {
        
        @Override
        public String getDescription() {
            return ".xml or .properties files";
        }

        @Override
        public boolean accept(final File f) {
            String name = f.getName().toLowerCase();
            return name.endsWith(".xml") || name.endsWith(".properties");
        }
    }

}
