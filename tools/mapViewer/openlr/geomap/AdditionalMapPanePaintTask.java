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
package openlr.geomap;

import java.awt.Dimension;
import java.awt.Graphics2D;


/**
 * This interface describes objects that can be attached to the
 * {@link JMapPane} for post processing the {@link Graphics2D} object of the
 * map drawing. Method {@link #paintComponent(java.awt.Graphics, Dimension)} is called after
 * drawing the map image.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public interface AdditionalMapPanePaintTask {

    /**
     * This method is called on a registered {@link AdditionalMapPanePaintTask}
     * at the end of painting the map graphics. The provided Graphics object 
     * provides the Geomap coordinate space where the coordinate source (0,0) 
     * is in the lower left! 
     * 
     * @param g
     *            The {@link Graphics2D} object underlying the map drawing.
     * @param screenSize
     *            The dimension of the map graphics.
     */
    void paintComponent(Graphics2D g, Dimension screenSize);
    

    /**
     * {@inheritDoc}
     */
    @Override 
    int hashCode();

    /**
     * {@inheritDoc}
     */
    @Override 
    boolean equals(Object obj);
}
