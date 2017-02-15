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

package org.diffxml.diffxml.fmes;

import java.util.Comparator;

/**
 * Comparator for NodeInfo objects.
 * 
 * Based on reverse depth.
 */

class NodeDepthComparator implements Comparator<NodeDepth> {

    /**
     * Compares two NodeDepth objects.
     * 
     * Stores in reverse order of depth.
     * *NOT* consistent with equals; we only care about depth and it is
     * expensive to compare Nodes.
     * 
     * TODO: Consider making serializable, in case TreeSet is serialized.
     * TODO: Check if need to enforce Document Position ordering
     * 
     * @param nodeInfo1
     *            First NodeInfo object
     * @param nodeInfo2
     *            Second NodeInfo object
     * @return Negative if nodeInfo1 is at a greater depth than nodeInfo2, 
     *         positive if smaller depth, 0 if same depth 
     */
    public final int compare(final NodeDepth nodeInfo1, 
            final NodeDepth nodeInfo2) {

            return nodeInfo2.getDepth() - nodeInfo1.getDepth();
    }
}
