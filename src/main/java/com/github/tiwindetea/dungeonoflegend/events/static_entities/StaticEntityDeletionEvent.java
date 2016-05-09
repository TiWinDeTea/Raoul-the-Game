package com.github.tiwindetea.dungeonoflegend.events.static_entities;

import java.util.UUID;

/**
 * Created by maxime on 5/3/16.
 */
public class StaticEntityDeletionEvent extends StaticEntityEvent {
	public StaticEntityDeletionEvent(UUID entityId) {
		super(entityId);
	}
}
