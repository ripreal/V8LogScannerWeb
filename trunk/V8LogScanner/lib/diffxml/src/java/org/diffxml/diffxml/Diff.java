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

import org.w3c.dom.Document;

/**
 * This is the interface all differencing algorithms should implement.
 *
 * The class defines two diff methods for handling File and String input.
 * 
 * The result is returned as an XML document, which unfortunately means DOM
 * needs to be used.
 * 
 * TODO: Add a method that takes URLs
 *
 * @author    Adrian Mouat
 */

public interface Diff {

    /**
     * Differences two files.
     *
     * Returns a patch document representing the differences. 
     * 
     * The document will have only one empty element if the documents are
     * identical.
     *
     * TODO: Consider changing the return type to an interface supporting
     * printing to stream and a boolean areIdentical method.
     *
     * @param f1    Original file
     * @param f2    Modified file
     * @return Document An XML document containing the differences between the 
     *                  2 files.
     * @throws DiffException If something goes wrong
     */

    Document diff(final File f1, final File f2) throws DiffException;


}

