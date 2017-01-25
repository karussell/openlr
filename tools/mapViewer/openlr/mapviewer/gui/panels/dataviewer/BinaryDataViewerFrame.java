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
package openlr.mapviewer.gui.panels.dataviewer;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;
import openlr.mapviewer.gui.MapViewerGui;

/**
 * The Class BinaryDataViewerFrame shows the output of the binary data viewer.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class BinaryDataViewerFrame extends JFrame {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = -5055903756886540749L;

	/** The Constant OUTPUT_ROWS. */
	private static final int OUTPUT_ROWS = 40;

	/** The Constant OUTPUT_COLUMNS. */
	private static final int OUTPUT_COLUMNS = 50;
	
	/**
	 * Instantiates a new binary data viewer frame.
	 *
	 * @param result the result
	 */
	public BinaryDataViewerFrame(final String result) {
		JTextArea textArea = new JTextArea(OUTPUT_ROWS, OUTPUT_COLUMNS);
		textArea.setText(result);
		textArea.setEditable(false);
		textArea.setCaretPosition(0);
		JScrollPane scroll = new JScrollPane(textArea);
		setTitle("Human-readable version of the binary location reference");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setLayout(new MigLayout("insets 3"));
		add(scroll);
		setIconImage(MapViewerGui.OPENLR_ICON);
		pack();
	}
}
