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
public class LivingEntityXpUpdateEvent extends LivingEntityEvent {
	private int xpDiff;

	/**
	 * Instantiates a new LivingEntityXpUpdateEvent.
	 *
	 * @param entityId the entity id
	 * @param xpDiff   the xp diff
	 */
	public LivingEntityXpUpdateEvent(long entityId, int xpDiff) {
		super(entityId);
		this.xpDiff = xpDiff;
	}

	@Override
	public LivingEntityEventType getSubType() {
		return LivingEntityEventType.LIVING_ENTITY_XP_UPDATE_EVENT;
	}

	/**
	 * Gets xp diff.
	 *
	 * @return the xp diff
	 */
	public int getXpDiff() {
		return this.xpDiff;
	}
}
