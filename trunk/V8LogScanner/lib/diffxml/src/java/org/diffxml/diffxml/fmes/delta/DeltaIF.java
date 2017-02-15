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
package org.diffxml.diffxml.fmes.delta;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Interface for Delta formats.
 * 
 * Implement this to plug-in a new delta format.
 * 
 * @author Adrian Mouat
 *
 */
public interface DeltaIF {
    
    /**
     * Adds a Move operation to the EditScript. 
     * 
     * @param n The node being moved
     * @param parent XPath to the new parent Node
     * @param childno Child number of the parent n will become
     * @param ncharpos The new character position for the Node
     */
    void move(final Node n, final Node parent, final int childno, 
            final int ncharpos);
    
    /**
     * Adds a delete operation to the EditScript for the given Node.
     * 
     * @param n The Node that is to be deleted
     */
    void delete(final Node n);
    
    /**
     * Adds an insert operation to the EditScript.
     * 
     * @param n The node to insert
     * @param parent The Node to be parent of n
     * @param childno The child number of the parent node that n will become
     * @param charpos The character position to insert at
     */
    void insert(final Node n, final Node parent, final int childno,
            final int charpos);

    /**
     * Adds an update operation to the EditScript.
     * 
     * @param w The node to be updated
     * @param x The node it should be equal to
     */
    void update(Node w, Node x);

    /**
     * Get the XML Document for the EditScript.
     * 
     * @return The EditScript as an XML document.
     */
    Document getDocument();

}
