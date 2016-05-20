package com.github.tiwindetea.dungeonoflegend.events.requests.inventory;

import com.github.tiwindetea.dungeonoflegend.events.requests.RequestEventType;

/**
 * Created by maxime on 5/6/16.
 */
public class UsageRequestEvent extends InventoryRequestEvent {

	public UsageRequestEvent(long objectId) {
		super(objectId);
	}

	@Override
	public RequestEventType getType() {
		return RequestEventType.USAGE_REQUEST_EVENT;
	}
}
