package com.github.tiwindetea.dungeonoflegend.events.requests.inventory;

import java.util.UUID;

/**
 * Created by maxime on 5/6/16.
 */
public class UsageRequestEvent extends InventoryRequestEvent {

	public UsageRequestEvent(UUID objectId) {
		super(objectId);
	}
}
