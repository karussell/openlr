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
package openlr.geomap.tools;

import java.awt.Cursor;

import openlr.geomap.MapMouseEvent;

/**
 * The SelectTool is used for the selection of a location.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class SelectTool extends CursorTool {
	
    /**
     * The currently attached selection processor that handles the selection
     * events.
     */
    private SelectionProcessor processor;

    @Override
    public final void mouseClicked(final MapMouseEvent ev) {
        if (processor != null) {
            processor.selectionClicked(ev.getCoordinate());
        }
    }
	
	/**
	 * Gets the cursor.
	 *
	 * @return the cursor
	 */
	@Override
    public final Cursor getCursor() {
		return new Cursor(Cursor.HAND_CURSOR);
	}

    /**
     * Does nothing
     */
    @Override
    public void deactivate() {

    }
    
    /**
     * Sets and replaces the currently attached selection processor that handles
     * the selection events.
     * 
     * @param sp
     *            The new selection processor.
     */
    public final void setSelectionProcessor(final SelectionProcessor sp) {
        processor = sp;
    }    
}
