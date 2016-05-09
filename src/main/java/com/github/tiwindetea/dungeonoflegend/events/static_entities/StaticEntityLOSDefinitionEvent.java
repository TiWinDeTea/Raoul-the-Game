package com.github.tiwindetea.dungeonoflegend.events.static_entities;

import java.util.UUID;

/**
 * Created by maxime on 5/6/16.
 */
public class StaticEntityLOSDefinitionEvent extends StaticEntityEvent {
	public boolean[][] newLOS;

	public StaticEntityLOSDefinitionEvent(UUID entityId, boolean[][] newLOS) {
		super(entityId);
		this.newLOS = newLOS;
	}
}
