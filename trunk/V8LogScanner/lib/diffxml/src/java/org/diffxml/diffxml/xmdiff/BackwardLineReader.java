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

//Backward Line Reader
//Reads a line of text forward, but starts at end of file

import java.io.RandomAccessFile;
import java.io.IOException;

public class BackwardLineReader
{
private long pos;
private RandomAccessFile file;

BackwardLineReader(RandomAccessFile _file) throws IOException
{
file=_file;
//maybe file.length() -1
pos=file.length()-2;
}

public String readLine() throws IOException
{
String line="";
char tmp;
//System.out.println("in readline pos="+ pos);
if (pos >= 0)
	file.seek(pos);
else
	{
	System.out.println("Read before start of file");
	return "";
	}
tmp=(char) file.readByte();
//System.out.println("Char " + tmp);
while (tmp != '\n' && pos >=0)
	{
	line=tmp+line;    
	pos--;
	if (pos >=0)
		file.seek(pos);
	tmp=(char) file.readByte();
	//line=tmp+line;
	}

if (line == "")
	{
	System.out.println("BackwardLineReader didn't find line");
	return null;
	}
pos--;
//System.out.println("Exiting readline line="+line);
return line;
}

public boolean atStart()
{
if (pos <= 0 )
	return true;

return false;
}
}
