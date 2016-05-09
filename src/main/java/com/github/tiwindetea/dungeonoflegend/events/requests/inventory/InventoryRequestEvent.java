package com.github.tiwindetea.dungeonoflegend.events.requests.inventory;

import com.github.tiwindetea.dungeonoflegend.events.requests.RequestEvent;

/**
 * Created by maxime on 5/6/16.
 */
public abstract class InventoryRequestEvent extends RequestEvent {
	public long objectId;

	public InventoryRequestEvent(long objectId) {
		this.objectId = objectId;
	}
}
