package com.github.tiwindetea.dungeonoflegend.events.players.inventory;

import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntityType;

/**
 * Created by maxime on 5/6/16.
 */
public class InventoryAdditionEvent extends InventoryEvent {
	StaticEntityType type;

	public InventoryAdditionEvent(byte playerNumber, int objectId, StaticEntityType type) {
		super(playerNumber, objectId);
		this.type = type;
	}
}
