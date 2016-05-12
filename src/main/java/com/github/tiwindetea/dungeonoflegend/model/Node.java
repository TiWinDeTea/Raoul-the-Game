//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.model;

/**
 * Created by organic-code on 5/5/16.
 */
public class Node {
    public int heuristic;
    public int distance;
    public Node parent;
    public Vector2i pos;

    public Node(Vector2i pos) {
        this.pos = pos;
    }

    public Node(Vector2i pos, Node parent) {
        this.pos = pos;
        this.parent = parent;
    }

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

    public boolean equals(Node n) {
        return n.pos.x == this.pos.x && n.pos.y == this.pos.y;
    }

    public int sum() {
        return this.distance + this.heuristic;
    }
}
