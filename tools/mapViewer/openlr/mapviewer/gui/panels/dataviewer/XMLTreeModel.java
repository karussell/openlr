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

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The XMLTreeModel implements the TreeModel interface and is used to present
 * XML data. This model is not editable.
 * 
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 */
public class XMLTreeModel implements TreeModel { 
    
    /** The node. */
    private Node node; 

 
    /**
     * Instantiates a new XML tree model with root n.
     * 
     * @param n the root node
     */
    public XMLTreeModel(final Node n) { 
        this.node = n; 
    } 
 
    /**
     * {@inheritDoc}
     */
    @Override
    public final Object getRoot() { 
        return node; 
    } 
 
    /**
     * {@inheritDoc}
     */
    @Override
    public final Object getChild(final Object parent, final int index) { 
        Node n = (Node) parent; 
        NamedNodeMap attrs = n.getAttributes(); 
        int attrCount = 0;
        if (attrs != null) {
        	attrCount = attrs.getLength(); 
        }
        if (index < attrCount) {
            return attrs.item(index); 
        }
        NodeList children = n.getChildNodes(); 
        return children.item(index - attrCount); 
    } 
 
    /**
     * {@inheritDoc}
     */
    @Override
    public final int getChildCount(final Object parent) { 
        Node n = (Node) parent; 
        NamedNodeMap attrs = n.getAttributes(); 
        int attrCount = 0;
        if (attrs != null) {
        	attrCount = attrs.getLength(); 
        }
        NodeList children = n.getChildNodes(); 
        int childCount = 0;
        if (children != null) {
        	childCount = children.getLength(); 
        }
        return attrCount + childCount; 
    } 
 
    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isLeaf(final Object n) { 
        return getChildCount(n) == 0; 
    } 
 
    /**
     * {@inheritDoc}
     */
    @Override
    public final int getIndexOfChild(final Object parent, final Object child) { 
        Node n = (Node) parent; 
        Node childNode = (Node) child; 
 
        NamedNodeMap attrs = n.getAttributes(); 
        int attrCount = 0;
        if (attrs != null) {
        	attrCount = attrs.getLength(); 
        }
        if (childNode.getNodeType() == Node.ATTRIBUTE_NODE) { 
            for (int i = 0; i < attrCount; i++) { 
                if (attrs.item(i) == child) {
                    return i; 
                }
            } 
        } else { 
            NodeList children = n.getChildNodes(); 
            int childCount = 0;
            if (children != null) {
            	childCount = children.getLength(); 
            }
            for (int i = 0; i < childCount; i++) { 
                if (children.item(i) == child) { 
                    return attrCount + i; 
                }
            } 
        } 
        throw new RuntimeException("this should never happen!"); 
    } 
 
    /**
     * {@inheritDoc}
     */
    @Override
    public void addTreeModelListener(final TreeModelListener listener) { 
        // not editable 
    } 
 
    /**
     * {@inheritDoc}
     */
    @Override
    public void removeTreeModelListener(final TreeModelListener listener) { 
        // not editable 
    } 
 
    /**
     * {@inheritDoc}
     */
    @Override
    public void valueForPathChanged(final TreePath path, final Object newValue) { 
        // not editable 
    } 
}

