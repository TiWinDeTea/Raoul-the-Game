package com.github.tiwindetea.dungeonoflegend.events.living_entities;

/**
 * Created by maxime on 5/6/16.
 */
public class LivingEntityLOSDefinitionEvent extends LivingEntityEvent {
	public boolean[][] newLOS;

	public LivingEntityLOSDefinitionEvent(int entityId, boolean[][] newLOS) {
		super(entityId);
		this.newLOS = newLOS;
	}
}
