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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Style;

/**
 * Bundles a KML output generation assembled from multiple content providers
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public final class KmlOutput {

    /**
     * Instance of {@link de.micromata.opengis.kml.v_2_2_0.Document} used to
     * generate the KML output file.
     */
    private final Document kmlDocument;

    /**
     * The list of registered content providers
     */
    private final List<ContentProvider<?>> contentProviders = new ArrayList<ContentProvider<?>>();

    /**
     * Instance of {@link de.micromata.opengis.kml.v_2_2_0.Kml} used to generate
     * the KML output file.
     */
    private final Kml kml;

    /**
     * The KML file to write the results to.
     */
    private final File kmlFile;

    /**
     * Creates a new instance of this class.
     * 
     * @param kFile
     *            the KML file to write the results to.
     */
    public KmlOutput(final File kFile) {

        kmlFile = kFile;
        kml = new Kml();
        kmlDocument = kml.createAndSetDocument();
        kmlDocument.setOpen(true);
        kmlDocument.withDescription("(C) 2009-11 TomTom International B.V.");
    }

    /**
     * Adds the given style definitions to the context. Content providers can
     * refer to these styles via their identifiers.
     * 
     * @param styles
     *            The style definitions to add
     */
    void addStyles(final Collection<Style> styles) {
        for (Style style : styles) {
            kmlDocument.addToStyleSelector(style);
        }
    }

    /**
     * Adds a folder to the KML output
     * 
     * @param folder
     *            The folder to add
     */
    void addFolder(final Folder folder) {
        kmlDocument.getFeature().add(folder);
    }

    /**
     * Registers a new content provider
     * 
     * @param provider
     *            The content provider to add
     */
    public void addContentProvider(final ContentProvider<?> provider) {
        contentProviders.add(provider);
    }


    /**
     * Writes the KML file to disk.
     * 
     * @return true, if marshaling the KML file was successful
     * @throws KmlGenerationException
     *             If an error occurred generation the KML from the data of one
     *             of the content providers
     * @throws IOException
     *             If an error occurred writing the KML output to the specified
     *             file
     */
    public boolean write() throws KmlGenerationException, IOException {
        if (!kmlFile.exists()) {
            kmlFile.createNewFile();
        }

        for (ContentProvider<?> contenProvider : contentProviders) {
            contenProvider.addContent(this);
        }

        return kml.marshal(kmlFile);
    }
}
