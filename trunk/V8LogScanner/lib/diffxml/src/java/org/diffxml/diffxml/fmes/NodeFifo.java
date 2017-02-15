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

import java.util.LinkedList;
import java.util.Queue;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

/**
 * Implements a First In First Out list.
 *
 * Equivalent to a stack where elements are removed from
 * the *opposite* end to where the are added. Hence the
 * Stack terms "push" and pop" are used.
 * 
 * Only real addition over Java library is method to add children of a node.
 */

public class NodeFifo {
    
    /**
     * Underlying list.
     */
    private final Queue<Node> mFifo;

    /**
     * Default constructor.
     */
    NodeFifo() {
        
        /*
         * TODO: Check if ArrayList is faster.
         */
        mFifo = new LinkedList<Node>();
    }

    /**
     * Adds a Node to the Fifo.
     *
     * @param n the Node to added
     */
    public final void push(final Node n) {
        mFifo.add(n);
    }

    /**
     * Checks if the Fifo contains any objects.
     *
     * @return true if there are any objects in the Fifo
     */

    public final boolean isEmpty() {
        return mFifo.isEmpty();
    }

    /**
     * Remove a Node from the Fifo.
     *
     * This Node is always the oldest item in the array.
     *
     * @return the oldest item in the Fifo
     */
    public final Node pop() {

        Node ret;
        if (mFifo.isEmpty()) {
            ret = null;
        } else {
            ret = mFifo.remove();
        }

        return ret;
    }

    /**
     * Adds the children of a node to the fifo.
     *
     * TODO: Check use of isBanned()
     * 
     * @param x    the node whose children are to be added
     */
    public final void addChildrenOfNode(final Node x) {
        
        NodeList kids = x.getChildNodes();

        if (kids != null) {
            for (int i = 0; i < kids.getLength(); i++) {
                if (Fmes.isBanned(kids.item(i))) {
                    continue;
                }

                push(kids.item(i));
            }
        }
    }

}
