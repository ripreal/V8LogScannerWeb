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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.diffxml.diffxml.TestDocHelper;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Test the NodeFifo works.
 * 
 * Essentially a first-in-first-out form a stack with extra Node operations.
 * 
 * @author Adrian Mouat
 *
 */
public class NodeFifoTest {

    /**
     * Test an empty fifo is empty.
     */
    @Test
    public void testEmptyFifo() {
        
        NodeFifo fifo = new NodeFifo();
        assertTrue(fifo.isEmpty());
        assertNull(fifo.pop());
    }
    
    /**
     * Test nodes are pushed and popped in the right order.
     */
    @Test
    public final void testPushPopOrder() {
        
        Document testDoc = TestDocHelper.createDocument("<a><b><c/></b></a>");
        NodeFifo fifo = new NodeFifo();
        Node docEl = testDoc.getDocumentElement();
        fifo.push(docEl);
        assertEquals(docEl, fifo.pop());
        assertNull(fifo.pop());
        
        Node b =  docEl.getFirstChild();
        Node c = docEl.getFirstChild().getFirstChild();
        
        fifo.push(docEl);
        fifo.push(b);
        fifo.push(c);
        
        assertEquals(docEl, fifo.pop());
        assertEquals(b, fifo.pop());
        assertEquals(c, fifo.pop());
        assertNull(fifo.pop());
        
    }
    
    /**
     * Test that children of a node are added in the correct order.
     */
    @Test
    public final void testAddChildrenOfNode() {
        
        Document testDoc = TestDocHelper.createDocument(
                "<a><b/><c/><d/></a>");
        
        NodeFifo fifo = new NodeFifo();
        Node docEl = testDoc.getDocumentElement();
        fifo.push(docEl);
        fifo.addChildrenOfNode(docEl);
        
        assertEquals(docEl, fifo.pop());
        assertEquals("b", fifo.pop().getNodeName());
        assertEquals("c", fifo.pop().getNodeName());
        assertEquals("d", fifo.pop().getNodeName());
        assertNull(fifo.pop());
        
        //Check nothing happens if add node with no children
        fifo.addChildrenOfNode(docEl.getFirstChild());
        assertNull(fifo.pop());
        
    }
}
