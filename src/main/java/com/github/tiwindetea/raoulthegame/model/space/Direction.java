//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.model.space;

import java.util.Random;

/**
 * Direction
 *
 * @author Lucas LAZARE
 */
public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    public static Direction[] values = Direction.values();

    public static Direction random() {
        return Direction.values[new Random().nextInt(Direction.values.length)];
    }
}
