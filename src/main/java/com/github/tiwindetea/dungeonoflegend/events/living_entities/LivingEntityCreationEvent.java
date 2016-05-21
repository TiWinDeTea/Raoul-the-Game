package com.github.tiwindetea.dungeonoflegend.events.living_entities;

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
	public String description;

	public LivingEntityCreationEvent(long entityId, LivingEntityType type, Vector2i position, Direction direction, String description) {
		super(entityId);
		this.type = type;
		this.position = position;
		this.direction = direction;
		this.description = description;
	}
    @Override
    public LivingEntityEventType getSubType() {
        return LivingEntityEventType.LIVING_ENTITY_CREATION_EVENT;
    }
}
