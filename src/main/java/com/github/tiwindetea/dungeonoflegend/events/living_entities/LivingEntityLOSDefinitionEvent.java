package com.github.tiwindetea.dungeonoflegend.events.living_entities;

import java.util.UUID;

/**
 * Created by maxime on 5/6/16.
 */
public class LivingEntityLOSDefinitionEvent extends LivingEntityEvent {
	public boolean[][] newLOS;

	public LivingEntityLOSDefinitionEvent(UUID entityId, boolean[][] newLOS) {
		super(entityId);
		this.newLOS = newLOS;
	}
}
