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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;

/**
 * This factory creates {@link JFileChooser}s and stores their last selected
 * directory over runtime of the application. Another request for a
 * {@link JFileChooser} with the same topic will create that starts in the
 * stored directory.
 * 
 * There are some common topics defined in fields of interface
 * {@link FileChooserFactory}, but it is valid to define a new topic in a call
 * to {@link #createFileChooser(String)}. Value <code>null</code> will always
 * create JFileChooser in the default directory of JFileChooser.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class DirectoryRememberingFileChooserFactory implements FileChooserFactory {

    /**
     * The internal storage for detected topics and their last directories.
     */
    private final Map<String, File> cachedDirs = new HashMap<String, File>();

    /**
     * Creates a new {@link JFileChooser} that starts in the directory the last
     * chooser of this topic was closed in. <br>
     * 
     * There are some common topics defined in fields of this interface, but it
     * is valid to define a new topic. Value <code>null</code> will always
     * create JFileChooser in the default directory of JFileChooser.
     * 
     * @param topic
     *            The topic this chooser deals with. The last directory of the
     *            JFileChooser will be stored and will provide the start
     *            directory for the next chooser requested for this topic.
     * @return A new instance of {@link JFileChooser}.
     */
    @Override
    public final JFileChooser createFileChooser(final String topic) {
        File dir = cachedDirs.get(topic);

        JFileChooser result = new JFileChooser(dir);

        result.addActionListener(new ActionListener() {

            /**
             * Stores the selected directory after leaving the file chooser with
             * submit or cancel.
             * @param e The action event.
             */
            @Override
            public void actionPerformed(final ActionEvent e) {

                if (topic != null) {
                    synchronized (cachedDirs) {
                        cachedDirs.put(topic, ((JFileChooser) e
                                .getSource()).getCurrentDirectory());
                    }
                }
            }
        });
        return result;
    }
}
