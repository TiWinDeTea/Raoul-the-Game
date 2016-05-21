package com.github.tiwindetea.dungeonoflegend.events.players.inventory;

import com.github.tiwindetea.dungeonoflegend.events.players.PlayerEventType;

/**
 * Created by maxime on 5/6/16.
 */
public class InventoryDeletionEvent extends InventoryEvent {
	public InventoryDeletionEvent(int playerNumber, long objectId) {
		super(playerNumber, objectId);
	}

	@Override
	public PlayerEventType getSubType() {
		return PlayerEventType.INVENTORY_DELETION_EVENT;
	}
}
