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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.diffxml.diffxml.DOMOps;
import org.diffxml.diffxml.TestDocHelper;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Test Delta creates elements DUL elements properly.
 * 
 * @author Adrian Mouat
 */
public class DULDeltaTest {

    /**
     * The edit script to add commands to.
     */
    private DULDelta mDelta;

    /**
     * Encoding for printing documents.
     */
    private static final String ENCODING = "UTF-8";
    
    /**
     * Set up the DUL EditScript.
     */
    @Before
    public final void setUp() {
        try {
            mDelta = new DULDelta();
        } catch (DeltaInitialisationException e) {
            fail("Caught Exception: " + e.getMessage());
        }
    }
    
    /**
     * Test inserting a new element.
     */
    @Test
    public final void testInsertElement() {
        
        Document testDoc2 = TestDocHelper.createDocument("<a></a>");
        Node ins = testDoc2.createElement("insertTest");
        mDelta.insert(ins, "/a", 1, 1);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        
        try {
            DOMOps.outputXML(mDelta.getDocument(), os);
            String out = new String(os.toByteArray(), ENCODING);
            assertTrue(out.contains(
                    "><insert childno=\"1\" name=\"insertTest\" "
                    + "nodetype=\"1\" parent=\"/a\"/></delta>"));
        } catch (UnsupportedEncodingException e) {
            fail("Caught exception: " + e.getMessage());
        } catch (IOException e) {
            fail("Caught exception: " + e.getMessage());
        }
    }
        
