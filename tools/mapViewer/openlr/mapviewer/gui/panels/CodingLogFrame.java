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


import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;
import openlr.mapviewer.gui.MapViewerGui;

/**
 * The Class CodingLogFrame.
 */
public class CodingLogFrame extends JFrame {
	
    /**
     * The maximum number of characters that are displayed in the window title
     * as a preview of the content in the window
     */
    private static final int MAX_NUMBER_PREVIEW_CHARS = 24;
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 3045911910736190497L;

	/** The Constant OUTPUT_ROWS. */
	private static final int OUTPUT_ROWS = 40;

	/** The Constant OUTPUT_COLUMNS. */
	private static final int OUTPUT_COLUMNS = 90;
	
	/**
	 * Instantiates a new coding log frame.
	 * @param title the the window title text
	 * @param text the text
	 */
	public CodingLogFrame(final String title, final String text) {
		setIconImage(MapViewerGui.OPENLR_ICON);		
		JScrollPane panel = new JScrollPane();
		JTextArea textArea = new JTextArea(OUTPUT_ROWS, OUTPUT_COLUMNS);
		textArea.setText(text);
		textArea.setEditable(false);
		textArea.setCaretPosition(0);
		panel.setViewportView(textArea);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setLayout(new MigLayout("insets 3"));
		add(panel);
		String title2 = buildTitle(title, text);
        setTitle(title2);
		pack();
		setLocationByPlatform(true);
	}
	
    /**
     * Builds the window title. It will contain the given title text followed by
     * a preview snippet of the log text
     * 
     * @param titleText
     *            The title text
     * @param logText
     *            The log text this window displays
     * @return The assembled window title
     */
    private String buildTitle(final String titleText, final String logText) {

        int snippetLength = Math
                .min(MAX_NUMBER_PREVIEW_CHARS, logText.length());
        String previewSnippet = logText.substring(0, snippetLength);

        StringBuilder newTtitle = new StringBuilder(titleText.length()
                + MAX_NUMBER_PREVIEW_CHARS + 10);
        newTtitle.append(titleText);
        newTtitle.append(" \"");
        newTtitle.append(previewSnippet);
        newTtitle.append("...");
        newTtitle.append("\"");
        return newTtitle.toString();
    }

}
