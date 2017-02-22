//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.game.players.inventory;

import com.github.tiwindetea.raoulthegame.events.game.players.PlayerEventType;

/**
 * The type InventoryDeletionEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class InventoryDeletionEvent extends InventoryEvent {
	/**
	 * Instantiates a new InventoryDeletionEvent.
	 *
	 * @param playerNumber the player number
	 * @param objectId     the object id
	 */
	public InventoryDeletionEvent(int playerNumber, long objectId) {
		super(playerNumber, objectId);
	}

	@Override
	public PlayerEventType getSubType() {
		return PlayerEventType.INVENTORY_DELETION_EVENT;
	}
}
