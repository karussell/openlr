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
package openlr.binary.decoder;

import java.util.ArrayList;
import java.util.List;

import openlr.LocationReferencePoint;
import openlr.Offsets;
import openlr.binary.OpenLRBinaryConstants;
import openlr.binary.OpenLRBinaryException;
import openlr.binary.bitstream.impl.ByteArrayBitstreamInput;
import openlr.binary.data.FirstLRP;
import openlr.binary.data.IntermediateLRP;
import openlr.binary.data.LastLRP;
import openlr.binary.data.Offset;
import openlr.binary.data.RawBinaryData;
import openlr.binary.impl.OffsetsBinaryImpl;
import openlr.rawLocRef.RawLineLocRef;
import openlr.rawLocRef.RawLocationReference;

/**
 * The decoder for the line location type.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class LineDecoder extends AbstractDecoder {

	/**
	 * Decode data.
	 * 
	 * @param ibs
	 *            the ibs
	 * @param totalBytes
	 *            the total bytes
	 * @param version
	 *            the version
	 * @return the raw binary location reference
	 * @throws OpenLRBinaryException
	 *             the open lr binary processing exception {@inheritDoc}
	 */
	@Override
	public final RawLocationReference decodeData(final String id,
			final ByteArrayBitstreamInput ibs, final int totalBytes,
			final int version, final RawBinaryData binData)
			throws OpenLRBinaryException {
		// calculate number of intermediates
		// integer division!! (get rid of possible offset information)
		int nrIntermediates = (totalBytes - (OpenLRBinaryConstants.MIN_BYTES_LINE_LOCATION))
				/ OpenLRBinaryConstants.LRP_SIZE;

		FirstLRP firstLRP = null;
		// read first location reference point
		firstLRP = new FirstLRP(ibs);

		List<IntermediateLRP> intermediates = new ArrayList<IntermediateLRP>();
		// read intermediate location reference points
		for (int i = 0; i < nrIntermediates; ++i) {
			IntermediateLRP lrp = null;
			lrp = new IntermediateLRP(ibs);
			intermediates.add(lrp);
		}

		LastLRP lastLRP = null;
		// read last location reference point
		lastLRP = new LastLRP(ibs);

		Offset posOff = null;
		Offset negOff = null;
		// check for positive offset and read in
		if (lastLRP.getAttrib4().getPOffsetf() == OpenLRBinaryConstants.HAS_OFFSET) {
			posOff = null;
			posOff = new Offset(ibs);
		}
		// check for negative offset and read in
		if (lastLRP.getAttrib4().getNOffsetf() == OpenLRBinaryConstants.HAS_OFFSET) {
			negOff = null;
			negOff = new Offset(ibs);
		}
		Offsets offsets = new OffsetsBinaryImpl(0, 0);
		if (version == OpenLRBinaryConstants.BINARY_VERSION_2) {
			int pOffValue = 0;
			int nOffValue = 0;
			if (posOff != null) {
				pOffValue = calculateDistanceEstimate(posOff.getOffset());
			}
			if (negOff != null) {
				nOffValue = calculateDistanceEstimate(negOff.getOffset());
			}
			offsets = new OffsetsBinaryImpl(pOffValue, nOffValue);
		} else if (version == OpenLRBinaryConstants.BINARY_VERSION_3) {
			float pOffValue = 0;
			float nOffValue = 0;
			if (posOff != null) {
				pOffValue = calculateRelativeDistance(posOff.getOffset());
			}
			if (negOff != null) {
				nOffValue = calculateRelativeDistance(negOff.getOffset());
			}
			offsets = new OffsetsBinaryImpl(pOffValue, nOffValue);
		}
		int lrpCount = 1;
		List<LocationReferencePoint> points = new ArrayList<LocationReferencePoint>();
		LocationReferencePoint p = createLRP(lrpCount, firstLRP);
		lrpCount++;
		points.add(p);
		double prevLon = p.getLongitudeDeg();
		double prevLat = p.getLatitudeDeg();
		for (IntermediateLRP i : intermediates) {
			LocationReferencePoint ip = createLRP(lrpCount, i, prevLon, prevLat);
			lrpCount++;
			points.add(ip);
			prevLon = ip.getLongitudeDeg();
			prevLat = ip.getLatitudeDeg();
		}
		LocationReferencePoint lp = createLRP(lrpCount, lastLRP, prevLon,
				prevLat);
		points.add(lp);
		RawLineLocRef rawLocRef = new RawLineLocRef(id, points, offsets);
		if (binData != null) {
			binData.setBinaryNegOffset(negOff);
			binData.setBinaryPosOffset(posOff);
			binData.setBinaryLastLRP(lastLRP);
			binData.setBinaryIntermediates(intermediates);
			binData.setBinaryFirstLRP(firstLRP);
		}
		return rawLocRef;
	}

}
