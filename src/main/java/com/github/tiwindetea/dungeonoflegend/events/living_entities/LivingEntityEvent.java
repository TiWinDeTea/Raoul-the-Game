package com.github.tiwindetea.dungeonoflegend.events.living_entities;

import com.github.tiwindetea.dungeonoflegend.events.Event;

import java.util.UUID;

/**
 * Created by maxime on 5/6/16.
 */
public abstract class LivingEntityEvent extends Event {
	public UUID entityId;

	public LivingEntityEvent(UUID entityId) {
		this.entityId = entityId;
	}
}
