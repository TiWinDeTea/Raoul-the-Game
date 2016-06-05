//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.listeners.game.players.inventory;

import com.github.tiwindetea.dungeonoflegend.events.players.inventory.InventoryAdditionEvent;

/**
 * The interface InventoryAdditionListener
 * @author Maxime PINARD
 */
public interface InventoryAdditionListener {
	/**
	 * Handler associated to an InventoryAdditionEvent
	 * @param e Event to handle
	 */
	void addInventory(InventoryAdditionEvent e);
}
