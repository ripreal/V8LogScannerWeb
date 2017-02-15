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

import static junit.framework.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.diffxml.diffxml.fmes.ParserInitialisationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Helper class for XML tests.
 * 
 * @author Adrian Mouat
 *
 */
public final class TestDocHelper {

    /**
     * Private constructor.
     */
    private TestDocHelper() {
        //SHouldn't be called
    }
    
    /**
     * Create an XML Document from a string of XML.
     * 
     * Calls fail if any exception is thrown.
     * 
     * @param xml The XML to turn into a document.
     * @return A DOM document representing the string.
     */
    public static Document createDocument(final String xml) {
        
        Document ret = null;
        DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
        DOMOps.initParser(fac);
                
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(
                    xml.getBytes("utf-8"));
            ret = fac.newDocumentBuilder().parse(is);
        } catch (UnsupportedEncodingException e) {
            fail("No utf-8 encoder!");
        } catch (ParserConfigurationException e) {
            fail("Error configuring parser: " + e.getMessage());
        } catch (IOException e) {
            fail("Caught IOException: " + e.getMessage());
        } catch (SAXException e) {
            fail("Caught SAXexception: " + e.getMessage());
        }
        
        return ret;

    }
}
