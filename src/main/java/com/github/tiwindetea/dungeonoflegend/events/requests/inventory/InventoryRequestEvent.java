package com.github.tiwindetea.dungeonoflegend.events.requests.inventory;

import com.github.tiwindetea.dungeonoflegend.events.requests.RequestEvent;

/**
 * Created by maxime on 5/6/16.
 */
public abstract class InventoryRequestEvent extends RequestEvent {
	public int objectId;

	public InventoryRequestEvent(int objectId) {
		this.objectId = objectId;
	}
}