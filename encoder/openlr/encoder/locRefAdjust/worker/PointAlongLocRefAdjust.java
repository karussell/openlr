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
package openlr.encoder.locRefAdjust.worker;

import openlr.OpenLRProcessingException;
import openlr.encoder.OpenLREncoderProcessingException;
import openlr.encoder.OpenLREncoderProcessingException.EncoderProcessingError;
import openlr.encoder.data.LocRefData;
import openlr.encoder.locRefAdjust.LocationReferenceAdjust;
import openlr.encoder.properties.OpenLREncoderProperties;

import org.apache.log4j.Logger;

/**
 * 
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class PointAlongLocRefAdjust extends LocationReferenceAdjust {
	
	
	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(PointAlongLocRefAdjust.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void adjustLocationReference(final OpenLREncoderProperties properties,
			final LocRefData locRefData) throws OpenLRProcessingException {
		
		checkMaxDistances(properties, locRefData);

		/* check if the location reference path covers the location completely */
		/* last check of the data!! */
		if (!checkLRPCoverage(locRefData)) {
			throw new OpenLREncoderProcessingException(
					EncoderProcessingError.LOCATION_REFERENCE_DOES_NOT_COVER_LOCATION);
		}

		locRefData.setLocRefPoints(checkAndAdjustOffsets(
				locRefData, properties));

		if (locRefData.getLocRefPoints().size() != 2) {
			locRefData.setLocRefPoints(fit4PointLocation(locRefData, properties));
			if (locRefData.getLocRefPoints().isEmpty()) {
				throw new OpenLREncoderProcessingException(
						EncoderProcessingError.INVALID_POINT_LOCATION_LRP);
			}
		}

		// check if all LRP are on valid nodes
		if (LOG.isDebugEnabled()) {
			checkNodeValidity(locRefData.getLocRefPoints());
		}
	}

}
