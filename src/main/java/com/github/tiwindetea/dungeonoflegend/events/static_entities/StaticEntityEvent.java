package com.github.tiwindetea.dungeonoflegend.events.static_entities;

import com.github.tiwindetea.dungeonoflegend.events.Event;

import java.util.UUID;

/**
 * Created by maxime on 5/3/16.
 */
public abstract class StaticEntityEvent extends Event {
	public UUID entityId;

	public StaticEntityEvent(UUID entityId) {
		this.entityId = entityId;
	}
}
