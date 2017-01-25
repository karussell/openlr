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


/**
 * This is an adaptor that provides empty default implementation of all the
 * methods of {@link MapMouseListener}.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public abstract class MapMouseAdaptor implements MapMouseListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseClicked(final MapMouseEvent ev) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseEntered(final MapMouseEvent ev) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseExited(final MapMouseEvent ev) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseMoved(final MapMouseEvent ev) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mousePressed(final MapMouseEvent ev) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseReleased(final MapMouseEvent ev) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseDragged(final MapMouseEvent ev) {
    }

}
