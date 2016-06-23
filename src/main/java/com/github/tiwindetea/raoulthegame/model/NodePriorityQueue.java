//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.model;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * NodePriorityQueue (path finding)
 *
 * @author Lucas LAZARE
 */
public class NodePriorityQueue extends PriorityQueue<Node> {

    /**
     * Instantiates a new Node priority queue.
     *
     * @param comparator the comparator
     */
    public NodePriorityQueue(Comparator<? super Node> comparator) {
        super(comparator);
    }

    /**
     * Finds a node.
     *
     * @param node_position the node position
     * @return null if no node was found at the given position, the node at the given position otherwise
     */
    public Node find(Vector2i node_position) {
        Node iter = new Node(new Vector2i(-1, -1));
        Node lookingFor = new Node(node_position);
        Iterator<Node> it = super.iterator();
        while (!lookingFor.equals(iter) && it.hasNext()) {
            iter = it.next();
        }
        if (lookingFor.equals(iter)) {
            return iter;
        } else {
            return null;
        }
    }
}
