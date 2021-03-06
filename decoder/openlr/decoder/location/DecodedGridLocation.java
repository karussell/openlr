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
package openlr.decoder.location;

import openlr.location.data.AffectedLines;
import openlr.map.GeoCoordinates;
import openlr.map.InvalidMapDataException;

/**
 * Implementation of the location interface for grid locations.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author DLR e.V. (RE)
 */
public class DecodedGridLocation extends openlr.location.GridLocation {

	/** The list of covered lines. */
	private final AffectedLines affectedLines;

	/**
	 * Instantiates a new decoded location.
	 * 
	 * @param idValue
	 *            the id value
	 * @param affected
	 *            the affected
	 * @param ncols
	 *            the number of columns
	 * @param nrows
	 *            the number of rows
	 * @param ll
	 *            the ll
	 * @param ur
	 *            the ur
	 * @throws InvalidMapDataException
	 *             the invalid map data exception
	 */
	public DecodedGridLocation(final String idValue,
			final AffectedLines affected, final int ncols, final int nrows,
			final GeoCoordinates ll, final GeoCoordinates ur)
			throws InvalidMapDataException {
		super(idValue, ll.getLongitudeDeg(), ll.getLatitudeDeg(), ur
				.getLongitudeDeg(), ur.getLatitudeDeg(), ncols, nrows);
		affectedLines = affected;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final AffectedLines getAffectedLines() {
		return affectedLines;
	}
}
