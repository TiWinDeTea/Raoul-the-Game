package com.github.tiwindetea.dungeonoflegend.events.players.inventory;

import com.github.tiwindetea.dungeonoflegend.events.players.PlayerEventType;
import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntityType;

/**
 * Created by maxime on 5/6/16.
 */
public class InventoryAdditionEvent extends InventoryEvent {
	public boolean isEquiped;
	public StaticEntityType type;
	public String description;

	public InventoryAdditionEvent(int playerNumber, long objectId, boolean isEquiped, StaticEntityType type, String description) {
		super(playerNumber, objectId);
		this.isEquiped = isEquiped;
		this.type = type;
		this.description = description;
	}

	@Override
	public PlayerEventType getSubType() {
		return PlayerEventType.INVENTORY_ADDITION_EVENT;
	}
}