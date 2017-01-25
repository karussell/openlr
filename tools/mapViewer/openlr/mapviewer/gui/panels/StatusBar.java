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

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

/**
 * The StatusBar shows actual information about the mouse position and status of the
 * gui.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class StatusBar extends JPanel {

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 8013317370893939302L;

	/** The message label. */
	private final JLabel messageLabel;

	/** The info label. */
	private final JLabel infoLabel;

	/**
	 * Creates a new status bar for the map viewer gui.
	 * 
	 */
	public StatusBar() {
		super(new MigLayout("insets 2", "[grow][][][]"));
		messageLabel = new JLabel();
		infoLabel = new JLabel("(c)2012, TomTom International B.V.");
		add(messageLabel, "grow");
		add(new JLabel("|"));
		add(infoLabel);
		clear();
	}

	/**
	 * Sets the message field.
	 * 
	 * @param message
	 *            the message
	 */
	public final void setMessage(final String message) {
		messageLabel.setText(message);
	}

	/**
	 * Clears the message field.
	 */
	public final void clear() {
		setMessage("");
	}

}
