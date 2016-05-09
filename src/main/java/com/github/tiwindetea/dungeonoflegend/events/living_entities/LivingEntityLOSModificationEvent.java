package com.github.tiwindetea.dungeonoflegend.events.living_entities;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;

import java.util.List;
import java.util.UUID;

/**
 * Created by maxime on 5/6/16.
 */
public class LivingEntityLOSModificationEvent extends LivingEntityEvent {
	public List<Vector2i> modifiedTilesPositions;

	public LivingEntityLOSModificationEvent(UUID entityId, List<Vector2i> modifiedTilesPositions) {
		super(entityId);
		this.modifiedTilesPositions = modifiedTilesPositions;
	}
}
