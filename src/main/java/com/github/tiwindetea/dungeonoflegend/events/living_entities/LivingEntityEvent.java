package com.github.tiwindetea.dungeonoflegend.events.living_entities;

import com.github.tiwindetea.dungeonoflegend.events.Event;

/**
 * Created by maxime on 5/6/16.
 */
public abstract class LivingEntityEvent extends Event {
	public long entityId;

	public LivingEntityEvent(long entityId) {
		this.entityId = entityId;
	}
}
