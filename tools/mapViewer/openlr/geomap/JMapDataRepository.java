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
package openlr.geomap;

import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import openlr.map.InvalidMapDataException;
import openlr.map.Line;
import openlr.map.MapDatabase;
import openlr.map.Node;

/**
 * The Class JMapDataRepository.
 */
public class JMapDataRepository {

	/** The bb. */
	private final Rectangle2D.Double bb;

	/** The lines. */
	private final Map<Long, Line> lines = new HashMap<Long, Line>();
	
	/** The nodes. */
	private final Map<Long, Node> nodes = new HashMap<Long, Node>();
	
	/** The map. */
	private final MapDatabase map;
	
	/**
	 * Instantiates a new j map data repository.
	 *
	 * @param mdb the mdb
	 * @throws InvalidMapDataException the invalid map data exception
	 */
	public JMapDataRepository(final MapDatabase mdb) throws InvalidMapDataException {
		bb = mdb.getMapBoundingBox();
		Iterator<? extends Line> iter = mdb.getAllLines();
		while (iter.hasNext()) {
			Line l = iter.next();
			Node start = l.getStartNode();
			Node end = l.getEndNode();
			nodes.put(start.getID(), start);
			nodes.put(end.getID(), end);
			lines.put(l.getID(), l);
		}
		map = mdb;
	}

	/**
	 * Gets the map rectangle.
	 * 
	 * @return the map rectangle
	 */
	public final Rectangle2D.Double getMapRectangle() {
		return bb;
	}

	/**
	 * Gets the lines.
	 *
	 * @return the lines
	 */
	public final Collection<Line> getLines() {
		return lines.values();
	}

	/**
	 * Gets the line.
	 *
	 * @param id the id
	 * @return the line
	 */
	public final Line getLine(final long id) {
		return lines.get(id);
	}

	/**
	 * Gets the node.
	 *
	 * @param id the id
	 * @return the node
	 */
	public final Node getNode(final long id) {
		return nodes.get(id);
	}

	/**
	 * Gets the nodes.
	 *
	 * @return the nodes
	 */
	public final Collection<Node> getNodes() {
		return nodes.values();
	}
	
	/**
	 * Gets the map database.
	 *
	 * @return the map database
	 */
	public final MapDatabase getMapDatabase() {
		return map;
	}

}
