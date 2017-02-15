/*
diffxml and patchxml - diff and patch for XML files

Copyright (C) 2002-2009  Adrian Mouat

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

import org.w3c.dom.Node;
import java.util.ArrayList;

/**
 * Class to hold pairs of nodes.
 * TODO: Test if any performance benefit from using UserData.
 */
public class NodePairs {

    /**
     * Key for user data on whether the node is matched.
     */
    private static final String MATCHED = "matched";
    
    /**
     * Internal list to store nodes.
     */
    private ArrayList<Node> mPairs = new ArrayList<Node>();

    /**
     * Adds a pair of nodes to the set.
     * Sets UserData as matched.
     * 
     * @param x
     *            first node
     * @param y
     *            partner of first node
     */
    public final void add(final Node x, final Node y) {
        
        if (x == null || y == null) {
            throw new NullPointerException("Nodes cannot be null");
        }
        
        mPairs.add(x);
        mPairs.add(y);
        setMatched(x, y);
    }

    /**
     * Mark the node as being "matched".
     *
     * @param n the node to mark as "matched"
     */
    private static void setMatched(final Node n) {
        n.setUserData(MATCHED, true, null);
    }

    /**
     * Check if node is marked "matched".
     *
     * Made static so that I can use a instance method later if it is faster or
     * better.
     * 
     * @param n node to check
     * @return true if marked "matched", false otherwise
     */
    public final boolean isMatched(final Node n) {
        
        boolean ret;
        Object data = n.getUserData(MATCHED);
        if (data == null) {
            ret = false;
        } else {
            ret = (Boolean) data;
        }
        return ret;
    }
    
    /**
     * Mark a pair of nodes as matched.
     *
     * @param nodeA  The unmatched partner of nodeB
     * @param nodeB  The unmatched partner of nodeA
     */
    private static void setMatched(final Node nodeA, final Node nodeB) {
        setMatched(nodeA);
        setMatched(nodeB);
    }
    
    /**
     * Returns the partner of a given node. Returns null if the node does not
     * exist.
     * 
     * @param n
     *            the node to find the partner of.
     * @return the partner of n.
     */
    public final Node getPartner(final Node n) {

        Node ret = null;
        int in = mPairs.indexOf(n);

        if (in != -1) {

            if ((in % 2) == 1) {
                ret = mPairs.get(--in);
            } else {
                ret = mPairs.get(++in);
            }
        }

        return ret;
    }

    /**
     * Get the number of nodes stored. 
     * 
     * Note that this includes both nodes and partners.
     * 
     * @return The number of nodes stored.
     */
    public final int size() {
        return mPairs.size();
    }

    /**
     * Remove a node and it's partner from the list of matchings.
     * 
     * @param n The Node to remove
     */
    public final void remove(final Node n) {
        
        Node nMatch = getPartner(n);
        
        nMatch.setUserData(MATCHED, null, null);
        n.setUserData(MATCHED, null, null);
        
        mPairs.remove(getPartner(n));
        mPairs.remove(n);
    }
}
