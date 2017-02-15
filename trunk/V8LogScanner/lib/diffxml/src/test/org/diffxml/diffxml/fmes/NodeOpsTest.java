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

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import org.diffxml.diffxml.DOMOps;
import org.diffxml.diffxml.TestDocHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class to test helper functions in NodeOps.
 * 
 * @author Adrian Mouat
 *
 */
public class NodeOpsTest {

    /**
     * Test getting the unique XPath for nodes.
     */
    @Test
    public final void testGetXPath() {
        //Create an XML doc, loop through nodes, confirming that doing a
        //getXPath then a select returns the node
        XPathFactory xPathFac = XPathFactory.newInstance();
        XPath xpath = xPathFac.newXPath();
        
        Document testDoc = TestDocHelper.createDocument(
                "<a>aa<b attr='test'>b<!-- comment -->c<c/></b>d</a>");
        
        Node b = testDoc.getDocumentElement().getFirstChild().getNextSibling();
        
        //Old test to ensure comment nodes are processed
        assertEquals(b.getFirstChild().getNextSibling().getNodeType(),
                Node.COMMENT_NODE);
        assertEquals(b.getChildNodes().item(1).getNodeType(), 
                Node.COMMENT_NODE); 
        
        testXPathForNode(testDoc.getDocumentElement(), xpath);
    }
    
    /**
     * Helper method for testGetXPath.
     * 
     * Gets the XPath for the node and evaluates it, checking if the same node
     * is returned. 
     * 
     * DocumentType nodes are ignored as they cannot be identified by an XPath
     * 
     * @param n The node to test
     * @param xp XPath expression (reused for efficiency only)
     */
    private void testXPathForNode(final Node n, final XPath xp) {
        
        if (n.getNodeType() != Node.DOCUMENT_TYPE_NODE) {
            String xpath = NodeOps.getXPath(n);
            //Uncomment for debug info
            //System.out.println("Node: " + DOMOps.getNodeAsString(n) 
            //    + " XPath:" + xpath);
            compareXPathResult(n, xpath, xp);
        }
    }
    
