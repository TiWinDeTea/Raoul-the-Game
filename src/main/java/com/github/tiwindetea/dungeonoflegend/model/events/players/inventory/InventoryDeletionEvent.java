package com.github.tiwindetea.dungeonoflegend.model.events.players.inventory;

/**
 * Created by maxime on 5/6/16.
 */
public class InventoryDeletionEvent extends InventoryEvent {
	public InventoryDeletionEvent(byte playerNumber, int objectId) {
		super(playerNumber, objectId);
	}
}
