//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.game.players.inventory;

import com.github.tiwindetea.raoulthegame.events.game.players.PlayerEvent;

/**
 * The type InventoryEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public abstract class InventoryEvent extends PlayerEvent {
	private long objectId;

	/**
	 * Instantiates a new InventoryEvent.
	 *
	 * @param playerNumber the player number
	 * @param objectId     the object id
	 */
	public InventoryEvent(int playerNumber, long objectId) {
		super(playerNumber);
		this.objectId = objectId;
	}

	/**
	 * Gets object id.
	 *
	 * @return the object id
	 */
	public long getObjectId() {
		return this.objectId;
	}
}
