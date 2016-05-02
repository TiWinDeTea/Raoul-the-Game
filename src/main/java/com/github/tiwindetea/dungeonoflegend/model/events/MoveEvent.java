package com.github.tiwindetea.dungeonoflegend.model.events;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;

/**
 * Created by maxime on 5/2/16.
 */
public class MoveEvent extends Event {
	int entityId;
	public Vector2i oldPosition;
	public Vector2i newPosition;

	public MoveEvent(int entityId, Vector2i oldPosition, Vector2i newPosition) {
		this.entityId = entityId;
		this.oldPosition = oldPosition;
		this.newPosition = newPosition;
	}
}
