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

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import openlr.mapviewer.gui.MapViewerGui;

/**
 * The AboutDialog showing the version of the MapViewer and copyright information.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class AboutDialog extends JDialog implements ActionListener {
	
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 6240260833395503282L;
	
	/** The Constant FONT_SIZE. */
	private static final int FONT_SIZE = 24;
	
	/** The ok btn. */
	private static final JButton OK_BTN = new JButton("OK");
	
	/**
	 * Instantiates a new about dialog.
	 */
	public AboutDialog() {
		super();
		JPanel panel = new JPanel(new MigLayout("insets 8", "[][grow, center][]", "[][][][]"));
		setContentPane(panel);
		setTitle("About: OpenLR Map Viewer");
		JLabel copyright = new JLabel("(c)2012, TomTom International B.V.");
		JLabel info = new JLabel("OpenLR Map Viewer, version 1.4.0");
		info.setFont(info.getFont().deriveFont(Font.BOLD, FONT_SIZE));
		JLabel logoPanel = new JLabel();
		Icon logo = new ImageIcon(AboutDialog.class.getResource("/png/logo.png"));
		logoPanel.setIcon(logo);
		add(logoPanel, "span 1 4");
		add(new JPanel(), "grow");
		add(OK_BTN, "span 1 4, wrap");
		add(info, "wrap");
		add(copyright, "wrap");
		OK_BTN.addActionListener(this);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setLocationRelativeTo(null);
		pack();
		setModal(true);
		setIconImage(MapViewerGui.OPENLR_ICON);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void actionPerformed(final ActionEvent e) {
		//close window
		setVisible(false);		
	}

}
