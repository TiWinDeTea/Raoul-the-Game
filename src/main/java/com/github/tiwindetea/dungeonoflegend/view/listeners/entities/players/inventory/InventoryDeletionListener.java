package com.github.tiwindetea.dungeonoflegend.view.listeners.entities.players.inventory;

import com.github.tiwindetea.dungeonoflegend.model.events.players.inventory.InventoryDeletionEvent;

/**
 * Created by maxime on 5/6/16.
 */
public interface InventoryDeletionListener {
	void deleteInventory(InventoryDeletionEvent e);
}
