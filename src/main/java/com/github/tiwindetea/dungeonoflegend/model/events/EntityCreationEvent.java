package com.github.tiwindetea.dungeonoflegend.model.events;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import com.github.tiwindetea.dungeonoflegend.view.EntityType;

/**
 * Created by maxime on 5/3/16.
 */
public class EntityCreationEvent extends EntityEvent {
	public EntityType type;
	public Vector2i position;

	public EntityCreationEvent(int entityId, EntityType type, Vector2i position) {
		super(entityId);
		this.type = type;
		this.position = position;
	}
}
