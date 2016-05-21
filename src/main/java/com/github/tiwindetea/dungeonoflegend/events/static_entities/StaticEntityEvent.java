package com.github.tiwindetea.dungeonoflegend.events.static_entities;

import com.github.tiwindetea.dungeonoflegend.events.Event;
import com.github.tiwindetea.dungeonoflegend.events.EventType;

/**
 * Created by maxime on 5/3/16.
 */
public abstract class StaticEntityEvent extends Event {
	public long entityId;

	public StaticEntityEvent(long entityId) {
		this.entityId = entityId;
	}

	public EventType getType() {
		return EventType.STATIC_ENTITY_EVENT;
	}

	public abstract StaticEntityEventType getSubType();
}
