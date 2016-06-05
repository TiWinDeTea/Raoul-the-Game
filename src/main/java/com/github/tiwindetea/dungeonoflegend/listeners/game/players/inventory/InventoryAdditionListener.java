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
 * Created by maxime on 5/6/16.
 */
public interface InventoryAdditionListener {
	void addInventory(InventoryAdditionEvent e);
}
