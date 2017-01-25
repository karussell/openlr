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
package openlr.mapviewer.gui.panels.dataviewer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;

/**
 * The XMLTreeCellRenderer extends the DefaultTreeCellRenderer and changes the
 * rendering process of the XML data.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class XMLTreeCellRenderer extends DefaultTreeCellRenderer {
	
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 667387679665818211L;

	/** The Constant ELEMENT_COLOR. */
	private static final Color ELEMENT_COLOR = new Color(0, 0, 128);
	
	/** The Constant ATTRIBUTE_COLOR. */
	private static final Color ATTRIBUTE_COLOR = new Color(0, 128, 0);

	/**
	 * Instantiates a new XML tree cell renderer.
	 */
	public XMLTreeCellRenderer() {
		setOpenIcon(null);
		setClosedIcon(null);
		setLeafIcon(null);
	}

	/**
	 * {@inheritDoc}
	 */
	public final Component getTreeCellRendererComponent(final JTree tree,
			final Object value, final boolean sel, final boolean expanded,
			final boolean leaf, final int row, final boolean hasFocus) {
		Node node = (Node) value;
		String text = null;
		switch (node.getNodeType()) {
		case Node.ELEMENT_NODE:
			text = '<' + node.getNodeName() + '>';
			break;
		case Node.ATTRIBUTE_NODE:
			text = '@' + node.getNodeName();
			break;
		case Node.TEXT_NODE:
			text = node.getNodeValue();
			break;
		case Node.COMMENT_NODE:
			text = "<!--" + node.getNodeValue() + "-->";
			break;
		case Node.DOCUMENT_TYPE_NODE:
			DocumentType dtype = (DocumentType) node;
			text = "<" + "!DOCTYPE " + dtype.getName() + '>';
			break;
		default:
			text = node.getNodeName();
		}
		super.getTreeCellRendererComponent(tree, text, sel, expanded, leaf,
				row, hasFocus);
		if (!selected) {
			switch (node.getNodeType()) {
			case Node.ELEMENT_NODE:
				setForeground(ELEMENT_COLOR);
				break;
			case Node.ATTRIBUTE_NODE:
				setForeground(ATTRIBUTE_COLOR);
				break;
			default:
				break;
			}
		}
		return this;
	}
}
