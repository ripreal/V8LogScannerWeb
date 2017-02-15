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

import java.io.RandomAccessFile;
import java.io.File; 
import java.io.FileWriter;
import org.diffxml.diffxml.DiffXML;
public class Recover
{

public Recover(int[][] D, RandomAccessFile fA, int M, RandomAccessFile fB, int N, String f1, String f2, File out) throws Exception
{
//boolean first_i=true, first_j=true;

/*
try
	{
	BLR A_reader=new BLR(fA);
	}
catch (IOException e) {System.out.println("Error reading fA");}

try
	{
	BLR B_reader=new BLR(fB);
	}
catch (IOException e) {System.out.println("Error reading fB");}
*/
//System.out.println("Entered recover");
FileWriter o = new FileWriter(out);
BackwardLineReader A_reader=new BackwardLineReader(fA);
BackwardLineReader B_reader=new BackwardLineReader(fB);
//System.out.println("Set up BLRs");
Depth A = new Depth();
Depth B = new Depth();


Depth old_A = new Depth();
Depth old_B = new Depth();

//System.out.println("About to read and parse lines");
String t=A_reader.readLine();
//System.out.println("read A line=" + t);
A.parseLine(t);
//System.out.println("parsed line");
//A.parseLine(A_reader.readLine());
B.parseLine(B_reader.readLine());
//System.out.println("Parsed lines");
//String i_str = i_reader.readLine();
//String j_str = j_reader.readLine();

//M and N number of nodes in fA and fB respectively

int i=(M-1);
int j=(N-1);

//while ( !A_reader.atStart() && !B_reader.atStart() )
//System.out.println("<?xml version=\"1.0\"?>");
o.write("<?xml version=\"1.0\"?>");
//System.out.println("<delta>");
o.write("<delta>");
while ( i>0 && j>0)
	{
	if (D[i][j] == (D[(i-1)][j] + Costs.costDelete()) 
		&& ( j == N || old_B.depth <= A.depth) ) 
		{
		/*==========
		Apply Delete
		==========*/
		//Modify the path
		//System.out.println("<delete node=\"" + A.path +"\" />");
		o.write("<delete node=\"" + A.path +"\" />");
		old_A=A;
		if (!A_reader.atStart())
			{
			A.parseLine(A_reader.readLine());
			}
		i--;
		}
	else if (D[i][j] == ( D[i][(j-1)] + Costs.costInsert())	
			&& (i == M || old_A.depth <= B.depth) )
		{
		/*==========
		Apply Insert
		==========*/
		//System.out.println("<insert node=\"" + B.path + "\" />"); 
		o.write("<insert node=\"" + B.path + "\" />");
		old_B=B;
		if (!B_reader.atStart())
			{
			B.parseLine(B_reader.readLine());
			}
		j--;
		}
	else
		{//Nodes must be equal
		old_A=A;
		if (!A_reader.atStart())
                	{
			A.parseLine(A_reader.readLine());
			}
		i--;
		//System.out.println("got A");
		old_B=B;
		if (!B_reader.atStart())
			{
                	B.parseLine(B_reader.readLine());
			}
		j--;
		//System.out.println("Exiting Equal");
		}
	}

while ( i>0 )
	{
	/*==========
        Apply Delete
        ==========*/
        //Modify the path
	//System.out.println("<delete node=\"" + A.path +"\" />");
	o.write("<delete node=\"" + A.path +"\" />");
	if (!A_reader.atStart())
		A.parseLine(A_reader.readLine());
	i--;
	}

while ( j>0 )
	{
	/*==========
        Apply Insert
        ==========*/
	//System.out.println("<insert node=\"" + B.path + "\" />");
	o.write("<insert node=\"" + B.path + "\" />");
	if (!B_reader.atStart())
		B.parseLine(B_reader.readLine());
        j--;
	}
//System.out.println("</delta>");
o.write("</delta>");
//o.close();
//Bang this out to Mark and its a job well done
o.flush();
o.close();
Mark.init(out, f1, f2);
//o.close();
}
	
}
		
