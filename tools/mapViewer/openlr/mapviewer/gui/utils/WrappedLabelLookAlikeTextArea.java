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
package openlr.mapviewer.gui.utils;

import javax.swing.JTextArea;

/**
 * This class extends {@link JTextArea} to create a text area looking like a
 * {@link javax.swing.JLabel JLabel} but providing word wrap. Using this instead
 * of a {@link javax.swing.JLabel JLabel} then makes the text area aware of very
 * long values.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class WrappedLabelLookAlikeTextArea extends JTextArea {

    /**
     * Default serial ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new empty TextArea with the specified number of rows and
     * columns. A default model is created, and the initial string is null.
     * 
     * @param rows
     *            the number of rows >= 0
     * @param columns
     *            the number of columns >= 0
     */
    public WrappedLabelLookAlikeTextArea(final int rows, final int columns) {
        super(rows, columns);
        setWrapStyleWord(true);
        setEditable(false);
        setLineWrap(true);
        setOpaque(false);
    }
}
