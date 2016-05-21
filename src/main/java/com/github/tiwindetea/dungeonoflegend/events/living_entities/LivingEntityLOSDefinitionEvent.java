package com.github.tiwindetea.dungeonoflegend.events.living_entities;

/**
 * Created by maxime on 5/6/16.
 */
public class LivingEntityLOSDefinitionEvent extends LivingEntityEvent {
	public boolean[][] newLOS;

	public LivingEntityLOSDefinitionEvent(long entityId, boolean[][] newLOS) {
		super(entityId);
		this.newLOS = newLOS;
	}

    @Override
    public LivingEntityEventType getSubType() {
        return LivingEntityEventType.LIVING_ENTITY_LOS_DEFINITION_EVENT;
    }
}
