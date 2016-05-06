package com.github.tiwindetea.dungeonoflegend.model.events.living_entities;

import com.github.tiwindetea.dungeonoflegend.model.events.Event;

/**
 * Created by maxime on 5/6/16.
 */
public abstract class LivingEntityEvent extends Event {
	public int entityId;

	public LivingEntityEvent(int entityId) {
		this.entityId = entityId;
	}
}
