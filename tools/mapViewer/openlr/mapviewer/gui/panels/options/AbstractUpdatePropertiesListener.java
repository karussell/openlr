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

import java.util.EventObject;

import javax.swing.InputVerifier;
import javax.swing.text.JTextComponent;

import openlr.mapviewer.MapViewer;

/** 
 * This class provides base implementations for listeners for {@link JTextComponent}s
 * that update a user specific property if an input event of the component occurs.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
class AbstractUpdatePropertiesListener {
    
    /**
     * The key of the property to update at focus lost.
     */
    private final String property;
    
    /**
     * An optional verifier for the input.
     */
    private InputVerifier verifier; 
    
    /**
     * Creates a new instance.
     * @param propertyKey The key of the property to update at focus lost.
     */
    AbstractUpdatePropertiesListener(final String propertyKey) {
        property = propertyKey;
    }
    
    /**
     * Sets an input verifier that checks the value before changing the
     * corresponding property.
     * 
     * @param inputVerifier
     *            The input verifier.
     */
    public void setInputVerifier(final InputVerifier inputVerifier) {
        verifier = inputVerifier;
    }       

    /**
     * Updates the specified property with the value of the source component of
     * the event. The source is expected to be an instance of
     * {@link JTextComponent}. If the value equals the empty string "" the user
     * property is removed.
     * 
     * @param e
     *            The event object.
     */    
    void updatePropertiesActionPerformed(final EventObject e) {
        String value = ((JTextComponent) e.getSource()).getText();

        boolean proceed = true;
        if (verifier != null) {
            proceed = verifier.shouldYieldFocus((JTextComponent) e.getSource());
        }

        if (proceed) {
            String valueBefore = MapViewer.PROPERTIES.getProperty(property);
            if (value.length() > 0) {
                if (!value.equals(valueBefore)) {
                    MapViewer.PROPERTIES.setProperty(property, value);
                }
            } else {
                if (valueBefore != null) {
                    MapViewer.PROPERTIES.removeUserProperty(property);
                }
            }
        }
    }    
}
