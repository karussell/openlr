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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import openlr.mapviewer.coding.CodingPropertiesHolder;
import openlr.mapviewer.gui.filechoose.FileChooserFactory;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.FileConfiguration;

/**
 * Saves the current values of the form to a OpenLR configuration file
 */
final class SaveConfigButtonListener implements ActionListener {

    /**
     * A reference to the dialog that holds the configuration to save
     */
    private final AbstractCodingOptionsDialog codeOptionsDialog;

    /**
     * A file chooser topic that identifies file choosers related to encoding or
     * decoding properties
     */
    private static final String FILE_CHOOSER_TOPIC_DECODER_PROPERTIES = "CODING_PROPERTIES";

    /**
     * The factory to use for creating file choosers
     */
    private final FileChooserFactory fileChooserFactory;

    /**
     * The encoding and decoding properties holder
     */
    private final CodingPropertiesHolder codingPropertiesHolder;

    /**
     * The processor that applies the current values from the form to the
     * runtime properties
     */
    private final ApplyChangesListener applyChangesActionListener;

    /**
     * Creates the listener
     * 
     * @param fcf
     *            The file chooser factory to use
     * @param codingOptionsDialog
     *            A reference to the dialog to set the loaded configuration
     * @param codingPropsHolder
     *            The encoding and decoding properties holder
     * @param applyActionListener
     *            The processor that applies the current values from the form to
     *            the runtime properties
     */
    SaveConfigButtonListener(
            final AbstractCodingOptionsDialog codingOptionsDialog,
            final FileChooserFactory fcf,
            final CodingPropertiesHolder codingPropsHolder,
            final ApplyChangesListener applyActionListener) {

        codeOptionsDialog = codingOptionsDialog;
        fileChooserFactory = fcf;
        codingPropertiesHolder = codingPropsHolder;
        applyChangesActionListener = applyActionListener;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {

        JFileChooser chooser = fileChooserFactory
                .createFileChooser(FILE_CHOOSER_TOPIC_DECODER_PROPERTIES);
        String codingTypeString = codeOptionsDialog.getCodingType().name()
                .toLowerCase();
        chooser.setDialogTitle("Select the file to save the "
                + codingTypeString + " properties to");

        int re = chooser.showSaveDialog(codeOptionsDialog);
        if (re == JFileChooser.APPROVE_OPTION) {

            File file = chooser.getSelectedFile();

            if (writePermitted(file)) {

                applyChangesActionListener.applyChanges();

                FileConfiguration currentConfig = codingPropertiesHolder
                        .getProperties(codeOptionsDialog.getCodingType());

                try {

                    currentConfig.save(file);

                } catch (ConfigurationException e1) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Could not write the properties to file "
                                    + file.getAbsolutePath() + ". " + e1,
                            "Write error", JOptionPane.ERROR_MESSAGE);
                }
            }

        }
    }

    /**
     * Checks if writing to the specified file is valid. If the target file
     * already exists the user is asked for confirmation to overwrite it. In
     * case of confirmation or if the file not yet exists {@code true} is
     * returned, otherwise {@code false}.
     * 
     * @param target
     *            The target file to check
     * @return whether it is valid to write to that file
     */
    private boolean writePermitted(final File target) {

        boolean write = true;
        if (target.exists()) {

            int choice = JOptionPane
                    .showConfirmDialog(
                            codeOptionsDialog,
                            "File "
                                    + target.getName()
                                    + " already exists. Do you really want to overwrite it?",
                            "File already exists", JOptionPane.YES_NO_OPTION);

            if (choice != JOptionPane.OK_OPTION) {
                write = false;
            }
        }
        return write;
    }
}
