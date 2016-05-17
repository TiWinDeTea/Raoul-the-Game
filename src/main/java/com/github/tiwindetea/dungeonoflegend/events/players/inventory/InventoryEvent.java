package com.github.tiwindetea.dungeonoflegend.events.players.inventory;

import com.github.tiwindetea.dungeonoflegend.events.players.PlayerEvent;

/**
 * Created by maxime on 5/6/16.
 */
public abstract class InventoryEvent extends PlayerEvent {
	public long objectId;

	public InventoryEvent(int playerNumber, long objectId) {
		super(playerNumber);
		this.objectId = objectId;
	}
}
