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
package openlr.mapviewer.gui.filechoose;

import javax.swing.JFileChooser;

/**
 * This factory creates {@link JFileChooser}s. It is able to cache them or 
 * initialize them dependent on a specific 'topic'. 
 * 
 * There are some common topics defined in fields of this class, but it is valid
 * to define a new topic in a call to {@link #createFileChooser(String)}.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public interface FileChooserFactory {

    /**
     * A well know topic that defines everything dealing with locations.
     */
    String TOPIC_LOCATION = "TOPIC_LOCATION";

    /**
     * A well know topic that defines everything dealing with location
     * references.
     */
    String TOPIC_LOCATION_REF = "TOPIC_LOCATION_REF";

    /**
     * Creates a new {@link JFileChooser} with respect to the given topic ID.<br>
     * <strong>Implementations should always really create a new {@link JFileChooser} 
     * instance instead of returning a cached one since the returned object 
     * can be further customized by the caller via the several setters!</strong>
     * Another caller of the same topic may not be interested in an file chooser
     * object of such unknown state.
     * 
     * There are some common topics defined in fields of this interface, but it
     * is valid to define a new topic ID.
     * 
     * @param topic
     *            The topic this chooser deals with. 
     * @return An instance of {@link JFileChooser}.
     */
    JFileChooser createFileChooser(String topic);
}
