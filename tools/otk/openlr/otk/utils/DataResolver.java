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
package openlr.otk.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.bind.JAXBException;

import openlr.binary.ByteArray;
import openlr.datex2.Datex2Location;
import openlr.datex2.OpenLRDatex2Exception;
import openlr.datex2.XmlReader;
import openlr.otk.utils.DataResolverException.DataResolverErrorCode;
import openlr.xml.OpenLRXMLException;
import openlr.xml.OpenLRXmlReader;
import openlr.xml.generated.OpenLR;

import org.xml.sax.SAXException;

/**
 * The Class DataHandler.
 */
public final class DataResolver {
	
	
	/**
	 * Utility class shall not be instantiated.
	 */
	private DataResolver() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Resolve binary data from input stream.
	 * 
	 * @param is
	 *            the input stream
	 * @param isBase64
	 *            the is base64
	 * @return the byte array
	 * @throws DataResolverException
	 *             if reading input stream failed
	 */
	public static ByteArray resolveBinaryData(final InputStream is,
			final boolean isBase64) throws DataResolverException {
		ByteArray ba = null;
		try {
            if (isBase64) {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        is, IOUtils.SYSTEM_DEFAULT_CHARSET));
				String s = br.readLine();
				ba = new ByteArray(s);
			} else {
				ba = new ByteArray(is);
			}
		} catch (IOException e) {
			throw new DataResolverException(
					DataResolverErrorCode.INVALID_INPUT_DATA, e);
		}
		return ba;
	}

	/**
	 * Resolve datex2 data from input stream.
	 * 
	 * @param is
	 *            the input stream
	 * @return the datex2 location
	 * @throws DataResolverException
	 *             if reading input stream failed
	 */
	public static Datex2Location resolveDatex2Data(final InputStream is)
			throws DataResolverException {
		Datex2Location xml = null;
		try {
			XmlReader reader = new XmlReader();
			xml = reader.readDatex2Location(is);
		} catch (OpenLRDatex2Exception e) {
			throw new DataResolverException(
					DataResolverErrorCode.INVALID_INPUT_DATA, e);
		} catch (JAXBException e) {
			throw new DataResolverException(
					DataResolverErrorCode.INVALID_INPUT_DATA, e);
		} catch (SAXException e) {
			throw new DataResolverException(
					DataResolverErrorCode.INVALID_INPUT_DATA, e);
		}
		return xml;
	}

	/**
	 * Resolve xml data from input stream.
	 * 
	 * @param is
	 *            the input stream
	 * @return the open lr
	 * @throws DataResolverException
	 *             if reading input stream failed
	 */
	public static OpenLR resolveXmlData(final InputStream is)
			throws DataResolverException {
		OpenLR xml = null;
		try {
			OpenLRXmlReader reader = new OpenLRXmlReader();
			xml = reader.readOpenLRXML(is, true);
		} catch (OpenLRXMLException e) {
			throw new DataResolverException(
					DataResolverErrorCode.INVALID_INPUT_DATA, e);
		} catch (JAXBException e) {
			throw new DataResolverException(
					DataResolverErrorCode.INVALID_INPUT_DATA, e);
		} catch (SAXException e) {
			throw new DataResolverException(
					DataResolverErrorCode.INVALID_INPUT_DATA, e);
		}
		return xml;
	}


}
