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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** 
 * This class implements an {@link ActionListener} for {@link JTextComponent}s
 * that updates a user specific property when 
 * {@link #actionPerformed()} is fired.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
class UpdatePropertyOnActionPerformedListener extends
        AbstractUpdatePropertiesListener implements ActionListener {
	
	/**
	 * Creates a new instance.
	 * @param propertyKey The key of the property to update when action performed.
	 */
	UpdatePropertyOnActionPerformedListener(final String propertyKey) {
		super(propertyKey);
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        updatePropertiesActionPerformed(e);
    }

}
