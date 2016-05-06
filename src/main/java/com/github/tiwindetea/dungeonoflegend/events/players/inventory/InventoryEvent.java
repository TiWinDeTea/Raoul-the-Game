package com.github.tiwindetea.dungeonoflegend.events.players.inventory;

import com.github.tiwindetea.dungeonoflegend.events.players.PlayerEvent;

/**
 * Created by maxime on 5/6/16.
 */
public abstract class InventoryEvent extends PlayerEvent {
	int ObjectId;

	public InventoryEvent(byte playerNumber, int objectId) {
		super(playerNumber);
		this.ObjectId = objectId;
	}
}
