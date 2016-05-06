package com.github.tiwindetea.dungeonoflegend.events.requests.moves;

import com.github.tiwindetea.dungeonoflegend.model.Direction;

/**
 * Created by maxime on 5/6/16.
 */
public class SimpleMoveRequestEvent extends MoveRequestEvent {
	public Direction moveDirection;

	public SimpleMoveRequestEvent(Direction moveDirection) {
		this.moveDirection = moveDirection;
	}
}
