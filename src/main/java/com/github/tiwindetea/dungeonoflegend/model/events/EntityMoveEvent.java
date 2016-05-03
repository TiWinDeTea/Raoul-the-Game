package com.github.tiwindetea.dungeonoflegend.model.events;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;

/**
 * Created by maxime on 5/2/16.
 */
public class EntityMoveEvent extends EntityEvent {
	public Vector2i oldPosition;
	public Vector2i newPosition;

	public EntityMoveEvent(int entityId, Vector2i oldPosition, Vector2i newPosition) {
		super(entityId);
		this.oldPosition = oldPosition;
		this.newPosition = newPosition;
	}
}
