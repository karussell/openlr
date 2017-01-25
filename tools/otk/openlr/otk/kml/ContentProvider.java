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
**/

/**
 *  Copyright (C) 2009-12 TomTom International B.V.
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
package openlr.otk.kml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.micromata.opengis.kml.v_2_2_0.ExtendedData;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Style;

/**
 * This class defines the interface of a content provider to the process of a
 * single KML creation.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 * @param <T>
 *            The type of data object to draw with the concrete provider
 *            implementation
 */
public abstract class ContentProvider<T> {

    /**
     * The label of the feature collection
     */
    private String folderName;

    /**
     * A map of optional general data represented by key-value pairs
     */
    private final Map<String, String> generalData = new LinkedHashMap<String, String>();

    /**
     * The style definitions used to format the KML features
     */
    private Collection<Style> styles = new ArrayList<Style>();

    /**
     * The data object to draw
     */
    private final T dataObject;

    /**
     * Creates a new content writer
     * 
     * @param item
     *            The object to draw
     */
    public ContentProvider(final T item) {
        dataObject = item;
    }

    /**
     * Adds the given styles to the KML output.
     * 
     * @param stylesToAdd
     *            The styles to use when drawing the data
     */
    public final void addStyles(final Collection<Style> stylesToAdd) {
        styles.addAll(stylesToAdd);
    }

    /**
     * The method the {@link KmlOutput}ter calls back when it is actually
     * drawing all the registered content providers
     * 
     * @param kml
     *            A reference to the KML helper that provides methods for
     *            drawing simple KML base object
     * @throws KmlGenerationException
     *             If an error occurs while creating KML from the attributes of
     *             the drawn objects
     */
    final void addContent(final KmlOutput kml) throws KmlGenerationException {

        Folder folder = new Folder().withName(folderName).withOpen(false);
        // .withDescription(folderDescription);
        kml.addFolder(folder);

        kml.addStyles(styles);

        List<Feature> features = createContent(dataObject);
        for (Feature feature : features) {
            folder.addToFeature(feature);
        }

        addExtendedData(folder);
    }

    /**
     * Add the optional extended data to the folder.
     * 
     * @param folder
     *            The folder to add the data to.
     */
    private void addExtendedData(final Folder folder) {

        ExtendedData extData = new ExtendedData();     

        for (Map.Entry<String, String> entry : generalData.entrySet()) {
            extData.createAndAddData(entry.getValue())
                    .setName(entry.getKey());
        }

        folder.setExtendedData(extData);
    }

    /**
     * This method delivers a list of KML features created from the data of the
     * given object to write.
     * 
     * @param item
     *            The subject to draw
     * @return The collection of features to add to the generated KML, must
     *         never be never <code>null</code>
     * @throws KmlGenerationException
     *             If an error occurs while creating KML from the attributes of
     *             the drawn objects
     */
    public abstract List<Feature> createContent(T item)
            throws KmlGenerationException;

    /**
     * Sets a label to the parent KML feature that encloses all the content.
     * This will be visible to the user.
     * 
     * @param label
     *            The label of the collection of drawn KML features
     */
    public final void setLabel(final String label) {
        folderName = label;
    }

    /**
     * Adds a date of general information to the KML folder. This will be
     * visible to the user.
     * 
     * @param key
     *            The key of this data element
     * @param value
     *            The value of this data element
     */
    public final void addGeneralData(final String key, final String value) {
        generalData.put(key, value);
    }
}
