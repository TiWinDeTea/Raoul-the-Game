//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.game.players;

import com.github.tiwindetea.raoulthegame.events.Event;
import com.github.tiwindetea.raoulthegame.events.EventType;

/**
 * The type PlayerEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public abstract class PlayerEvent extends Event {
	private int playerNumber;

	/**
	 * Instantiates a new PlayerEvent.
	 *
	 * @param playerNumber the player number
	 */
	public PlayerEvent(int playerNumber) {
		this.playerNumber = playerNumber;
	}

	@Override
	public EventType getType() {
		return EventType.PLAYER_EVENT;
	}

	/**
	 * Gets sub type.
	 *
	 * @return the sub type
	 */
	public abstract PlayerEventType getSubType();

	/**
	 * Gets player number.
	 *
	 * @return the player number
	 */
	public int getPlayerNumber() {
		return this.playerNumber;
	}
}
