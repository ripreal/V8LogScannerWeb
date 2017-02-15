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


//Holds data associated with tag
import java.util.Vector;

public class Node
{
public static final int TEXT = 0;
public static final int TAG = 1;

public int type;
public String value;
public int depth;
public int attrCount;
private Vector attributes;

Node()
{
attributes=new Vector();
depth=0;
value="";
type=TAG;
attrCount=0;
}
Node(int t, String n, int d)
{
attributes=new Vector();
depth = d;
value = n;
type = t;
attrCount=0;
}

public void set(int t, String v, int d)
{
depth = d;
value = v;
type = t;
}

public void removeAttrs()
{
attributes.removeAllElements();
attrCount=0;
}

public void addAttr(String n, String v)
{
//System.out.println("Entered addAttr name: " + n + " Value: " +v);
attributes.add(n);
attributes.add(v);
attrCount++;
}

public String getAttrName(int i)
{
if (i>attrCount)
	return null;

return ((String) attributes.get((2*i)));

}

public String getAttrValue(int i)
{
if (i>attrCount)
        return null;
 
return ((String) attributes.get((2*i)+1));
}
}
