//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.living_entities;

/**
 * The type LivingEntityManaUpdateEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class LivingEntityManaUpdateEvent extends LivingEntityEvent {
	private int manaDiff;

	/**
	 * Instantiates a new LivingEntityManaUpdateEvent.
	 *
	 * @param entityId the entity id
	 * @param manaDiff the mana diff
	 */
	public LivingEntityManaUpdateEvent(long entityId, int manaDiff) {
		super(entityId);
		this.manaDiff = manaDiff;
	}

	@Override
	public LivingEntityEventType getSubType() {
		return LivingEntityEventType.LIVING_ENTITY_MANA_UPDATE_EVENT;
	}

	/**
	 * Gets mana diff.
	 *
	 * @return the mana diff
	 */
	public int getManaDiff() {
		return this.manaDiff;
	}
}
