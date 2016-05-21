package com.github.tiwindetea.dungeonoflegend.events.living_entities;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;

import java.util.List;

/**
 * Created by maxime on 5/6/16.
 */
public class LivingEntityLOSModificationEvent extends LivingEntityEvent {
	public List<Vector2i> modifiedTilesPositions;

	public LivingEntityLOSModificationEvent(long entityId, List<Vector2i> modifiedTilesPositions) {
		super(entityId);
		this.modifiedTilesPositions = modifiedTilesPositions;
	}

    @Override
    public LivingEntityEventType getSubType() {
        return LivingEntityEventType.LIVING_ENTITY_LOS_MODIFICATION_EVENT;
    }
}
