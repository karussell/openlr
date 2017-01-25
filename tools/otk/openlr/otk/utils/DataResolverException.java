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

import openlr.OpenLRProcessingException;

/**
 * The Class DataHandlerException.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class DataResolverException extends OpenLRProcessingException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2664040149306087966L;

	/**
	 * The Enum DataHandlerErrorCode.
	 */
	public enum DataResolverErrorCode implements ErrorCode {
		
		/** The INVALI d_ inpu t_ data. */
		INVALID_INPUT_DATA("input data is invalid");
		
		/** The explanation. */
		private final String explanation;
		
		/**
		 * Instantiates a new converter error code.
		 *
		 * @param s the s
		 */
		DataResolverErrorCode(final String s) {
			explanation = s;
		}		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getExplanation() {
			return explanation;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getName() {
			return name();
		}
		
	}
	
	/**
	 * Instantiates a new phys format converter exception.
	 *
	 * @param err the err
	 */
	public DataResolverException(final ErrorCode err) {
		super(err);
	}
	
	/**
	 * Instantiates a new phys format converter exception.
	 *
	 * @param err the err
	 * @param t the t
	 */
	public DataResolverException(final ErrorCode err, final Throwable t) {
		super(err, t);
	}
	
	/**
	 * Instantiates a new phys format converter exception.
	 *
	 * @param err the err
	 * @param s the s
	 */
	public DataResolverException(final ErrorCode err, final String s) {
		super(err, s);
	}

}
