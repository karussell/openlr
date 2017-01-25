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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;

/** 
 * Provides I/O utilities.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public final class IOUtils {
    
    /** The logger. */
    private static final Logger LOG = Logger.getLogger(IOUtils.class);
    
    /**
     * The system default character set
     */
    public static final Charset SYSTEM_DEFAULT_CHARSET = Charset.defaultCharset();
    
    /**
     * The system dependent line separator
     */
    public static final String LINE_SEPARATOR = System.getProperty(
            "line.separator", "\n");    

    /**
     * Disabled constructor.
     */
    private IOUtils() { 
    }
    
    /**
     * Closes an output stream and swallows {@code null} or exceptions. Please
     * be aware of the fact that this can lead to undetected data loss if the
     * stream buffer is not flushed before. It is recommended to close the
     * writer explicitly before via {@link Writer#close()} in the client code. 
     * 
     * @param out
     *            The output stream to close.
     */
    public static void closeQuietly(final OutputStream out) {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.error(e);
                }
            }
        }
    }
    
    /**
     * Closes a writer and swallows {@code null} or exceptions. Please
     * be aware of the fact that this can lead to undetected data loss if the
     * writer's buffer is not flushed before. It is recommended to close the
     * writer explicitly before via {@link Writer#close()} in the client code. 
     * 
     * @param writer
     *            The output writer to close.
     */
    public static void closeQuietly(final Writer writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.error(e);
                }
            }
        }
    }
    
    /**
     * Closes an input stream and swallows {@code null} or exceptions. 
     * 
     * @param in
     *            The input stream to close.
     */
    public static void closeQuietly(final InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.error(e);
                }
            }
        }
    }
    
    /**
     * Closes a reader and swallows {@code null} or exceptions. 
     * 
     * @param reader
     *            The reader to close.
     */
    public static void closeQuietly(final Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.error(e);
                }
            }
        }
    }
    
}
