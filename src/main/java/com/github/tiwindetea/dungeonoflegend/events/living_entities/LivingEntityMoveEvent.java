package com.github.tiwindetea.dungeonoflegend.events.living_entities;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;

/**
 * Created by maxime on 5/6/16.
 */
public class LivingEntityMoveEvent extends LivingEntityEvent {
	public Vector2i newPosition;

	public LivingEntityMoveEvent(int entityId, Vector2i newPosition) {
		super(entityId);
		this.newPosition = newPosition;
	}
}
