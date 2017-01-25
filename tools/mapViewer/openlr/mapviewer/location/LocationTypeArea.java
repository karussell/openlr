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
package openlr.mapviewer.location;

import java.util.ArrayList;
import java.util.List;

import openlr.decoder.OpenLRDecoderProcessingException;
import openlr.geomap.MapLayer;
import openlr.map.InvalidMapDataException;
import openlr.map.Line;
import openlr.map.MapDatabase;
import openlr.mapviewer.maplayer.CoverageMapLayer;

import org.apache.log4j.Logger;

/**
 * The Class LocationTypeArea.
 */
public abstract class LocationTypeArea extends AbstractLocationType {

    /**
     * The logger
     */
    private static final Logger LOG = Logger.getLogger(LocationTypeArea.class);
    
	/** The FACTOR. */
	protected static final double FACTOR = 111319.490793273564198;

	/** The area loc covered lines. */
	protected final List<Line> areaLocCoveredLines = new ArrayList<Line>();

	/** The area loc intersected lines. */
	protected final List<Line> areaLocIntersectedLines = new ArrayList<Line>();

	/**
	 * Build the Layer for the given lines, this method is relevant to draw the
	 * layer for covered lines as well as intersected line of an area location.
	 *
	 * @return the layer to be draw
	 * @author DLR. e.V. (LTouk)
	 */
	@Override
    public final MapLayer getCoverageMapLayer(final MapDatabase map) {
		try {
            updateCoverage(map);
        } catch (OpenLRDecoderProcessingException e) {
            LOG.error("Error calculating the effected lines", e);
        } catch (InvalidMapDataException e) {
            LOG.error("Error calculating the effected lines", e);
        }
		return new CoverageMapLayer(areaLocCoveredLines,
				areaLocIntersectedLines);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean providesCoverageInformation() {
		return true;
	}
	
    /**
     * Update coverage.
     * 
     * @param map
     *            The map database
     * @throws OpenLRDecoderProcessingException
     *             If an error occurs during calculation
     * @throws InvalidMapDataException
     *             If an error occurs during calculation
     */
    abstract void updateCoverage(final MapDatabase map)
            throws OpenLRDecoderProcessingException, InvalidMapDataException;

}
