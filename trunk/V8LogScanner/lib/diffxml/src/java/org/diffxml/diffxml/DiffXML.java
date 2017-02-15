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

package org.diffxml.diffxml;

import java.io.File;
import java.io.IOException;

import org.w3c.dom.Document;


/**
 * DiffXML finds the differences between 2 XML files.
 *
 * This class takes input from the command line and starts the
 * differencing algorithm.
 *
 * @author      Adrian Mouat
 */

public final class DiffXML {

    /** Version number. **/
    private static final String VERSION = "0.96 BETA";

    /** First file to be differenced. **/
    private static File mFile1;

    /** Second file to be differenced. **/
    private static File mFile2;

    /**
     * Private constructor - shouldn't be called.
     */
    private DiffXML() {
        //Private constructor - shouldn't be called
    }

    /**
     * Checks and interprets the command line arguments.
     *
     * Code is based on Sun standard code for handling arguments.
     *
     * @param args    An array of the command line arguments
     */
    private static void parseArgs(final String[] args) {
        
        int argNo = 0;
        String currentArg;
        char flag;

        while (argNo < args.length && args[argNo].startsWith("-")) {
            currentArg = args[argNo++];

            /* Normalize multiple dashes
               I don't understand point in differentiating between 1
               and 2 dashes.
               We allow 2 to mimic diff util, but compress to 1 */

            if (currentArg.startsWith("--")) {
                currentArg = currentArg.substring(1);
            }

            //"wordy" arguments
            if (currentArg.equals("-brief")) {
                DiffFactory.setBrief(true);
            } else if (currentArg.equals("-debug")) {
                DiffFactory.setDebug(true);
            } else if (currentArg.equals("-version")) {
                printVersionAndExit();
            } else if (currentArg.equals("-help")) {
                printHelpAndExit();
            } else if (currentArg.equals("-fmes")) {
                DiffFactory.setFMES(true);
            } else if (currentArg.equals("-dul")) {
                DiffFactory.setDUL(true);
            } else {

                //(series of) flag arguments
                for (int charNo = 1; charNo < currentArg.length(); charNo++) {
                    flag = currentArg.charAt(charNo);
                    switch (flag) {
                        case 'q':
                            DiffFactory.setBrief(true);
                            break;
                        case 'V':
                            printVersionAndExit();
                            break;
                        case 'h':
                            printHelpAndExit();
                            break;
                        case 'f':
                            DiffFactory.setFMES(true);
                            break;
                        case 'D':
                            DiffFactory.setDUL(true);
                            break;

                        default:
                            System.err.println("diffxml: illegal option "
                                    + flag);
                            printUsage();
                            break;
                    }
                }
            }
        }

        if ((argNo + 2) != args.length) {
            //Not given 2 files on input
            printUsage();
        }

        mFile1 = new File(args[argNo]);
        mFile2 = new File(args[++argNo]);
    }

    /**
     * Outputs usage message to standard error.
     */
    public static void printUsage() {
        System.err.println("Usage: diffxml [OPTION]... XMLFILE1 XMLFILE2");
        System.exit(2);
    }

    /**
     * Outputs brief help message to standard out and exits.
     */

    public static void printHelpAndExit() {
        System.out.print("\nUsage: diffxml [OPTION]... XMLFILE1 XMLFILE2\n\n " +
                "Find the differences between two XML files.\n\n" +
                "--brief  -q  Report only if files differ, don't output the " +
                "delta.\n" +
                "--version  -V  Output version number of program.\n" +
                "--help  -h  Output this help.\n");

        System.out.print("\nThis product includes software developed by the " +
                "Indiana University Extreme! Lab " +
        "(http://www.extreme.indiana.edu/).\n\n");

        System.exit(0);
    }

    /**
     * Outputs the current version of diffxml to standard out.
     */
    public static void printVersionAndExit() {
        System.out.println("diffxml Version " + VERSION + "\n");
        System.out.print("\nThis product includes software developed by the" +
                " Indiana University Extreme! Lab " +
        "(http://www.extreme.indiana.edu/).\n");
        System.exit(0);
    }

    /**
     * Main method. Takes command line arguments, parses them and performs diff.
     *
     * @param args Command line arguments. See printUsageAndExit() for details.
     */
    public static void main(final String[] args) {

        //Set options - instantiates _file1 and _file2
        parseArgs(args);

        //Check files
        if (!mFile1.exists()) {
            System.err.println("Could not find file: "
                    + mFile1.getAbsolutePath());
            System.exit(2);
        }
        if (!mFile2.exists()) {
            System.err.println("Could not find file: "
                    + mFile2.getAbsolutePath());
            System.exit(2);
        }
        
        Diff diffInstance = DiffFactory.createDiff();
        
        Document delta = null;
        try {
            delta = diffInstance.diff(mFile1, mFile2);
        } catch (DiffException e) {
            System.err.println("An error occured:\n" + e.getMessage());
            System.exit(2);
        }
        
        //Output XML if appropriate

        //Documents differ if there are any child nodes in the doc.
        boolean differ = delta.getDocumentElement().hasChildNodes();

        if (DiffFactory.isBrief()) {
            //If in brief mode, don't output delta, only whether files differ
            if (differ) {
                System.out.println("XML documents " + mFile1 + " and "
                        + mFile2 + " differ");
            }
        } else {
            try {
                DOMOps.outputXMLIndented(delta, System.out);
                System.out.println();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }

        if (differ) {
            System.exit(1);
        } else {
            System.exit(0);
        }
    }
}
