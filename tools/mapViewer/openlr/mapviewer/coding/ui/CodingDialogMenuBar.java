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
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import openlr.mapviewer.coding.CodingPropertiesHolder;
import openlr.mapviewer.gui.filechoose.FileChooserFactory;

/**
 * The menu bar of the encoding and decoding properties UI
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
class CodingDialogMenuBar extends JMenuBar {

    /**
     * Default serial version ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create a new menu bar.
     * 
     * @param codingDialog
     *            The parent window used for the closing action.
     * @param codingPropertiesHolder
     *            The provider of the central references to the OpenLR encoder
     *            or decoder settings
     * @param fcf
     *            The file chooser factory to use for file dialogs
     * @param applyProcessor
     *            The processor that applies the current values from the form to
     *            the runtime properties
     */
    public CodingDialogMenuBar(final AbstractCodingOptionsDialog codingDialog,
            final CodingPropertiesHolder codingPropertiesHolder,
            final FileChooserFactory fcf,
            final ApplyChangesListener applyProcessor) {

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        add(fileMenu);

        String codingTypeString = codingDialog.getCodingType().name()
                .toLowerCase();

        JMenuItem loadAction = new JMenuItem("Load settings");
        loadAction.setToolTipText("Loads a stored " + codingTypeString
                + " configuration from file");
        loadAction.setMnemonic(KeyEvent.VK_L);

        JMenuItem saveAction = new JMenuItem("Save settings");
        saveAction.setToolTipText("Saves a stored " + codingTypeString
                + " configuration to file");
        saveAction.setMnemonic(KeyEvent.VK_S);

        JMenuItem exitAction = new JMenuItem("Exit");
        exitAction.setToolTipText("Closes this dialog");
        exitAction.setMnemonic(KeyEvent.VK_X);

        fileMenu.add(loadAction);
        fileMenu.add(saveAction);
        fileMenu.add(new JSeparator());
        fileMenu.add(exitAction);

        exitAction.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                codingDialog.dispatchEvent(new WindowEvent(codingDialog,
                        WindowEvent.WINDOW_CLOSING));
            }
        });

        loadAction.addActionListener(new LoadConfigButtonListener(codingDialog,
                fcf));
        saveAction.addActionListener(new SaveConfigButtonListener(codingDialog,
                fcf, codingPropertiesHolder, applyProcessor));

    }
}
