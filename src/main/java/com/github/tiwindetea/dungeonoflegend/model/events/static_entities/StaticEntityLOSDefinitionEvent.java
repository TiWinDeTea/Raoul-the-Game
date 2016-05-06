package com.github.tiwindetea.dungeonoflegend.model.events.static_entities;

/**
 * Created by maxime on 5/6/16.
 */
public class StaticEntityLOSDefinitionEvent extends StaticEntityEvent {
	public boolean[][] newLOS;

	public StaticEntityLOSDefinitionEvent(int entityId, boolean[][] newLOS) {
		super(entityId);
		this.newLOS = newLOS;
	}
}
