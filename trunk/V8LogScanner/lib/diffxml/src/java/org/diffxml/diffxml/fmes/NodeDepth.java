/*
diffxml and patchxml - diff and patch for XML files

Copyright (C) 2002-2009 Adrian Mouat

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

Author: Adrian Mouat
email: adrian.mouat@gmail.com
 */

package org.diffxml.diffxml.fmes;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Associates depth with a node.
 *
 * @author Adrian Mouat
 */

public class NodeDepth {
    /**
     * Field holding nodes depth.
     */
    private final int mDepth;

    /**
     * Node we're pointing to.
     */
    private final Node mNode;

    /**
     * Create a NodeDepth for the given node.
     *
     * @param node The node to find the depth of
     */
    NodeDepth(final Node node) {
        
        if (node == null) {
            throw new NullPointerException("Node cannot be null");
        }
        mNode = node;
        mDepth = calculateDepth(mNode);
    }
    
    /**
     * Calculates the depth of a Node.
     * 
     * The root Node is at depth 0.
     * 
     * @param node The Node to calculate the depth of
     * @return The depth of the node
     */
    private int calculateDepth(final Node node) {
        
        int depth = 0;
        Node tmpNode = node;
        Node doc;
        if (node.getNodeType() == Node.DOCUMENT_NODE) {
            doc = node;
        } else {
            doc = tmpNode.getOwnerDocument();
        }

        while (!tmpNode.equals(doc)) {
            depth++;
            tmpNode = tmpNode.getParentNode();
        }
        return depth;
    }

    /**
     * Determines if two NodeInfo objects are equal.
     * 
     * Just calls equals on the underlying nodes.
     *
     * @param o NodeInfo to compare with
     * @return True if nodes are equal, otherwise false
     */

    public final boolean equals(final Object o) {

        boolean equals = false;
        
        if (o instanceof NodeDepth) {
            NodeDepth ni = (NodeDepth) o;
            equals = ni.mNode.equals(this.mNode);
        }
        
        return equals;
    }

    /**
     * Hashcode from underlying node.
     * 
     * @return hashcode
     */
    public final int hashCode() {
        return mNode.hashCode();
    }

    /**
     * Returns the depth value.
     *
     * @return The current depth value
     */
    public final int getDepth() {
        return mDepth;
    }

    /**
     * Returns the underlying node.
     *
     * @return The Node.
     */
    public final Node getNode() {
        return mNode;
    }
 
}
