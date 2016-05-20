package com.github.tiwindetea.dungeonoflegend.events.requests.inventory;

import com.github.tiwindetea.dungeonoflegend.events.requests.RequestEventType;
import com.github.tiwindetea.dungeonoflegend.model.Vector2i;

/**
 * Created by maxime on 5/6/16.
 */
public class DropRequestEvent extends InventoryRequestEvent {
	public Vector2i dropPosition;

	public DropRequestEvent(long objectId, Vector2i dropPosition) {
		super(objectId);
		this.dropPosition = dropPosition;
	}

	@Override
	public RequestEventType getType() {
		return RequestEventType.DROP_REQUEST_EVENT;
	}
}
