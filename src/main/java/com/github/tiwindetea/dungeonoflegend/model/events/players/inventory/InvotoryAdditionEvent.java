package com.github.tiwindetea.dungeonoflegend.model.events.players.inventory;

import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntityType;

/**
 * Created by maxime on 5/6/16.
 */
public class InvotoryAdditionEvent extends InventoryEvent {
	StaticEntityType type;

	public InvotoryAdditionEvent(byte playerNumber, int objectId, StaticEntityType type) {
		super(playerNumber, objectId);
		this.type = type;
	}
}
