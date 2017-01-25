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
package openlr.mapviewer;

/**
 * The MapViewerException will be thrown if the execution of the map viewer failed.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class MapViewerException extends Exception {
	
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 8320745787142658228L;

	/**
	 * Instantiates a new map viewer exception due to a previously thrown exception.
	 * 
	 * @param t the previously thrown exception
	 */
	public MapViewerException(final Throwable t) {
		super(t);
	}

	/**
	 * Instantiates a new map viewer exception.
	 *
	 * @param msg the msg
	 */
	public MapViewerException(final String msg) {
		super(msg);
	}
	
    /**
     * Instantiates a new map viewer exception.
     *
     * @param msg the msg
     * @param cause the previously thrown exception
     */
    public MapViewerException(final String msg, final Throwable cause) {
        super(msg, cause);
    }	

}
