package com.github.tiwindetea.dungeonoflegend.events.living_entities;

import java.util.UUID;

/**
 * Created by maxime on 5/6/16.
 */
public class LivingEntityDeletionEvent extends LivingEntityEvent {
	public LivingEntityDeletionEvent(UUID entityId) {
		super(entityId);
	}
}
