package com.github.tiwindetea.dungeonoflegend.model.events.living_entities;

/**
 * Created by maxime on 5/6/16.
 */
public class LivingEntityDeletionEvent extends LivingEntityEvent {
	public LivingEntityDeletionEvent(int entityId) {
		super(entityId);
	}
}
