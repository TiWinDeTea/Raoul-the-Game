package com.github.tiwindetea.dungeonoflegend.model.events.living_entities;

import com.github.tiwindetea.dungeonoflegend.model.Direction;
import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import com.github.tiwindetea.dungeonoflegend.view.entities.LivingEntityType;

/**
 * Created by maxime on 5/6/16.
 */
public class LivingEntityCreationEvent extends LivingEntityEvent {
	public LivingEntityType type;
	public Vector2i position;
	public Direction direction;

	public LivingEntityCreationEvent(int entityId, LivingEntityType type, Vector2i position, Direction direction) {
		super(entityId);
		this.type = type;
		this.position = position;
		this.direction = direction;
	}
}