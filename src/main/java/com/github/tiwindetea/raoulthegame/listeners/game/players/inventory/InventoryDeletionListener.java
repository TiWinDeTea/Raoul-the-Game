//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.listeners.game.players.inventory;

import com.github.tiwindetea.raoulthegame.events.players.inventory.InventoryDeletionEvent;

/**
 * The interface InventoryDeletionListener
 * @author Maxime PINARD
 */
public interface InventoryDeletionListener {
	/**
	 * Handler associated to an InventoryDeletionEvent
	 * @param e Event to handle
	 */
	void deleteInventory(InventoryDeletionEvent e);
}
