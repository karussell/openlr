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
package openlr.mapviewer.gui;

import java.util.ArrayList;
import java.util.List;

/**
 * The shut down manager provides the possibility to features in the map viewer
 * application to define tasks that shall be executed on application shut down.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public final class ShutDownManager {

    /**
     * The list of registered shut down actions
     */
    private final List<ShutDownAction> shutDownActions = new ArrayList<ShutDownAction>();

    /**
     * The single instance of the shutdown manager
     */
    private static final ShutDownManager INSTANCE = new ShutDownManager();

    /**
     * The hidden constructor
     */
    private ShutDownManager() {
        // disabled constructor
    }

    /**
     * Delivers the single instance of the shutdown manager.
     * 
     * @return The single instance
     */
    public static ShutDownManager getInstance() {
        return INSTANCE;
    }

    /**
     * Adds a shut down action.
     * 
     * @param action
     *            The action to add
     */
    public void addShutDownAction(final ShutDownAction action) {
        shutDownActions.add(action);
    }

    /**
     * This interface defines a shut down action that can be registered to the
     * {@link ShutDownManageroild}.
     */
    public interface ShutDownAction {

        /**
         * This method is called on application shut down.
         */
        void shutDown();
    }

    /**
     * Executes all registered shut down actions
     */
    void statShutDown() {

        for (ShutDownAction action : shutDownActions) {
            action.shutDown();
        }
    }

}
