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
//import java.org.diffxml.diffxml.*; 

import org.diffxml.diffxml.DiffXML;
import java.util.StringTokenizer;

public class Depth
{

public int depth;
public String path;

public void parseLine(String line)
{
//System.out.println("Entered dp");
int sp = line.indexOf(' ');
//System.out.println("Found index "+ sp);
String depth_str = line.substring(0, (sp));
//System.out.println("depth_str " + depth_str);
depth=Integer.valueOf(depth_str);
//System.out.println("got line");
path=line.substring((sp+1));

//Want to parse path

StringTokenizer st=new StringTokenizer(path, ",[] ");
path="";
while (st.hasMoreTokens()) 
	{
	
	path=path+"/node()["+st.nextToken()+"]";
	}

}

}
