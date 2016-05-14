//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.model;

/**
 * Node. (path finding)
 *
 * @author Lucas LAZARE
 */
public class Node {

    public int heuristic;
    public int distance;
    public Node parent;
    public Vector2i pos;

    /**
     * Instantiates a new Node. (for comparison purposes)
     *
     * @param pos the pos
     */
    public Node(Vector2i pos) {
        this.pos = pos;
    }

    /**
     * Instantiates a new Node.
     *
     * @param pos    the pos
     * @param parent the parent
     */
    public Node(Vector2i pos, Node parent) {
        this.pos = pos;
        this.parent = parent;
    }

    /**
     * Instantiates a new Node.
     *
     * @param pos       the pos
     * @param heuristic the heuristic
     * @param distance  the distance
     * @param parent    the parent
     */
    public Node(Vector2i pos, int heuristic, int distance, Node parent) {
        this.pos = pos;
        this.parent = parent;
        this.distance = distance;
        this.heuristic = heuristic;
    }

    public boolean equals(Object o) {
        /* asserting o to be a Node */
        return this.equals((Node) o);
    }

    /**
     * Equals boolean.
     *
     * @param n a Node
     * @return true if both nodes are at the same position
     */
    public boolean equals(Node n) {
        return n.pos.x == this.pos.x && n.pos.y == this.pos.y;
    }

    /**
     * Sum.
     *
     * @return this.distance + this.heuristic (global value)
     */
    public int sum() {
        return this.distance + this.heuristic;
    }
}
