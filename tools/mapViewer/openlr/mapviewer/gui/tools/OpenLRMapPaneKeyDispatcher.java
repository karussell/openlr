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
package openlr.mapviewer.gui.tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.KeyEventDispatcher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import openlr.mapviewer.utils.FeatureInfo;

/**
 * The Class OpenLRMapPaneKeyDispatcher.
 */
public class OpenLRMapPaneKeyDispatcher implements KeyEventDispatcher {

	/**
	 * The initial dimension of the dialog that displays the line info
	 */
	private static final Dimension DIALOG_DIM = new Dimension(250, 200);

	/** The mouse. */
	private final MouseLineInfo mouse;

	/**
	 * The frame that is the parent of the info dialogs opened by this
	 * dispatcher
	 */
	private final Frame parentFrame;

	/**
	 * Instantiates a new open lr map pane key dispatcher.
	 * 
	 * @param m
	 *            the m
	 * @param parent
	 *            The frame that shall be the parent of the dialogs openend by
	 *            this dispatcher
	 */
	public OpenLRMapPaneKeyDispatcher(final MouseLineInfo m, final Frame parent) {
		mouse = m;
		parentFrame = parent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean dispatchKeyEvent(final KeyEvent e) {
		if (e.getID() == KeyEvent.KEY_TYPED && e.getKeyChar() == 'i'
				&& mouse.getCurrentFocus() != null) {
			String infoText = FeatureInfo.createLineInfo(
					mouse.getCurrentFocus(), false);
			openDialog(infoText);
		}
		return false;
	}

	/**
	 * Opens a new dialog that displays the given line information text.
	 * 
	 * @param infoText
	 *            The text to print
	 */
	private void openDialog(final String infoText) {

		final JDialog dialog = new JDialog(parentFrame);
		dialog.setTitle("Line information");
		dialog.setLocationRelativeTo(parentFrame);
		dialog.setMinimumSize(DIALOG_DIM);
		dialog.setLayout(new BorderLayout());
		JTextArea text = new JTextArea();

		text.setBorder(BorderFactory.createLoweredBevelBorder());
		text.setText(infoText);
		text.setEditable(false);
		dialog.add(text, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new BorderLayout());

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				dialog.dispose();
			}
		});

		buttonPanel.add(okButton, BorderLayout.EAST);
		dialog.add(buttonPanel, BorderLayout.SOUTH);

		dialog.setVisible(true);
	}

}
