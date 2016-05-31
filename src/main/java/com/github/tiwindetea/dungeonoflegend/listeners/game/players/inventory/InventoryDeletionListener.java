package com.github.tiwindetea.dungeonoflegend.listeners.game.players.inventory;

import com.github.tiwindetea.dungeonoflegend.events.players.inventory.InventoryDeletionEvent;

/**
 * Created by maxime on 5/6/16.
 */
public interface InventoryDeletionListener {
	void deleteInventory(InventoryDeletionEvent e);
}
