//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.events.living_entities;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;

/**
 * The type LivingEntityMoveEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class LivingEntityMoveEvent extends LivingEntityEvent {
	private Vector2i newPosition;

	/**
	 * Instantiates a new LivingEntityMoveEvent.
	 *
	 * @param entityId    the entity id
	 * @param newPosition the new position
	 */
	public LivingEntityMoveEvent(long entityId, Vector2i newPosition) {
		super(entityId);
		this.newPosition = newPosition;
	}

    @Override
    public LivingEntityEventType getSubType() {
        return LivingEntityEventType.LIVING_ENTITY_MOVE_EVENT;
    }

	/**
	 * Gets new position.
	 *
	 * @return the new position
	 */
	public Vector2i getNewPosition() {
		return this.newPosition;
	}
}
