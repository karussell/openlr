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
package openlr.otk.loaderinfo;

import static openlr.otk.utils.IOUtils.LINE_SEPARATOR;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.List;

import openlr.map.loader.MapLoadParameter;
import openlr.map.loader.OpenLRMapLoader;
import openlr.otk.common.CommandLineParseException;
import openlr.otk.common.OpenLRCommandLineTool;
import openlr.otk.common.OpenLRToolkitException;
import openlr.otk.utils.IOUtils;
import openlr.otk.utils.MapLoadersFinder;

/**
 * A tool that lists information about {@link OpenLRMapLoader} implementations
 * provided via path to a JAR or searched in the class-path.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class LoaderInfo extends OpenLRCommandLineTool<LoaderInfoOptions> {

    /**
     * The tool identifier that represents the sub command inside the OTK
     * commands collection
     */
    public static final String TOOL_IDENTIFIER = "loader-info";

    /**
     * The description of the functionality of this tool.
     */
    public static final String TOOL_DESCRIPTION = "Provides information about an OpenLR map-loader implementation. "
            + "This implementation is either provided by specification of a path to a JAR archive or searched in the class-path of the current run of the OTK if the tool argument is omitted.";

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getToolIdentifier() {
        return TOOL_IDENTIFIER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void executeImpl(final LoaderInfoOptions optionsHandler)
            throws OpenLRToolkitException, CommandLineParseException {

        Writer outWriter = new OutputStreamWriter(getOutputStream());

        List<OpenLRMapLoader> loaders;

        String processedResource;
        String loaderLibraryArgument = optionsHandler
                .getSpecifiedPathToJar();
        if (loaderLibraryArgument != null) {

            processedResource = loaderLibraryArgument;
            loaders = optionsHandler.getLoaders();

        } else {
            processedResource = "classpath";
            loaders = new MapLoadersFinder().extractMapLoaders(Thread
                    .currentThread().getContextClassLoader());
        }
        try {

            outWriter.write("OpenLR map loaders found in " + processedResource
                    + ": ");
            outWriter.write(LINE_SEPARATOR);
            outWriter.write(LINE_SEPARATOR);

            for (OpenLRMapLoader openLRMapLoader : loaders) {

                printInfo(openLRMapLoader, outWriter);
                outWriter.write(LINE_SEPARATOR);
            }

        } catch (IOException e) {
            throw new OpenLRToolkitException("Error while printing the output",
                    e);

        } finally {
            IOUtils.closeQuietly(outWriter);
        }
    }

    /**
     * Prints the information about the given loader.
     * 
     * @param loader
     *            The loader to print the information about.
     * @param output
     *            The output writer to write to
     * @throws IOException
     *             If an error occurs during write
     */
    private void printInfo(final OpenLRMapLoader loader, final Writer output)
            throws IOException {

        String name = loader.getName();
        StringBuilder loaderLine = new StringBuilder(2 * name.length());
        loaderLine.append("Loader name: ").append("\"").append(name).append("\"")
                .append(LINE_SEPARATOR);

        output.write(loaderLine.toString());

        String horizontalLine = String.format("%" + loaderLine.length() + "s",
                "").replace(' ', '-');

        output.write(horizontalLine);
        output.write(LINE_SEPARATOR);
        output.write("Description: ");
        writeIfNotNull(output, loader.getDescription());
        output.write(LINE_SEPARATOR);
        String mapDescriptor = loader.getMapDescriptor();
        if (mapDescriptor != null) {
            output.write("Map descriptor: ");
            output.write(mapDescriptor);
            output.write(LINE_SEPARATOR);
        }

        Collection<MapLoadParameter> parameters = loader.getParameter();

        if (parameters.size() > 0) {

            output.write("Parameters (ID, name, description, required/optional): ");
            output.write(LINE_SEPARATOR);

            for (MapLoadParameter param : parameters) {
                output.write(String.valueOf(param.getIdentifier()));
                output.write(", \"");
                writeIfNotNull(output, param.getName());
                output.write("\", \"");
                writeIfNotNull(output, param.getDescription());
                output.write("\", ");
                if (param.isRequired()) {
                    output.write("required");
                } else {
                    output.write("optional");
                }
                output.write(LINE_SEPARATOR);
            }
        }
    }

    /**
     * Helper method that writes the given value to the writer if value is not
     * null.
     * 
     * @param output
     *            The output writer
     * @param value
     *            The value to write
     * @throws IOException
     *             If an error occurs when writing the value.
     */
    private void writeIfNotNull(final Writer output, final String value)
            throws IOException {
        if (value != null) {
            output.write(value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final LoaderInfoOptions getOptionsHandler() {
        return new LoaderInfoOptions(TOOL_IDENTIFIER, TOOL_DESCRIPTION);
    }

}
