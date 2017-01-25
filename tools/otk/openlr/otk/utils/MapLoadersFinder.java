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
package openlr.otk.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import openlr.map.loader.OpenLRMapLoader;

/**
 * This class provided the functionality to search OpenLR map loaders in
 * different sources.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class MapLoadersFinder {

    /**
     * Identifies the specified loader in a JAR library the
     * {@code loaderLibFile} points to. <strong>This will also deliver all
     * loaders that are found in the current context class-path since this
     * class-path has to be set as the fall-back to the JARs classes.</strong>
     * 
     * @param pathToLoaderJar
     *            The loader library file, assumed to be a JAR
     * @return The found loaders
     * @throws FileNotFoundException
     *             If the specified JAR file was not found
     */
    public final List<OpenLRMapLoader> extractMapLoaders(
            final String pathToLoaderJar) throws FileNotFoundException {

        List<OpenLRMapLoader> relevantLoaders;

        if (pathToLoaderJar != null && new File(pathToLoaderJar).exists()) {
            File jarFile = new File(pathToLoaderJar);

            ClassLoader contextCL = Thread.currentThread()
                    .getContextClassLoader();
            String urlPath = "jar:file:////" + jarFile.getAbsolutePath() + "!/";
            ClassLoader urlCL;
            try {
                /*
                 * setting the current cl as the parent of this sub loader is
                 * necessary because the ServiceLoader tries to cast the found
                 * loader later to the exact class (including its loader) of
                 * OpenLRMapLoader we give him as service interface to look for
                 */
                urlCL = URLClassLoader.newInstance(
                        new URL[] {new URL(urlPath)}, contextCL);
            } catch (MalformedURLException e) {
                throw new IllegalStateException("Unexpected error", e);
            }
            Thread.currentThread().setContextClassLoader(urlCL);

            relevantLoaders = extractMapLoaders(urlCL);

        } else {
            throw new FileNotFoundException(
                    "Provided map loader file does not exist: "
                            + pathToLoaderJar);
        }
        return relevantLoaders;
    }

    /**
     * Finds loaders in the class-path of the provided or if <code>null</code>
     * in the current class-path
     * 
     * @param classloader
     *            The class-loader to check for map loader instances.
     * @return The list of found {@link OpenLRMapLoader} instances
     */
    public final List<OpenLRMapLoader> extractMapLoaders(
            final ClassLoader classloader) {

        List<OpenLRMapLoader> foundLoaders = new ArrayList<OpenLRMapLoader>();
        ServiceLoader<OpenLRMapLoader> mapLoaders;
        mapLoaders = ServiceLoader.load(OpenLRMapLoader.class, classloader);
        for (OpenLRMapLoader l : mapLoaders) {
            foundLoaders.add(l);
        }
        return foundLoaders;
    }
}
