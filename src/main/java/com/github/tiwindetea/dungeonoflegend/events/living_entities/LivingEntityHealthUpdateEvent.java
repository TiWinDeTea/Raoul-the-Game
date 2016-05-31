package com.github.tiwindetea.dungeonoflegend.events.living_entities;

/**
 * Created by Maxime on 22/05/2016.
 */
public class LivingEntityHealthUpdateEvent extends LivingEntityEvent {
	public double newHealthProportion;
	public int healthDiff;

	public LivingEntityHealthUpdateEvent(long entityId, double newHealthProportion, int healthDiff) {
		super(entityId);
		this.newHealthProportion = newHealthProportion;
		this.healthDiff = healthDiff;
	}

	@Override
	public LivingEntityEventType getSubType() {
		return LivingEntityEventType.LIVING_ENTITY_HEALTH_UPDATE_EVENT;
	}
}
