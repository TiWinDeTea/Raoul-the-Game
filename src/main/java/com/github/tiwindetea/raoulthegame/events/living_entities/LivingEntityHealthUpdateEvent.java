//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.living_entities;

/**
 * The type LivingEntityHealthUpdateEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class LivingEntityHealthUpdateEvent extends LivingEntityEvent {
	private double newHealthProportion;
	private int healthDiff;

	/**
	 * Instantiates a new LivingEntityHealthUpdateEvent.
	 *
	 * @param entityId            the entity id
	 * @param newHealthProportion the new health proportion
	 * @param healthDiff          the health diff
	 */
	public LivingEntityHealthUpdateEvent(long entityId, double newHealthProportion, int healthDiff) {
		super(entityId);
		this.newHealthProportion = newHealthProportion;
		this.healthDiff = healthDiff;
	}

	@Override
	public LivingEntityEventType getSubType() {
		return LivingEntityEventType.LIVING_ENTITY_HEALTH_UPDATE_EVENT;
	}

	/**
	 * Gets new health proportion.
	 *
	 * @return the new health proportion
	 */
	public double getNewHealthProportion() {
		return this.newHealthProportion;
	}

	/**
	 * Gets health diff.
	 *
	 * @return the health diff
	 */
	public int getHealthDiff() {
		return this.healthDiff;
	}
}