    /**
     * Compares the result of the xpath expression to the expected Node n.
     * 
     * Also tests children.
     *
     * @param n The expected result node
     * @param xpath The expression to evaluate
     * @param xp XPath expression (for efficiency)
     */
    private void compareXPathResult(final Node n, final String xpath, 
            final XPath xp) {

        Document doc;
        if (n.getNodeType() == Node.DOCUMENT_NODE) {
            doc = (Document) n;
        } else {
            doc = n.getOwnerDocument();
        }
        
        try {
            Node ret = (Node) xp.evaluate(
                    xpath, doc, XPathConstants.NODE);
            assertNotNull(ret);

            if (DOMOps.isText(n)) {
                Node textNode = ret;
                String text = "";
                while (DOMOps.isText(textNode)) {
                    text = text + textNode.getNodeValue();
                    textNode = textNode.getNextSibling();
                }
                
                assertTrue(text + " does not contain " + n.getTextContent(), 
                        text.contains(n.getTextContent()));
            } else {
                assertTrue(
                        ret.getNodeName() + ":" + ret.getNodeValue() 
                        + " is not " + n.getNodeName() + ":" + n.getNodeValue(),
                        n.isSameNode((Node) ret));
            }
        } catch (XPathExpressionException e) {
            fail("Caught exception: " + e.getMessage());
        }

        //Test children
        if (!(n.getNodeType() == Node.ATTRIBUTE_NODE)) {
            NodeList list = n.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                testXPathForNode(list.item(i), xp);
            }
        }
    }

    /**
     * Test for the horrible coalesced text nodes issue.
     * 
     */
    @Test
    public final void testGetXPathWithTextNodes() {
        
        Document testDoc = TestDocHelper.createDocument("<a>b</a>");
        Element docEl = testDoc.getDocumentElement();
        Node b = docEl.getFirstChild();
        Node c = testDoc.createTextNode("c\n");
        docEl.appendChild(c);
        Node d = testDoc.createElement("d");
        docEl.appendChild(d);
        Node e = testDoc.createTextNode("e");
        docEl.appendChild(e);
        String bxpath = NodeOps.getXPath(b);
        String cxpath = NodeOps.getXPath(c);
        String dxpath = NodeOps.getXPath(d);
        String expath = NodeOps.getXPath(e);

        //Have to normalize the doc for the XPath context to be correct.
        testDoc.normalize();
        
        //Move to beforeclass method
        XPathFactory xPathFac = XPathFactory.newInstance();
        XPath xp = xPathFac.newXPath();
 
        compareXPathResult(b, bxpath, xp);       
        compareXPathResult(c, cxpath, xp);       
        compareXPathResult(d, dxpath, xp);       
        compareXPathResult(e, expath, xp);       
    }
    
    /**
     * Test getting XPath for attributes.
     */
    @Test
    public final void testGetXPathForAttributes() {
        
        Document testDoc = TestDocHelper.createDocument(
                "<a><b attr=\"test\"/></a>");
        Element docEl = testDoc.getDocumentElement();
        NamedNodeMap attrs = docEl.getFirstChild().getAttributes();
        
        //Move to beforeclass method
        XPathFactory xPathFac = XPathFactory.newInstance();
        XPath xpathExpr = xPathFac.newXPath();
 
        testXPathForNode(attrs.item(0), xpathExpr);
    }   
 
    /**
     * Test getting XPath with namespaced element.
     */
    @Test
    public final void testGetXPathWithNamespace() {
        
        Document testDoc = TestDocHelper.createDocument(
                "<d:a xmlns:d=\"http://test.com\"><b/></d:a>");
        Element docEl = testDoc.getDocumentElement();
        
        //Move to beforeclass method
        XPathFactory xPathFac = XPathFactory.newInstance();
        XPath xpathExpr = xPathFac.newXPath();
 
        testXPathForNode(docEl, xpathExpr);
        testXPathForNode(docEl.getFirstChild(), xpathExpr);
    }   

    /**
     * Test check for blank text nodes.
     */
    @Test
    public final void testCheckForBlankText() {
        Document testDoc = TestDocHelper.createDocument(
                "<a></a>");

        Node nonBlank = testDoc.createTextNode("a");
        assertFalse(NodeOps.nodeIsEmptyText(nonBlank));

        Node blank = testDoc.createTextNode("");
        assertTrue(NodeOps.nodeIsEmptyText(blank));
    }

    /**
     * Test getElementsOfNodeList.
     */
    @Test
    public final void testGetElementsOfNodeList() {
        
        Document testDoc = TestDocHelper.createDocument(
                "<a><b/><c>1</c>23<!--comm--><d attr=\"1\"/></a>");
        Element docEl = testDoc.getDocumentElement();
        NodeList nodeList = docEl.getChildNodes();
        Node[] nodeArray = DOMOps.getElementsOfNodeList(nodeList);
        
        assertEquals(nodeList.getLength(), nodeArray.length);
        for (int i = 0; i < nodeArray.length; i++) {
            assertEquals(nodeArray[i], nodeList.item(i));
        }
        
        assertNull(DOMOps.getElementsOfNodeList(null));
        assertEquals(DOMOps.getElementsOfNodeList(docEl.getFirstChild(
                ).getChildNodes()).length, 0);
        
    }
    
    /**
     * Test getxPath with DTD thing in prolog.
     */
    @Test
    public final void testGetXPathWithDTDProlog() {
        
        Document testDoc = TestDocHelper.createDocument(
                "<!DOCTYPE a [ <!ELEMENT a (#PCDATA)>]><a>text</a>");
        
        //Move to beforeclass method
        XPathFactory xPathFac = XPathFactory.newInstance();
        XPath xpathExpr = xPathFac.newXPath();
 
        testXPathForNode(testDoc, xpathExpr);
    }
    
    /**
     * Test getXPath with comment in prolog.
     */
    @Test
    public final void testGetXPathWithCommentProlog() {
        
        Document testDoc = TestDocHelper.createDocument(
                "<!-- comment --><a>text</a>");
        
        //Move to beforeclass method
        XPathFactory xPathFac = XPathFactory.newInstance();
        XPath xpathExpr = xPathFac.newXPath();
 
        testXPathForNode(testDoc, xpathExpr);
 
    }
    
    /**
     * Test handling of newlines in text nodes.
     */
    @Test
    public final void testNewlineIsNotEmpty() {
        Document testDoc = TestDocHelper.createDocument(
            "<a>text</a>");
        
        Node text1 = testDoc.createTextNode("\r");
        Node text2 = testDoc.createTextNode("\r\n");
        Node text3 = testDoc.createTextNode("\n");
        
        assertFalse(NodeOps.nodeIsEmptyText(text1));
        assertEquals(1, text1.getNodeValue().length());
        assertFalse(NodeOps.nodeIsEmptyText(text2));
        assertEquals(2, text2.getNodeValue().length());
        assertFalse(NodeOps.nodeIsEmptyText(text3));
        assertEquals(1, text3.getNodeValue().length());
    }
    
    /**
     * Test getting XPath with spaced text nodes.
     */
    @Test
    public final void testGetXPathWithSpacedText() {
        
        Document testDoc = TestDocHelper.createDocument(
                "<a>x<b>4</b>y</a>");
        Element docEl = testDoc.getDocumentElement();
        
        //Move to beforeclass method
        XPathFactory xPathFac = XPathFactory.newInstance();
        XPath xpathExpr = xPathFac.newXPath();
 
        testXPathForNode(docEl, xpathExpr);
        testXPathForNode(docEl.getFirstChild(), xpathExpr);
    }   
 
    /**
     * Test getting XPath with spaced text nodes.
     */
    @Test
    public final void testBug() {
        
        Document testDoc = TestDocHelper.createDocument(
                "<p><br/>yyy</p>");
        Element docEl = testDoc.getDocumentElement();
        
        //Move to beforeclass method
        XPathFactory xPathFac = XPathFactory.newInstance();
        XPath xpathExpr = xPathFac.newXPath();
 
        testXPathForNode(docEl, xpathExpr);
        testXPathForNode(docEl.getFirstChild(), xpathExpr);
    }
    
    /**
     * Test getting XPath with CDATA and text.
     */
    @Test
    public final void testCDATAandText() {
        
        Document testDoc = TestDocHelper.createDocument(
                "<p>xxx<![CDATA[yyy]]>zzz</p>");
        Element docEl = testDoc.getDocumentElement();
        
        //Move to beforeclass method
        XPathFactory xPathFac = XPathFactory.newInstance();
        XPath xpathExpr = xPathFac.newXPath();
 
        testXPathForNode(docEl, xpathExpr);
        testXPathForNode(docEl.getFirstChild(), xpathExpr);
    }
}
