//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.living_entities;

/**
 * The type LivingEntityHealthVisibilityEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class LivingEntityHealthVisibilityEvent extends LivingEntityEvent {
	private boolean healthVisibility;

	/**
	 * Instantiates a new LivingEntityHealthVisibilityEvent.
	 *
	 * @param entityId         the entity id
	 * @param healthVisibility the health visibility
	 */
	public LivingEntityHealthVisibilityEvent(long entityId, boolean healthVisibility) {
		super(entityId);
		this.healthVisibility = healthVisibility;
	}

	@Override
	public LivingEntityEventType getSubType() {
		return LivingEntityEventType.LIVING_ENTITY_HEALTH_VISIBILITY_EVENT;
	}

	/**
	 * Is health visibility boolean.
	 *
	 * @return the boolean
	 */
	public boolean isHealthVisibility() {
		return this.healthVisibility;
	}
}
