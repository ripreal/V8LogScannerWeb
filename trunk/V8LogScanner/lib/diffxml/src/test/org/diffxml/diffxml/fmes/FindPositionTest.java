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

import org.diffxml.diffxml.TestDocHelper;
import org.junit.Test;
import org.w3c.dom.Document;


import org.w3c.dom.Node;

/**
 * Test FindPosition.
 * 
 * @author Adrian Mouat
 */
public class FindPositionTest {

       
    /**
     * Test simple insert of element.
     */
    @Test
    public final void testElementInsert() {

        Document testDoc1 = TestDocHelper.createDocument("<a><b/></a>");
        Document testDoc2 = TestDocHelper.createDocument("<a><b/><c/></a>");
        
        NodePairs pairs = Match.easyMatch(testDoc1, testDoc2);
        
        Node c = testDoc2.getFirstChild().getFirstChild().getNextSibling();
        assertEquals("c", c.getNodeName());
        FindPosition fp = new FindPosition(c, pairs);
        assertEquals(1, fp.getDOMInsertPosition());
        assertEquals(2, fp.getXPathInsertPosition());
        assertEquals(1, fp.getCharInsertPosition());
    }

    /**
     * Test where no leftmost match.
     */
    @Test
    public final void testSimpleInsert() {

        Document testDoc1 = TestDocHelper.createDocument("<a><b/><c/></a>");
        Document testDoc2 = TestDocHelper.createDocument("<a><d/><e/></a>");
        
        NodePairs pairs = Match.easyMatch(testDoc1, testDoc2);
        //Need to mark d out-of-order for the algorithm to work
        NodeOps.setOutOfOrder(testDoc2.getFirstChild().getFirstChild());
        
        Node e = testDoc2.getFirstChild().getFirstChild().getNextSibling();
        assertEquals("e", e.getNodeName());
        FindPosition fp = new FindPosition(e, pairs);
        assertEquals(0, fp.getDOMInsertPosition());
        assertEquals(1, fp.getXPathInsertPosition());
        assertEquals(1, fp.getCharInsertPosition());
    }
    
    /**
     * Test inserting a node after text with a leftmost match.
     */
    @Test
    public final void testInsertingAfterText() {

        Document testDoc1 = TestDocHelper.createDocument("<a>sometext</a>");
        Document testDoc2 = TestDocHelper.createDocument("<a>sometext<b/></a>");
        
        NodePairs pairs = Match.easyMatch(testDoc1, testDoc2);
        
        Node b = testDoc2.getFirstChild().getFirstChild().getNextSibling();
        assertEquals("b", b.getNodeName());
        FindPosition fp = new FindPosition(b, pairs);
        assertEquals(1, fp.getDOMInsertPosition());
        assertEquals(2, fp.getXPathInsertPosition());
        assertEquals(9, fp.getCharInsertPosition());
    }

}
