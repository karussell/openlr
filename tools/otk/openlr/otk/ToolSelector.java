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
package openlr.otk;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import openlr.otk.binview.BinaryDataViewer;
import openlr.otk.coding.Decode;
import openlr.otk.coding.Encode;
import openlr.otk.common.OpenLRCommandLineTool;
import openlr.otk.common.OpenLRToolkitException;
import openlr.otk.converter.PhysFormatConverter;
import openlr.otk.loaderinfo.LoaderInfo;
import openlr.otk.locrefboundary.LocRefBoundaryTool;
import openlr.otk.xml2kml.Xml2Kml;

import org.apache.log4j.Logger;

/**
 * Provides access to the available tool commands.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
class ToolSelector {

    /**
     * The logger
     */
    private static final Logger LOG = Logger.getLogger(ToolSelector.class);

    /**
     * The name of a properties file listing additional command implementations.
     * It is expected to contain the command as key with the fully qualified
     * name of the class as value
     */
    private static final String ADDITIONAL_TOOLS_PROPERTIES = "additional-tools.properties";
    /**
     * A ordered map of known command identifiers and their classes
     */
    private static final Map<String, Class<? extends OpenLRCommandLineTool<?>>> COMMANDS 
    = new LinkedHashMap<String, Class<? extends OpenLRCommandLineTool<?>>>();

    static {
        COMMANDS.put(Encode.TOOL_IDENTIFIER, Encode.class);
        COMMANDS.put(Decode.TOOL_IDENTIFIER, Decode.class);
        COMMANDS.put(PhysFormatConverter.TOOL_IDENTIFIER,
                PhysFormatConverter.class);
        COMMANDS.put(Xml2Kml.TOOL_IDENTIFIER, Xml2Kml.class);
        COMMANDS.put(BinaryDataViewer.TOOL_IDENTIFIER, BinaryDataViewer.class);
        COMMANDS.put(LocRefBoundaryTool.TOOL_IDENTIFIER,
                LocRefBoundaryTool.class);
        COMMANDS.put(LoaderInfo.TOOL_IDENTIFIER, LoaderInfo.class);
        try {
            searchAdditionalTools(COMMANDS);
        } catch (IOException e) {
            LOG.error(
                    "Error searching for additional command implementations in the classpath",
                    e);
        } catch (ClassNotFoundException e) {
            LOG.error(
                    "Error searching for additional command implementations in the classpath",
                    e);
        }
    }

    /**
     * Searches for additional commands available in the class path
     * 
     * @param targetMap
     *            The map to add the additional tools to
     * @throws IOException
     *             If an error occurs reading the properties file
     * @throws ClassNotFoundException
     *             If a configured class cannot be found
     */
    private static void searchAdditionalTools(
            final Map<String, Class<? extends OpenLRCommandLineTool<?>>> targetMap) throws IOException,
            ClassNotFoundException {

        InputStream additionalTools = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(ADDITIONAL_TOOLS_PROPERTIES);
        if (additionalTools != null) {
            Properties props = new Properties();
            props.load(additionalTools);

            for (Map.Entry<Object, Object> entry : props.entrySet()) {

                String command = entry.getKey().toString().trim();
                String className = entry.getValue().toString().trim();
                Class<?> clazz = Class.forName(className);

                if (OpenLRCommandLineTool.class.isAssignableFrom(clazz)) {
                    @SuppressWarnings("unchecked")
                    Class<? extends OpenLRCommandLineTool<?>> toolClass = (Class<? extends OpenLRCommandLineTool<?>>) clazz;
                    targetMap.put(command, toolClass);
                } else {
                    throw new IllegalStateException(
                            "Additional OTK tool in properties that does not extend class OpenLRCommandLineTool: "
                                    + className);
                }
            }
        }
    }

    /**
     * Delivers the command identified by the given command string.
     * 
     * @param command
     *            The command identifier
     * @return The related tool
     * @throws OpenLRToolkitException
     *             If no tool could be determined for the given command
     */
    public OpenLRCommandLineTool<?> getToolForCommand(final String command)
            throws OpenLRToolkitException {

        Class<? extends OpenLRCommandLineTool<?>> toolClass = COMMANDS.get(command);

        if (toolClass == null) {
            throw new OpenLRToolkitException("Unknown command: \"" + command
                    + "\".");
        }

        try {

            OpenLRCommandLineTool<?> instance = toolClass.newInstance();

            return instance;

        } catch (InstantiationException e) {
            throw new OpenLRToolkitException("Error starting command", e);
        } catch (IllegalAccessException e) {
            throw new OpenLRToolkitException("Error starting command", e);
        }
    }

    /**
     * Delivers all possible commands.
     * 
     * @return A list of all available commands
     */
    public List<String> getAllCommands() {

        return new ArrayList<String>(COMMANDS.keySet());
    }   
}
