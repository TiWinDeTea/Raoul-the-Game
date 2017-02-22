//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.game.living_entities;

/**
 * The type LivingEntityDeletionEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class LivingEntityDeletionEvent extends LivingEntityEvent {
	/**
	 * Instantiates a new LivingEntityDeletionEvent.
	 *
	 * @param entityId the entity id
	 */
	public LivingEntityDeletionEvent(long entityId) {
		super(entityId);
	}

    @Override
    public LivingEntityEventType getSubType() {
        return LivingEntityEventType.LIVING_ENTITY_DELETION_EVENT;
    }
}
