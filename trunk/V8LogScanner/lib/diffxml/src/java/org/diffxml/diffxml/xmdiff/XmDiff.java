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

//import org.diffxml.diffxml.*; 

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.io.RandomAccessFile;
import java.lang.Exception;
import org.diffxml.diffxml.Diff;
import org.diffxml.diffxml.DiffXML;

import org.w3c.dom.Document;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class XmDiff implements Diff
{
    private final static boolean OUTPUT_ARRAY=false;


    public Document diff(String f1, String f2)
        {
        try
            {
            xmdiff( f1, f2 );
            }

        catch (Exception e)
            {
            System.err.println("XmDiff failed: " + e);
            System.exit(2);
            }

        return null;
        }

    public Document diff(File f1, File f2)
        {
        try 
            { 
            xmdiff( f1.getPath(), f2.getPath() ); 
            }

        catch (Exception e)
            {
            System.err.println("XmDiff failed: " + e);
            System.exit(2);
            }

        return null;
        }

    public void xmdiff(String f1, String f2)
        throws XmlPullParserException, IOException
    {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);

        XmlPullParser doc1 = factory.newPullParser();
	XmlPullParser doc2 = factory.newPullParser();
        //System.out.println("parser implementation class is "+xpp.getClass());

        //pulldiff app = new pulldiff();

        //System.out.println("Parsing file: "+args[i]);
	//String f1= args [0];
	//String f2= args [1];
        doc1.setInput ( new FileReader ( f1 ) );
	doc2.setInput ( new FileReader ( f2 ) );

	//Intermediate temp files
	//Create output file
	File tmp1 = File.createTempFile("xdiff",null,null);
	File tmp2 = File.createTempFile("xdiff",null,null);
	File out = File.createTempFile("xdiff",null,null);
	tmp1.deleteOnExit();
	tmp2.deleteOnExit();
        RandomAccessFile fA = new RandomAccessFile(tmp1, "rw");
	RandomAccessFile fB = new RandomAccessFile(tmp2, "rw");

	//Algorithm mmdiff
	int D[][] = new int[1024][1024];	
	D[0][0]=0;
	
	//Calculate delete costs
	//Returns number of nodes in doc1
        int num_doc1=delcosts(doc1, D, fA);
	
	//Calculate insert costs
	//Returns number of nodes in doc2
	int num_doc2=inscosts(doc2, D, fB);

	//Calculate everything else
	//Need to reset inputs
	//doc1.setInput ( new FileReader ( args [0] ) );
        //doc2.setInput ( new FileReader ( args [1] ) );
	//Need to be able to reset parser so pass filename with parser
	allcosts(doc1, f1, num_doc1, doc2, f2, num_doc2, D);
	
	//Print out D
	//System.out.println("Max int " + Integer.MAX_VALUE + "Max int +1" + (Integer.MAX_VALUE +1));
	if (OUTPUT_ARRAY)
		{
		printarray(D, num_doc1, num_doc2);
		System.out.println();
		}
	try 	{
		Recover mm = new Recover(D, fA, num_doc1, fB, num_doc2, f1, f2, out);
		}
	catch (Exception e){ System.out.println("Recover has barfed");}
	fA.close();
	fB.close();
	
    }


    public int delcosts(XmlPullParser doc1, int[][] D, RandomAccessFile fA)
        throws XmlPullParserException, IOException
    {
	//Currently cost of inserting and deleting is fixed
	//So this effectively does little more than count nodes
	//But different if costd or costi depend on node

	//Also prints out XPaths

	int i=1;
        int eventType = doc1.getEventType();
        ArrayList path = new ArrayList();
	int depth=0; //Easier & quicker to keep our own depth setting
	int tmp;


	//Need to init first entry
	
	//path.add( new Integer(0) );

	//Skip first (hopefully root) tag
	if(eventType == XmlPullParser.START_TAG) 
		{
		doc1.next();
		}
		

        do {
            if(eventType == XmlPullParser.START_TAG) {

		//Deletion costs
		D[i][0]=D[(i-1)][0] + Costs.costDelete(); //+ costd(node);
		i++;

		//Path 
		if ( depth < doc1.getDepth() )
			{
			path.add( new Integer(1) );
			depth ++;
			}
		else if (depth > doc1.getDepth() )
			{
			path.remove((depth-1));
			depth--;
			tmp = ( (Integer) path.get((depth-1))).intValue();
			path.set( (depth-1), (new Integer(++tmp)) );
			}
		else 
			{//depth == doc1.getDepth()
			tmp = ( (Integer) path.get( (depth-1) )).intValue();
                        path.set( (depth-1), (new Integer(++tmp)) );
			}
			
		

		//Output Path
		//System.out.println("Depth=" + depth);
		PrintPath(path, fA, depth);
		
            } else if(eventType == XmlPullParser.TEXT) {

		D[i][0]=D[(i-1)][0] + Costs.costDelete(); // + costd(node);
		i++;
		
		//Do Path Stuff
		//We actually want to consider text nodes at depth getDepth +1

		int txt_depth=doc1.getDepth()+1;
		if ( depth < txt_depth )
                        {
                        path.add( (new Integer(1)) );
                        depth ++;
                        }
                else if (depth > txt_depth )
                        {
                        path.remove((txt_depth-1));
                        depth--;
                        tmp = ( (Integer) path.get( (depth-1) )).intValue();
                        path.set( (depth-1), (new Integer(++tmp)) );
                        }
                else
                        {//depth == doc1.getDepth()
                        tmp = ( (Integer) path.get( (depth-1) )).intValue();
                        path.set( (depth-1), (new Integer(++tmp)) );
                        }
		//System.out.println("Depth=" + depth);
		PrintPath(path, fA, depth);	
	    }
	   
            eventType = doc1.next();
        } while (eventType != XmlPullParser.END_DOCUMENT);

	//System.out.println("Number of nodes: " + (i-1));
	return((i));
    }

    public static void PrintPath(ArrayList path, RandomAccessFile file, int depth) throws IOException
	{

	String st=depth +" " + path.toString() + "\n";
	file.writeBytes(st);
	}

    public int inscosts(XmlPullParser doc2, int[][] D, RandomAccessFile fB)
	throws XmlPullParserException, IOException
	{
	int j=1;
	int eventType = doc2.getEventType();
	ArrayList path = new ArrayList();
        int depth=0; //Easier & quicker to keep our own depth setting
        int tmp;


	//Skip first (hopefully root) tag
        if(eventType == XmlPullParser.START_TAG)
       		{
                doc2.next();
                }

	do {
            if(eventType == XmlPullParser.START_TAG) 
		{
                D[0][j]=D[0][(j-1)] + Costs.costInsert(); //+ costi(node);
                j++;

		//Path
                if ( depth < doc2.getDepth() )
                        {
                        path.add( new Integer(1) );
                        depth ++;
                        }
                else if (depth > doc2.getDepth() )
                        {
                        path.remove((depth-1));
                        depth--;
                        tmp = ( (Integer) path.get((depth-1))).intValue();
                        path.set( (depth-1), (new Integer(++tmp)) );
                        }
                else
                        {//depth == doc1.getDepth()
                        tmp = ( (Integer) path.get( (depth-1) )).intValue();
                        path.set( (depth-1), (new Integer(++tmp)) );
                        }
 
 
 
                //Output Path
                //System.out.println("Depth=" + depth);
                PrintPath(path, fB, depth);
		}
            else if(eventType == XmlPullParser.TEXT) 
		{
                D[0][j]=D[0][(j-1)] + Costs.costInsert(); // + costi(node);
                j++;

		//Do Path Stuff
                //We actually want to consider text nodes at depth getDepth +1
 
                int txt_depth=doc2.getDepth()+1;
                if ( depth < txt_depth )
                        {
                        path.add( (new Integer(1)) );
                        depth ++;
                        }
                else if (depth > txt_depth )
                        {
                        path.remove((txt_depth-1));
                        depth--;
                        tmp = ( (Integer) path.get( (depth-1) )).intValue();
                        path.set( (depth-1), (new Integer(++tmp)) );
                        }
                else
                        {//depth == doc1.getDepth()
                        tmp = ( (Integer) path.get( (depth-1) )).intValue();
                        path.set( (depth-1), (new Integer(++tmp)) );
                        }
                //System.out.println("Depth=" + depth);
                PrintPath(path, fB, depth);

            	}
            eventType = doc2.next();
           } while (eventType != XmlPullParser.END_DOCUMENT);

        //System.out.println("Number of nodes: " + (j-1));
	return((j));
	}


    public void allcosts(XmlPullParser doc1, String file1, int num_doc1, 
		XmlPullParser doc2, String file2, int num_doc2, int[][] D)
	throws XmlPullParserException, IOException
	{
	int m1, m2, m3; 
	int i=0, j=0;
	Node i_node = new Node();
	int eventType1_next;
	int ai_nxt_depth, j_depth;

	//Skip first (hopefully root) tag
	doc1.setInput ( new FileReader ( file1 ) );
	doc1.next();
	/*		
            if(eventType == doc1.START_TAG)
                {
                System.out.println("Got start tag");
                doc.next();
                }
	*/
	//Reset input for doc1 
	
	int eventType1 = doc1.getEventType();
	while (eventType1 != XmlPullParser.START_TAG && eventType1 != XmlPullParser.TEXT && eventType1 != XmlPullParser.END_DOCUMENT)
                eventType1=doc1.next();
	
	eventType1_next=eventType1;

	do {
	   //Reset second loop
	   doc2.setInput ( new FileReader ( file2 ) ); 
	   //Skip root node
	   doc2.next();
	   int eventType2 = doc2.getEventType();
	   
	   eventType1=eventType1_next;
	   //System.out.println("Entered first loop");
	   //Need to save last tags info in structure
	   if (eventType1==XmlPullParser.START_TAG)
		{
	   	i_node.set( Node.TAG, doc1.getName(), doc1.getDepth() );
		i_node.removeAttrs();

		for (int z=0; z<doc1.getAttributeCount(); z++)
			i_node.addAttr(doc1.getAttributeName(z), doc1.getAttributeValue(z) );
		}
	   else if (eventType1==XmlPullParser.TEXT)
		i_node.set( Node.TEXT, doc1.getText(), (doc1.getDepth()+1) );
			
	   //Get next tag 
	   if (eventType1!=XmlPullParser.END_DOCUMENT)
	   	eventType1_next=doc1.next();

	   while (eventType1_next != XmlPullParser.START_TAG && eventType1_next != XmlPullParser.TEXT && eventType1_next != XmlPullParser.END_DOCUMENT)
		{
		//System.out.println("here");
		eventType1_next=doc1.next();
		}

	    if (eventType1==XmlPullParser.END_DOCUMENT)
                {
                ai_nxt_depth=0;
                }
	   else
		ai_nxt_depth=doc1.getDepth();

	   if (eventType1==XmlPullParser.TEXT)
		ai_nxt_depth++;

	   if (eventType1==XmlPullParser.START_TAG || eventType1==XmlPullParser.TEXT)
                {
		i++;
		j=0;
	   	do
			{
			//Only want to process if start node or text
			//System.out.println("Entered second loop");
			while (eventType2 != XmlPullParser.START_TAG && eventType2 != XmlPullParser.TEXT && eventType2 != XmlPullParser.END_DOCUMENT)
                		eventType2=doc2.next();

			if (eventType2==XmlPullParser.START_TAG || eventType2==XmlPullParser.TEXT)	
				{
				j++;
				m1 = Integer.MAX_VALUE;
				m2 = Integer.MAX_VALUE;
				m3 = Integer.MAX_VALUE;

				//Check if nodes match
				boolean matched=false;
				j_depth=doc2.getDepth();
				if (eventType2==XmlPullParser.TEXT)
					j_depth++;
				if ( (eventType2==eventType1) && (i_node.depth==j_depth) )
					{
					if (eventType2==XmlPullParser.START_TAG)
						{
						//Check if names match
						if ( i_node.value.equals(doc2.getName()) )
							{
							//Check if attributes match
							if (i_node.attrCount==0)
								matched=true;
							else //(i_node.getAttr()!=null)
								{
								//String[] doc1_attrs=i_node.getAttr();
								//System.out.println("Matching attributes");
								for (int x=0; x<i_node.attrCount; x++)
								{
								matched=false;
								for (int y=0; y<doc2.getAttributeCount(); y++)
									{
									//Check if names match
									//System.out.println("Matching " + i_node.getAttrName(x) + " to " + doc2.getAttributeName(y));
									if (i_node.getAttrName(x).equals(doc2.getAttributeName(y)))
										{
										if (i_node.getAttrValue(x).equals(doc2.getAttributeValue(y)))	
											{
											matched=true;
											break; //Don't continue trying to match elements
											}
										}
									}
								if (matched==false)	
									break; //Elements not equal
								}
							}
							}	
						}

					else if (eventType2==XmlPullParser.TEXT)
						{
						if ( i_node.value.equals(doc2.getText()) )
							matched=true;
						else
							matched=false;
						}

					//Check if elements matched
                                        if (matched==true)
					    {
					    //System.out.println("Matched");
                                    	    m1=D[(i-1)][(j-1)]; //Cost of update 0 as equal
					    }
					//Otherwise just leave as max int
						
					}
				//To calculate the next costs we also need the depth of the next element so move on now
				//We actually want to increment depth by 1 if text node to put on correct level
				
				int ai_depth=i_node.depth;
				//if (eventType1 == doc1.TEXT)
				//	ai_depth++;

				int bj_depth=doc2.getDepth();
				if (eventType2 == XmlPullParser.TEXT)
					bj_depth++;

				/*
				System.out.println("Entering first tag loop");
				eventType1 = doc1.next();
				while (eventType1 != doc1.START_TAG && eventType1 != doc1.TEXT && eventType1 != doc1.END_DOCUMENT)
					eventType1=doc1.next();
				*/

				//System.out.println("Entering second tag loop");
				eventType2 = doc2.next();
				while (eventType2 != XmlPullParser.START_TAG && eventType2 != XmlPullParser.TEXT && eventType2 != XmlPullParser.END_DOCUMENT)
					eventType2=doc2.next();

				//Again increment any text depths
				
				int bj_nxt_depth=doc2.getDepth();
			 	if (eventType2 == XmlPullParser.TEXT)
                                       bj_nxt_depth++;	

				//ai_next_depth already set

				//int ai_nxt_depth=doc1.getDepth();
				//if (eventType1 == doc1.TEXT)
                                //        ai_nxt_depth++;	
						
				//Get delete cost
				//Not sure if should be num_doc2 or num_doc2-1
				if ( (j==(num_doc2 -1)) || (bj_nxt_depth <= ai_depth) )
					if (D[(i-1)][j] != Integer.MAX_VALUE )
						m2=D[(i-1)][j] + Costs.costDelete(); //should be + cost delete doc1 element

				//Get insert cost
				if ( (i ==( num_doc1 -1)) || (ai_nxt_depth <= bj_depth) )
					if (D[i][(j-1)] != Integer.MAX_VALUE )
						m3=D[i][(j-1)] + Costs.costInsert();

				//System.out.println("ai_depth=" + ai_depth + " ai_nxt_depth=" + ai_nxt_depth + 
				//	" bj_depth=" + bj_depth + " bj_nxt_depth=" + bj_nxt_depth );
				//System.out.println("m1= " + m1 + " m2= " + m2 + " m3= " +m3);
				//Set D to smallest
				if (m1 < m2)
					D[i][j]=m1;
				else
					D[i][j]=m2;

				if (m3 < D[i][j])
					D[i][j]=m3;
				//System.out.println("i= " + i + " j= " + j + " D[i,j]=" + D[i][j] +"\n");
				}	

			//eventType2 = doc2.next();	
			} while (eventType2 != XmlPullParser.END_DOCUMENT);		
		}
	   //eventType1 = doc1.next();
	   } while (eventType1 != XmlPullParser.END_DOCUMENT);
	}

public void printarray(int[][] D, int size1, int size2)
	{
	for (int i=0;i<size1;i++)
		{
		System.out.println("");
		for (int j=0;j<size2;j++)
			{
			if (D[i][j]==Integer.MAX_VALUE)
				System.out.print("I\t");
			else
				System.out.print(D[i][j] + "\t");
			}
		}
	}
	

}

