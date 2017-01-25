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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;

import openlr.mapviewer.coding.CodingPropertiesHolder;
import openlr.mapviewer.coding.CodingPropertiesHolder.CodingType;

import org.apache.commons.configuration.FileConfiguration;

/**
 * This class implements the action listener that executes the action to apply
 * all changes currently specified in the input fields of the form.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
final class ApplyChangesListener implements ActionListener {

    /**
     * A map of all property keys to the corresponding input fields.
     */
    private final Map<String, JTextComponent> optionsTextFields;

    /**
     * 
     */
    private final CodingType type;
    /**
     * 
     */
    private final CodingPropertiesHolder propsHolder;

    /**
     * An instance of the default text box border
     */
    private static final Border STANDARD_TEXT_BOX_BORDER = new JTextField()
            .getBorder();

    /**
     * Creates a new listener instance
     * 
     * @param codingType
     *            The type of coding this dialog instance is dealing with
     * @param propertiesHolder
     *            The properties holder
     * @param propertyToTextFieldMap
     *            A map of all property keys to the corresponding input fields.
     */
    ApplyChangesListener(final CodingType codingType,
            final CodingPropertiesHolder propertiesHolder,
            final Map<String, JTextComponent> propertyToTextFieldMap) {
        this.optionsTextFields = propertyToTextFieldMap;
        this.type = codingType;
        this.propsHolder = propertiesHolder;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        applyChanges();
    }

    /**
     * Applies the current values of the properties form to the properties
     * holder of the current application runtime.
     */
    void applyChanges() {
        FileConfiguration config = propsHolder.getProperties(type);

        for (Map.Entry<String, JTextComponent> entry : optionsTextFields
                .entrySet()) {

            JTextComponent textComponent = entry.getValue();
            String text = textComponent.getText();
            config.setProperty(entry.getKey(), text);
            propsHolder.setProperties(type, config);

            textComponent.setBorder(STANDARD_TEXT_BOX_BORDER);
        }
    }
}
