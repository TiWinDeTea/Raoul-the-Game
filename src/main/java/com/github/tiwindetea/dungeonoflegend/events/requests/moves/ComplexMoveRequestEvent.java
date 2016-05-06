package com.github.tiwindetea.dungeonoflegend.events.requests.moves;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;

/**
 * Created by maxime on 5/6/16.
 */
public class ComplexMoveRequestEvent extends MoveRequestEvent {
	public Vector2i moveArrival;

	public ComplexMoveRequestEvent(Vector2i moveArrival) {
		this.moveArrival = moveArrival;
	}
}
