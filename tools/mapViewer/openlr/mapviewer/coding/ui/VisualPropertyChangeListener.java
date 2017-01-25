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

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * This document listener implementation visualizes the change in a text field
 * against a default value.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
final class VisualPropertyChangeListener implements DocumentListener {

    /**
     * The background color of a text field whose value differs from the
     * standard value
     */
    static final Color COLOR_FIELD_VALUE_DIFF_DEFAULT = Color.YELLOW;

    /**
     * The standard background color of a {@link JTextField}.
     */
    private static final Color STANDARD_TEXTFIELD_BG = new JTextField()
            .getBackground();

    /**
     * The border that indicates a changed value on a text field
     */
    private static final Border BORDER_CHANGED_VALUE = BorderFactory
            .createLineBorder(Color.red);

    /**
     * The field containing the value
     */
    private final JTextField field;

    /**
     * The default values to compare to
     */
    private final String defaultVal;

    /**
     * Creates a new instance
     * 
     * @param valueField
     *            The field containing the value
     * @param defaultValue
     *            The default values to compare to
     */
    VisualPropertyChangeListener(final JTextField valueField,
            final String defaultValue) {
        this.field = valueField;
        this.defaultVal = defaultValue;
    }

    @Override
    public void removeUpdate(final DocumentEvent e) {
        checkDiff(field, defaultVal);
    }

    @Override
    public void insertUpdate(final DocumentEvent e) {
        checkDiff(field, defaultVal);
    }

    @Override
    public void changedUpdate(final DocumentEvent e) {
        checkDiff(field, defaultVal);

    }

    /**
     * Checks if the current value in the text field is a different than the
     * default value. Highlights the text field in case of a difference and
     * highlights in every case to indicate tracked changes.
     * 
     * @param valueField
     *            The text field providing the value
     * @param defaultValue
     *            The default value to check inputs against
     */
    private void checkDiff(final JTextField valueField,
            final String defaultValue) {
        if (!valueField.getText().equals(defaultValue)) {
            valueField.setBackground(COLOR_FIELD_VALUE_DIFF_DEFAULT);
        } else {
            valueField.setBackground(STANDARD_TEXTFIELD_BG);
        }

        valueField.setBorder(BORDER_CHANGED_VALUE);

    }
}
