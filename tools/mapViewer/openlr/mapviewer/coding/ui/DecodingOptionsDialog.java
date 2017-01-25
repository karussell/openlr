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

import javax.swing.JTextField;

import openlr.mapviewer.coding.CodingPropertiesHolder;
import openlr.mapviewer.gui.filechoose.FileChooserFactory;

/**
 * This is that dialog that provides the possibility to adapt the OpenLR decoder
 * properties.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class DecodingOptionsDialog extends AbstractCodingOptionsDialog {

    /**
     * Default serial ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * The property key of the property for calculating the covered lines during
     * decoding
     */
    private static final String COVERED_LINES_PROPERTY = "Calc_Affected_Lines";

    /**
     * The text of the additional hint for the covered lines property
     */
    private static final String ADDITIONAL_HINT_COVERED_LINES = "This property affects only the decoding process.<br>"
            + "Covered lines are calculated during decoding or not. <br>"
            + "The visualization of the covered lines of an area location<br>"
            + "in the MapViewer is not controlled by it.";

    /**
     * @param propsHolder
     *            The properties holder
     * @param fcf
     *            The file choose factory to use
     */
    public DecodingOptionsDialog(final CodingPropertiesHolder propsHolder,
            final FileChooserFactory fcf) {
        super(CodingPropertiesHolder.CodingType.DECODING, propsHolder, fcf,
                "Decoding properties");
    }

    /**
     * Adds an additional hint to the tool tip of the input field of the covered
     * lines property
     * 
     * @param propKey
     *            {@inheritDoc}
     * @param field
     *            {@inheritDoc}
     */
    @Override
    protected final void afterInputFieldCreation(final String propKey,
            final JTextField field) {

        if (COVERED_LINES_PROPERTY.equals(propKey)) {
            String initialToolTip = field.getToolTipText();

            StringBuilder newToolTip = new StringBuilder(
                    2 * ADDITIONAL_HINT_COVERED_LINES.length());
   
            newToolTip.append("<html>").append(initialToolTip);
            newToolTip.append(".<br>").append(ADDITIONAL_HINT_COVERED_LINES);
            newToolTip.append("</hml>");

            field.setToolTipText(newToolTip.toString());
        }
    }

}
