package com.github.tiwindetea.dungeonoflegend.events.requests.inventory;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;

import java.util.UUID;

/**
 * Created by maxime on 5/6/16.
 */
public class DropRequestEvent extends InventoryRequestEvent {
	public Vector2i dropPosition;

	public DropRequestEvent(UUID objectId, Vector2i dropPosition) {
		super(objectId);
		this.dropPosition = dropPosition;
	}
}
