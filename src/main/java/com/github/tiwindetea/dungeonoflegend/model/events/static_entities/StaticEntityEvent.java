package com.github.tiwindetea.dungeonoflegend.model.events.static_entities;

import com.github.tiwindetea.dungeonoflegend.model.events.Event;

/**
 * Created by maxime on 5/3/16.
 */
public class StaticEntityEvent extends Event {
	public int entityId;

	public StaticEntityEvent(int entityId) {
		this.entityId = entityId;
	}
}
