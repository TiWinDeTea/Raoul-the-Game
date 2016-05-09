package com.github.tiwindetea.dungeonoflegend.events.requests.inventory;

import com.github.tiwindetea.dungeonoflegend.events.requests.RequestEvent;

import java.util.UUID;

/**
 * Created by maxime on 5/6/16.
 */
public abstract class InventoryRequestEvent extends RequestEvent {
	public UUID objectId;

	public InventoryRequestEvent(UUID objectId) {
		this.objectId = objectId;
	}
}
