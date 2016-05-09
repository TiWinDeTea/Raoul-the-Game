package com.github.tiwindetea.dungeonoflegend.events.players.inventory;

/**
 * Created by maxime on 5/6/16.
 */
public class InventoryDeletionEvent extends InventoryEvent {
	public InventoryDeletionEvent(int playerNumber, long objectId) {
		super(playerNumber, objectId);
	}
}
