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

package org.diffxml.patchxml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;

/** 
 * This is a messy bit of old code that doesn't currently work.
 * 
 * Note that it is quite simple in theory; just loop backwards through the
 * operations and reverse each operations:
 * 
 *   - delete becomes insert (requires knowing value & name of node)
 *   - move should easy
 *   - insert becomes delete
 *   - update requires knowing original value
 *   
 *   The problem is extra data is needed for insert and delete
 *   
 * @author Adrian Mouat
 *
 */
public class Reverse
{

public static Document go(Document patch)
{
Node root=patch.getDocumentElement();
NodeIterator ni = ((DocumentTraversal) patch).createNodeIterator
                        (root, NodeFilter.SHOW_ELEMENT, null, false);

Node op=ni.nextNode();
//Check reveresable delta doc

if (! (op.getNodeName().equals("delta") && ((Element)op).getAttribute("reverse_patch").equals("true")) )
	{
	System.err.println("Not a reversable delta");
	System.exit(2);
	}

//Loop through nodes, reversing ops

op=ni.nextNode();

while (op!=null)
	{
	String op_name=op.getNodeName();
	
	if (op_name.equals("insert"))
		{
		//We want an insert to become a delete of the inserted node
		Element del=patch.createElement("delete");	
		
		//XPath of node is parent path plus domcn
		String path=((Element)op).getAttribute("parent");
		path=path+"/node()["+((Element)op).getAttribute("childno")+"]";
		del.setAttribute("node",path);

		//charpos remains the same
		String charpos=((Element)op).getAttribute("charpos");
		if (charpos!="")
			del.setAttribute("charpos",charpos);

		//set length
		if ( ((Element)op).getAttribute("type").equals(""+Node.TEXT_NODE))
			del.setAttribute("length",""+op.getNodeValue().length());			
		//Update node
		op.getParentNode().replaceChild(del,op);	
		}
	else if (op_name.equals("delete"))
		{
		//We want delete to become insert
		Element ins=patch.createElement("insert");
		
		//Parent node is truncated path
		String path=((Element)op).getAttribute("node");
		ins.setAttribute("parent", path.substring(0,path.lastIndexOf("/")));
		//Assume childno is last bit of path
		//MAY NOT ALWAYS WORK!!!
		ins.setAttribute("childno",
			path.substring(path.lastIndexOf("node()[")+7,
					path.lastIndexOf("]")));	
		
		//Leave charpos the same
                String charpos=((Element)op).getAttribute("charpos");
                if (charpos!="")
                        ins.setAttribute("charpos",charpos);

		//Leave nodetype the same
		ins.setAttribute("nodetype",((Element)op).getAttribute("nodetype"));	
		
		//Value remains the same
		ins.appendChild(op.getLastChild());
		op.getParentNode().replaceChild(ins,op);
		}
	else if (op_name.equals("move"))
		{
		//Want move to swap parent and node, old_char and new_char
		
		//Store old values
		String path=((Element)op).getAttribute("node");
		//String par_path=((Element)op).getAttribute("parent");
		
		//Parent node is truncated path
		((Element)op).setAttribute("parent", (path.substring(0,path.lastIndexOf("/")-1)));
		//Assume childno is last bit of path
                //MAY NOT ALWAYS WORK!!!
                ((Element)op).setAttribute("childno",
                        path.substring(path.lastIndexOf("node()["+1),
                                        path.lastIndexOf("]")-1));
		
		//Swap charpos
		String ocharpos=((Element)op).getAttribute("old_charpos");
		String ncharpos=((Element)op).getAttribute("new_charpos");
	
		if (ocharpos!="")
			((Element)op).setAttribute("new_charpos",ocharpos);
	
		if (ncharpos!="")
			((Element)op).setAttribute("old_charpos",ncharpos);
		}
	op=ni.nextNode();
	}
return patch;
}
}
		
		
