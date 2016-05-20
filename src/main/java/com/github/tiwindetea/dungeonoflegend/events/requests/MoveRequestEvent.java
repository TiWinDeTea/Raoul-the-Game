package com.github.tiwindetea.dungeonoflegend.events.requests;

import com.github.tiwindetea.dungeonoflegend.model.Direction;

/**
 * Created by maxime on 5/6/16.
 */
public class MoveRequestEvent extends RequestEvent {
	public Direction moveDirection;

	public MoveRequestEvent(Direction moveDirection) {
		this.moveDirection = moveDirection;
	}

	@Override
	public RequestEventType getType() {
		return RequestEventType.MOVE_REQUEST_EVENT;
	}
}