    /**
     * Test inserting an attribute.
     */
    @Test
    public final void testInsertAttribute() {

        Document testDoc2 = TestDocHelper.createDocument("<a></a>");
        Attr ins = testDoc2.createAttribute("insertTest");
        ins.setNodeValue("ins");
        mDelta.insert(ins, "/a", 1, 1);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            DOMOps.outputXML(mDelta.getDocument(), os);
            String out = new String(os.toByteArray(), ENCODING);
            assertTrue(out.contains("><insert name=\"insertTest\""
                    + " nodetype=\"2\" parent=\"/a\">ins</insert></delta>"));
        } catch (UnsupportedEncodingException e) {
            fail("Caught exception: " + e.getMessage());
        } catch (IOException e) {
            fail("Caught exception: " + e.getMessage());
        }        
    }

    /**
     * Test inserting an element with attributes.
     */
    @Test
    public final void testInsertElementWithAttrs() {
        
        Document testDoc2 = TestDocHelper.createDocument("<a></a>");
        Element ins = testDoc2.createElement("attrTest");
        Attr attr1 = testDoc2.createAttribute("attrTest1");
        attr1.setValue("one");
        ins.setAttributeNode(attr1);
        Attr attr2 = testDoc2.createAttribute("attrTest2");
        attr2.setValue("two");
        ins.setAttributeNode(attr2);
        mDelta.insert(ins, "/a", 1, 1);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            DOMOps.outputXML(mDelta.getDocument(), os);
            String out = new String(os.toByteArray(), ENCODING);
            assertTrue(out.contains(
                    "><insert childno=\"1\" name=\"attrTest\" "
                    + "nodetype=\"1\" parent=\"/a\"/>"));
            assertTrue(out.contains("<insert name=\"attrTest1\" nodetype=\"2\" "
                    + "parent=\"/a/node()[1]\">one</insert>"));
            assertTrue(out.contains("<insert name=\"attrTest2\" nodetype=\"2\" "
                    + "parent=\"/a/node()[1]\">two</insert></delta>"));
        } catch (UnsupportedEncodingException e) {
            fail("Caught exception: " + e.getMessage());
        } catch (IOException e) {
            fail("Caught exception: " + e.getMessage());
        }
    }
    
    /**
     * Test inserting a comment.
     */
    @Test
    public final void testInsertComment() {

        Document testDoc2 = TestDocHelper.createDocument("<a></a>");
        Comment ins = testDoc2.createComment("insertTest");
        ins.setNodeValue("ins");
        mDelta.insert(ins, "/a", 1, 1);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            DOMOps.outputXML(mDelta.getDocument(), os);
            String out = new String(os.toByteArray(), ENCODING);
            assertTrue(out.contains(
                    "><insert childno=\"1\" nodetype=\"8\" parent=\"/a\">"
                    + "ins</insert></delta>"));
        } catch (UnsupportedEncodingException e) {
            fail("Caught exception: " + e.getMessage());
        } catch (IOException e) {
            fail("Caught exception: " + e.getMessage());
        }        
    }
    
    /**
     * Test deleting an Element.
     */
    @Test
    public final void testDeleteElement() {

        Document testDoc2 = TestDocHelper.createDocument("<a></a>");
        Element del = testDoc2.createElement("deleteTest");
        testDoc2.getDocumentElement().appendChild(del);
        
        mDelta.delete(del);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            DOMOps.outputXML(mDelta.getDocument(), os);
            String out = new String(os.toByteArray(), ENCODING);
            assertTrue(out.contains(
                    "><delete node=\"/node()[1]/node()[1]\"/></delta>"));
        } catch (UnsupportedEncodingException e) {
            fail("Caught exception: " + e.getMessage());
        } catch (IOException e) {
            fail("Caught exception: " + e.getMessage());
        }        
    }

    /**
     * Test deleting a text node.
     */
    @Test
    public final void testDeleteTextNode() {

        //Check start, end, middle
        Document testDoc2 = TestDocHelper.createDocument("<a></a>");
        Node del1 = testDoc2.createTextNode("text");
        Node del2 = testDoc2.createTextNode("moretext");
        Node del3 = testDoc2.createTextNode("evenmoretext");
        testDoc2.getDocumentElement().appendChild(del1);
        testDoc2.getDocumentElement().appendChild(del2);
        testDoc2.getDocumentElement().appendChild(del3);
        
        mDelta.delete(del1);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            DOMOps.outputXML(mDelta.getDocument(), os);
            String out = new String(os.toByteArray(), ENCODING);
            assertTrue(out.contains(
                    "><delete charpos=\"1\" length=\"4\" "
                    + "node=\"/node()[1]/node()[1]\"/></delta>"));
        } catch (UnsupportedEncodingException e) {
            fail("Caught exception: " + e.getMessage());
        } catch (IOException e) {
            fail("Caught exception: " + e.getMessage());
        }
        
        mDelta.delete(del2);
        os.reset();

        try {
            DOMOps.outputXML(mDelta.getDocument(), os);
            String out = new String(os.toByteArray(), ENCODING);
            assertTrue(out.contains(
                    "<delete charpos=\"5\" length=\"8\" "
                    + "node=\"/node()[1]/node()[1]\"/></delta>"));
        } catch (UnsupportedEncodingException e) {
            fail("Caught exception: " + e.getMessage());
        } catch (IOException e) {
            fail("Caught exception: " + e.getMessage());
        }
        
        mDelta.delete(del3);
        os.reset();

        try {
            DOMOps.outputXML(mDelta.getDocument(), os);
            String out = new String(os.toByteArray(), ENCODING);
            assertTrue(out.contains(
                    "<delete charpos=\"13\" length=\"12\" "
                    + "node=\"/node()[1]/node()[1]\"/></delta>"));
        } catch (UnsupportedEncodingException e) {
            fail("Caught exception: " + e.getMessage());
        } catch (IOException e) {
            fail("Caught exception: " + e.getMessage());
        }
    }

    /**
     * Test deleting a comment.
     */
    @Test
    public final void testDeleteComment() {

        //Check start, end, middle
        Document testDoc2 = TestDocHelper.createDocument("<a></a>");
        Node  del = testDoc2.createComment("deleteTest");
        testDoc2.getDocumentElement().appendChild(del);
        
        mDelta.delete(del);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            DOMOps.outputXML(mDelta.getDocument(), os);
            String out = new String(os.toByteArray(), ENCODING);
            assertTrue(out.contains(
                    "><delete node=\"/node()[1]/node()[1]\"/></delta>"));
        } catch (UnsupportedEncodingException e) {
            fail("Caught exception: " + e.getMessage());
        } catch (IOException e) {
            fail("Caught exception: " + e.getMessage());
        }        
    }

    /**
     * Test deleting an attribute.
     */
    @Test
    public final void testDeleteAttribute() {

        //Check start, end, middle
        Document testDoc2 = TestDocHelper.createDocument("<a><b/></a>");
        Attr del = testDoc2.createAttribute("deleteTest");
        del.setNodeValue("del");
        ((Element) testDoc2.getDocumentElement().getFirstChild()
                ).setAttributeNode(del);
 
        mDelta.delete(del);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            DOMOps.outputXML(mDelta.getDocument(), os);
            String out = new String(os.toByteArray(), ENCODING);
            assertTrue(out.contains(
                    ">"
                    + "<delete node=\"/node()[1]/node()[1]/@deleteTest\"/>"
                    + "</delta>"));
        } catch (UnsupportedEncodingException e) {
            fail("Caught exception: " + e.getMessage());
        } catch (IOException e) {
            fail("Caught exception: " + e.getMessage());
        }        
    }
    
    /**
     * Test moving an Element.
     */
    @Test
    public final void testMove() {

        Document testDoc2 = TestDocHelper.createDocument("<a><b/><c/></a>");
        Node move = testDoc2.createElement("moveTest");
        testDoc2.getDocumentElement().getFirstChild().appendChild(move);
        Node moveTo = testDoc2.getDocumentElement().getFirstChild(
                ).getNextSibling();
        
        mDelta.move(move, moveTo, 1, 1);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            DOMOps.outputXML(mDelta.getDocument(), os);
            String out = new String(os.toByteArray(), ENCODING);
            assertTrue(out.contains(
                    "><move childno=\"1\" new_charpos=\"1\" "
                    + "node=\"/node()[1]/node()[1]/node()[1]\" "
                    + "old_charpos=\"1\" "
                    + "parent=\"/node()[1]/node()[2]\"/></delta>"));
        } catch (UnsupportedEncodingException e) {
            fail("Caught exception: " + e.getMessage());
        } catch (IOException e) {
            fail("Caught exception: " + e.getMessage());
        }        
    }

    /**
     * Test moving an Element at a character offset.
     */
    @Test
    public final void testMoveInText() {

        Document testDoc2 = TestDocHelper.createDocument(
                "<a><b>text</b><c>moretext</c></a>");
        Node move = testDoc2.createElement("moveTest");
        testDoc2.getDocumentElement().getFirstChild().appendChild(move);
        Node moveTo = testDoc2.getDocumentElement().getFirstChild(
                ).getNextSibling();
        
        mDelta.move(move, moveTo, 2, 9);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        
        try {
            DOMOps.outputXML(mDelta.getDocument(), os);
            String out = new String(os.toByteArray(), ENCODING);
            assertTrue(out.contains(
                    "><move childno=\"2\" new_charpos=\"9\" "
                    + "node=\"/node()[1]/node()[1]/node()[2]\" "
                    + "old_charpos=\"5\" "
                    + "parent=\"/node()[1]/node()[2]\"/></delta>"));
        } catch (UnsupportedEncodingException e) {
            fail("Caught exception: " + e.getMessage());
        } catch (IOException e) {
            fail("Caught exception: " + e.getMessage());
        }       
    }

    /**
     * Test moving a text Node.
     */
    @Test
    public final void testMoveTextNode() {
        
        Document testDoc2 = TestDocHelper.createDocument("<a><b/><c/></a>");
        Node move = testDoc2.createTextNode("moveTest");
        testDoc2.getDocumentElement().getFirstChild().appendChild(move);
        Node moveTo = testDoc2.getDocumentElement().getFirstChild(
                ).getNextSibling();
        
        mDelta.move(move, moveTo, 1, 1);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        
        try {
            DOMOps.outputXML(mDelta.getDocument(), os);
            String out = new String(os.toByteArray(), ENCODING);
            assertTrue(out.contains(
                    "><move childno=\"1\" length=\"8\" new_charpos=\"1\" "
                    + "node=\"/node()[1]/node()[1]/node()[1]\" "
                    + "old_charpos=\"1\" "
                    + "parent=\"/node()[1]/node()[2]\"/></delta>"));
        } catch (UnsupportedEncodingException e) {
            fail("Caught exception: " + e.getMessage());
        } catch (IOException e) {
            fail("Caught exception: " + e.getMessage());
        }
        
    }
}
