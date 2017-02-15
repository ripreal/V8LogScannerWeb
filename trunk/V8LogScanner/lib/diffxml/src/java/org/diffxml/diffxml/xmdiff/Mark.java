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

package org.diffxml.diffxml.xmdiff;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.*;
import org.w3c.dom.NodeList;
import javax.xml.transform.TransformerException;
import org.w3c.dom.NamedNodeMap;
import java.io.File;

import org.diffxml.diffxml.DOMOps;
import org.diffxml.diffxml.fmes.ChildNumber;
import org.diffxml.diffxml.fmes.NodeOps;
import org.diffxml.diffxml.fmes.delta.DULDelta;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

//Should rewrite as external mem prog.
//All we really need to do is get Recover & pulldiff outputting values then reorder

public class Mark
{

    public static void mark(Document xm, Document doc1, Document doc2)
    {

        XPath xpath = XPathFactory.newInstance().newXPath();

        NodeIterator ni = ((DocumentTraversal) xm).createNodeIterator
        (xm.getDocumentElement(), NodeFilter.SHOW_ELEMENT, null, false);

        Node op=ni.nextNode();

        while (op!=null)
        {
            String op_name=op.getNodeName();
            if (op_name.equals("insert"))
            {

                //Change the insert instruction
                //Get the node

                try 
                {
                    Node node = (Node) xpath.evaluate(
                            ((Element)op).getAttribute("node"),
                            doc2.getDocumentElement(), 
                            XPathConstants.NODE);

                    //Mark the node to be inserted in the tree
                    node.setUserData("insert","true",null);
                }
                catch (XPathExpressionException e) {
                    System.err.println("Create could not find node to insert: "
                            + ((Element)op).getAttribute("node"));}

            }
            else if (op_name.equals("delete"))
            {
                //Change the delete instruction
                try
                {
                    Node node = (Node) xpath.evaluate(
                            ((Element)op).getAttribute("node"),
                            doc1.getDocumentElement(), 
                            XPathConstants.NODE);

                    //Mark node to be deleted
                    if (node==null)
                    {
                        System.err.println("Could not find node: " +((Element)op).getAttribute("node"));
                    }
                    node.setUserData("delete","true",null);
                }
                catch (XPathExpressionException e) {
                    System.err.println("Create could not find node to delete:" + ((Element)op).getAttribute("node"));}

            }
            op=ni.nextNode();
        }

    }

    public static void del(Node n, DULDelta es)
    {
//      Special traversal
//      Don't know name!

//      Go to righmost nodes first
        NodeList kids=n.getChildNodes();
        if (kids!=null)
        {
            //Note that we loop *backward* through kids
            for (int i=(kids.getLength()-1); i>=0; i--)
            {
                del(kids.item(i),es);
            }
        }

        if (n.getUserData("delete")!=null)
        {
            //Output delete
            es.delete(n);
        }	
    }

    public static void ins(Node n, DULDelta es)
    {
//      Think we want an in-order traversal here

//      Check if node is inserted
        if (n.getUserData("insert")!=null)
        {
            //Output insert

            //Get charpos
            ChildNumber cn = new ChildNumber(n);
            int charpos = cn.getXPathCharPos();

            //Get parent path
            Node par=n.getParentNode();
            //Pos par_pos=NodePos.get(par);
            ChildNumber parentNum = new ChildNumber(par);

            //Get XPath childno

            NodeList kids=par.getChildNodes();

            int index=0;
            for (int i=0;i<kids.getLength();i++)
            {
                index++;

                if ( kids.item(i).getNodeType()==Node.TEXT_NODE && (i>0) && kids.item(i-1).getNodeType()==Node.TEXT_NODE)
                    index--;

                if (n.isSameNode(kids.item(i)))
                    break;

            }

            es.insert(n, NodeOps.getXPath(par), index, charpos);

            //Insert any attributes
            NamedNodeMap attrs=n.getAttributes();
            if (attrs!=null)
            {
                if (attrs.getLength()>0)
                {
                    //Get path
                    ChildNumber cn2 = new ChildNumber(n);
                    for (int j=0; j<attrs.getLength(); j++)
                    {
                        es.insert(attrs.item(j), NodeOps.getXPath(n), 0, -1);
                    }
                }
            }
        }

//      Leftmost nodes first 
//      Should be "inorder traversal"
        NodeList babes=n.getChildNodes();
        if (babes!=null)
        {
            //Note that we loop *forward* through kids
            for (int j=0; j<babes.getLength(); j++)
            {
                ins(babes.item(j),es);
            }
        }

    }

    public static void init(File xmf, String f1, String f2)
    {
        try
        {
            DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser1 = fac.newDocumentBuilder();
            DocumentBuilder parser2 = fac.newDocumentBuilder();
            DocumentBuilder parser3 = fac.newDocumentBuilder();
            
            Document xm = parser1.parse(xmf);
            Document doc1 = parser2.parse(f1);
            Document doc2 = parser3.parse(f2);

            mark(xm, doc1, doc2);
            //Make a document for EditScript
            DULDelta es = new DULDelta();

            del(doc1.getDocumentElement(),es);
            ins(doc2.getDocumentElement(),es);

            //Transformer serializer = TransformerFactory.newInstance().newTransformer();
            //serializer.transform(new DOMSource(es), new StreamResult(System.out));
            /*
        Writer writer = new Writer();
                try {
                    writer.setOutput(System.out, "UTF8");
                }
                catch (UnsupportedEncodingException e) {
                    System.err.println("error: Unable to set output. Exiting.");
                    System.exit(1);
                }
         writer.setCanonical(false);
                writer.write(es);
             */
            DOMOps.outputXML(es.getDocument(), System.out);
        }
        catch (Exception e)
        {e.printStackTrace();}
    }	


    public static void main(String[] args)
    {
        //TODO: Use one parser object and remove redundancy with init
        try
        {
            DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser1 = fac.newDocumentBuilder();
            DocumentBuilder parser2 = fac.newDocumentBuilder();
            DocumentBuilder parser3 = fac.newDocumentBuilder();
            Document xm = parser1.parse(args[0]);
            Document doc1 = parser2.parse(args[1]);
            Document doc2 = parser3.parse(args[2]);

            mark(xm, doc1, doc2);
            //Make a document called EditScript
            DULDelta es = new DULDelta();

            del(doc1.getDocumentElement(),es);
            ins(doc2.getDocumentElement(),es);

            //Transformer serializer = TransformerFactory.newInstance().newTransformer();
            //serializer.transform(new DOMSource(es), new StreamResult(System.out));
            /*
	Writer writer = new Writer();
                try {
                    writer.setOutput(System.out, "UTF8");
                }
                catch (UnsupportedEncodingException e) {
                    System.err.println("error: Unable to set output. Exiting.");
                    System.exit(1);
                }
         writer.setCanonical(false);
                writer.write(es);
             */
            DOMOps.outputXML(es.getDocument(), System.out);
        }
        catch (Exception e)
        {e.printStackTrace();}	
    }
}

