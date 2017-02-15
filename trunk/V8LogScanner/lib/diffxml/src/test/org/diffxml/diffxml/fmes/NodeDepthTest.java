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

import org.diffxml.diffxml.TestDocHelper;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Test class for NodeDepth.
 * 
 * @author Adrian Mouat
 *
 */
public class NodeDepthTest {

    /** Test document. */
    private static Document mTestDoc1;
    
    /**
     * Initialises the test doc.
     */
    @BeforeClass
    public static void setUpTest() {
        
        String docString = "<x>text1<y><!-- comment --><z/>text2</y></x>";
        mTestDoc1 = TestDocHelper.createDocument(docString);
        
    }
    
    /** 
     * Helper method for testing nodes.
     *  
     * @param n The node to test
     * @param expectedDepth The expected depth of the node
     */
    private void testCreatingNodeDepth(final Node n, final int expectedDepth) {
        
        NodeDepth depthTest = new NodeDepth(n);
        assertEquals(expectedDepth, depthTest.getDepth());
        assertEquals(n, depthTest.getNode());
    }
    
    /**
     * Test calculating depth of nodes in document.
     */
    @Test
    public void testCorrectDepthCalculated() {
        
        //Try root
        testCreatingNodeDepth(mTestDoc1, 0);
        
        //Try document element
        Element docEl = mTestDoc1.getDocumentElement();
        testCreatingNodeDepth(docEl, 1);
        
        //Try first text node
        Node text1 = docEl.getFirstChild();
        testCreatingNodeDepth(text1, 2);
        
        //y Node
        Node y = text1.getNextSibling();
        testCreatingNodeDepth(y, 2);
        
        //Comment node
        Node comment = y.getFirstChild();
        testCreatingNodeDepth(comment, 3);
        
        //z Node
        Node z = comment.getNextSibling();
        testCreatingNodeDepth(z, 3);
        
        //second text node
        Node text2 = z.getNextSibling();
        testCreatingNodeDepth(text2, 3);                
    }
    
    /**
     * Check a NullPointerException is thrown if handed a null node.
     */
    @Test(expected = NullPointerException.class)  
    public void testNull() {
        NodeDepth nullTest = new NodeDepth(null);
    }
}
