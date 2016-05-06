package com.github.tiwindetea.dungeonoflegend.model.events.living_entities;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;

import java.util.List;

/**
 * Created by maxime on 5/6/16.
 */
public class LivingEntityLOSModificationEvent {
	public List<Vector2i> modifiedTilesPositions;

	public LivingEntityLOSModificationEvent(List<Vector2i> modifiedTilesPositions) {
		this.modifiedTilesPositions = modifiedTilesPositions;
	}
}
