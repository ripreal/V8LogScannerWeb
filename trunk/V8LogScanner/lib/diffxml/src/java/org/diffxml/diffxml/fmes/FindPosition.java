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

import org.diffxml.diffxml.DOMOps;
import org.w3c.dom.Node;

/**
 * Finds the position to insert a Node at.
 * 
 * Calculates XPath, DOM and character position.
 * @author Adrian Mouat
 *
 */
public class FindPosition {
 
    /** The DOM position. */
    private int mInsertPositionDOM;
    
    /** The XPath position. */
    private int mInsertPositionXPath;
    
    /** The character position. */
    private int mCharInsertPosition;
    
    /**
     * Finds the child number to insert a node as.
     *
     * (Equivalent to the current child number of the node to insert
     * before)
     *
     * @param x         the node with no partner
     * @param matchings the set of matching nodes
     */
    public FindPosition(final Node x, final NodePairs matchings) {

        Node v = getInOrderLeftSibling(x);

        if (v == null) {
            
            mInsertPositionDOM = 0;
            mInsertPositionXPath = 1;
            mCharInsertPosition = 1;
            
        } else {

            /**
             * Get partner of v and return index after
             * (we want to insert after the previous in-order node, so that
             * w's position is equivalent to x's).
             */
            Node u = matchings.getPartner(v);
            assert (u != null);

            ChildNumber uChildNo = new ChildNumber(u);
            Node w = matchings.getPartner(x);

            //Need position after u
            //NOTE: This is different from the FMES algorithm, which *wrongly*
            //indicates we should use the "in-order" child number.
            if (w != null) {
                //Doing a move, need to be careful not to count node being moved
                mInsertPositionDOM = uChildNo.getDOMIgnoring(w) + 1;
                mInsertPositionXPath = uChildNo.getXPathIgnoring(w) + 1;
            } else {
                mInsertPositionDOM = uChildNo.getDOM() + 1;
                mInsertPositionXPath = uChildNo.getXPath() + 1;
            }

            //For xpath, character position is used if node is text node
            if (DOMOps.isText(u)) {
                if (w != null) {
                    mCharInsertPosition = uChildNo.getXPathCharPosIgnoring(w)
                        + u.getTextContent().length();
                } else {
                    mCharInsertPosition = uChildNo.getXPathCharPos()
                        + u.getTextContent().length();
                }
            } else {
                mCharInsertPosition = 1;
            }
        }

    }
    
    /**
     * Gets the rightmost left sibling of n marked "inorder".
     *
     * @param n Node to find "in order" left sibling of
     * @return  Either the "in order" left sibling or null if none
     */
    private static Node getInOrderLeftSibling(final Node n) {
        
        Node curr = n.getPreviousSibling();
        while (curr != null && !NodeOps.isInOrder(curr)) {
            curr = curr.getPreviousSibling();
        }

        return curr;
    }

    /**
     * Returns the DOM number the node should have when inserted.
     * 
     * @return the DOM number to insert the node as
     */
    public final int getDOMInsertPosition() {
        return mInsertPositionDOM;
    }
    
    /**
     * Returns the XPath number the node should have when inserted.
     * 
     * @return The XPath number to insert the node as
     */
    public final int getXPathInsertPosition() {
        return mInsertPositionXPath;
    }
    
    /**
     * Returns the character position to insert the node as.
     * 
     * @return The character position to insert the node at
     */
    public final int getCharInsertPosition() {
        return mCharInsertPosition;
    }
}
