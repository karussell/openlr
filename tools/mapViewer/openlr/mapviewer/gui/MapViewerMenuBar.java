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
package openlr.mapviewer.gui;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import openlr.mapviewer.coding.CodingPropertiesHolder;
import openlr.mapviewer.coding.ui.DecodingOptionsDialog;
import openlr.mapviewer.coding.ui.EncodingOptionsDialog;
import openlr.mapviewer.gui.filechoose.FileChooserFactory;
import openlr.mapviewer.gui.panels.AboutDialog;
import openlr.mapviewer.gui.panels.options.OptionsDialog;

/**
 * The menu bar of the {@link MapViewerGui}.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class MapViewerMenuBar extends JMenuBar {

	/** The options dialog. */
	private OptionsDialog preferencesDialog;
	
	/** A reference to the decoding options dialog */
	private DecodingOptionsDialog decodeOptionsDialog;
	
	/** A reference to the encoding options dialog */
	private EncodingOptionsDialog encodeOptionsDialog;

	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create a new menu bar.
	 * 
	 * @param parentWindow
	 *          The parent window used for the closing action.
	 * @param codingPropertiesHolder     
     *          The provider of the central references to the OpenLR encoder or 
     *          decoder settings
     * @param fcf 
     *          The file chooser factory to use for file dialogs
	 */
    public MapViewerMenuBar(final Window parentWindow,
            final CodingPropertiesHolder codingPropertiesHolder,
            final FileChooserFactory fcf) {
        
		createMenus(parentWindow, codingPropertiesHolder, fcf);
	}

	/**
	 * Performs the initialization of the menu bar elements.
	 * 
	 * @param parentWindow
	 *          The parent window used for the closing action.
     * @param codingProperties     
     *          The provider of the central references to the OpenLR encoder or 
     *          decoder settings
     * @param fileChooserFactory 
     *          The file chooser factory to use for file dialogs          
	 */
    private void createMenus(final Window parentWindow,
            final CodingPropertiesHolder codingProperties,
            final FileChooserFactory fileChooserFactory) {

		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		JMenu editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		add(fileMenu);
		add(editMenu);
		add(helpMenu);

		JMenuItem prefsAction = new JMenuItem("Preferences");
		prefsAction.setMnemonic(KeyEvent.VK_P);
		JMenuItem encodeOptionsAction = new JMenuItem("Encoding settings");
		encodeOptionsAction.setMnemonic(KeyEvent.VK_E);
		JMenuItem decodeOptionsAction = new JMenuItem("Decoding settings");
		decodeOptionsAction.setMnemonic(KeyEvent.VK_D);
		JMenuItem exitAction = new JMenuItem("Exit");
		exitAction.setMnemonic(KeyEvent.VK_X);

		JMenuItem aboutAction = new JMenuItem("About MapViewer");
		aboutAction.setMnemonic(KeyEvent.VK_A);

		fileMenu.add(exitAction);
		
		editMenu.add(prefsAction);		
		editMenu.add(encodeOptionsAction);
		editMenu.add(decodeOptionsAction);

		exitAction.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				parentWindow.dispatchEvent(new WindowEvent(parentWindow,
						WindowEvent.WINDOW_CLOSING));
			}
		});
		helpMenu.add(aboutAction);

		aboutAction.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				AboutDialog dlg = new AboutDialog();
				dlg.setVisible(true);
			}
		});

		prefsAction.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				if (preferencesDialog == null) {
					preferencesDialog = new OptionsDialog();
				}
				preferencesDialog.setVisible(true);
			}
		});
		
		decodeOptionsAction.addActionListener(new ActionListener() {
		    
            @Override
            public void actionPerformed(final ActionEvent e) {
                
                if (decodeOptionsDialog == null) {
                    decodeOptionsDialog = new DecodingOptionsDialog(
                            codingProperties, fileChooserFactory);
                }
                decodeOptionsDialog.setVisible(true);
            }
        });
		encodeOptionsAction.addActionListener(new ActionListener() {
		    
		    @Override
		    public void actionPerformed(final ActionEvent e) {
                if (encodeOptionsDialog == null) {
                    encodeOptionsDialog = new EncodingOptionsDialog(
                            codingProperties, fileChooserFactory);
                }
                encodeOptionsDialog.setVisible(true);
		    }
		});
    }
}
