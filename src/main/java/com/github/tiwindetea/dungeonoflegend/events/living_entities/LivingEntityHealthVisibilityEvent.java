package com.github.tiwindetea.dungeonoflegend.events.living_entities;

/**
 * Created by Maxime on 22/05/2016.
 */
public class LivingEntityHealthVisibilityEvent extends LivingEntityEvent {
	public boolean healthVisibility;

	public LivingEntityHealthVisibilityEvent(long entityId, boolean healthVisibility) {
		super(entityId);
		this.healthVisibility = healthVisibility;
	}

	@Override
	public LivingEntityEventType getSubType() {
		return LivingEntityEventType.LIVING_ENTITY_HEALTH_VISIBILITY_EVENT;
	}
}
