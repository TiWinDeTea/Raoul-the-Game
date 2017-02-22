//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.game.players;

/**
 * The type PlayerNextTickEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class PlayerNextTickEvent extends PlayerEvent {

    /**
     * Instantiates a new PlayerNextTickEvent.
     *
     * @param playerNumber the player number
     */
    public PlayerNextTickEvent(int playerNumber) {
        super(playerNumber);
    }

    @Override
    public PlayerEventType getSubType() {
        return PlayerEventType.PLAYER_NEXT_TICK_EVENT;
    }
}
