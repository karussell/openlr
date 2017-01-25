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
package openlr.mapviewer.gui.panels.options;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import net.miginfocom.swing.MigLayout;
import openlr.mapviewer.gui.MapViewerGui;


/** 
 * Creates the options dialog providing several (at the moment one) tabs to set 
 * preferences for the MapViewer application.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class OptionsDialog extends JFrame {
	
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = -1L;
	
	/**
	 * Creates a new instance of OptionsDialog.
	 */
	public OptionsDialog() {
		
		super();
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		setTitle("MapViewer preferences");
        setResizable(false);
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);   
        tabbedPane.addTab("Map appearance", null, new MapDrawingPanel() , "Preferences for drawing the map");			
        tabbedPane.addTab("General", null, new GeneralPropertiesPanel() , "General preferences");			

        JPanel panel = new JPanel(new MigLayout("insets 0", "[grow]", "[][]"));

		setContentPane(panel);
		add(tabbedPane, "wrap");
		
		JButton closeBtn = new JButton("Close");
		final OptionsDialog theDialog = this;
		closeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				theDialog.setVisible(false);
			}
		});	
		
		add(closeBtn);
		setIconImage(MapViewerGui.OPENLR_ICON);
		
		setLocationRelativeTo(null);
		pack();
	}
}
