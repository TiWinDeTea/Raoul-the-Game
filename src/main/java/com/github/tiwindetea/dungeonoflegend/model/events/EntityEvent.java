package com.github.tiwindetea.dungeonoflegend.model.events;

/**
 * Created by maxime on 5/3/16.
 */
public class EntityEvent extends Event {
	public int entityId;

	public EntityEvent(int entityId) {
		this.entityId = entityId;
	}
}
