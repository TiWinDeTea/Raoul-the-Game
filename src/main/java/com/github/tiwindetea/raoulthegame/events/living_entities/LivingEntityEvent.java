//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.living_entities;

import com.github.tiwindetea.raoulthegame.events.Event;
import com.github.tiwindetea.raoulthegame.events.EventType;

/**
 * The type LivingEntityEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public abstract class LivingEntityEvent extends Event {
	private long entityId;

	/**
	 * Instantiates a new LivingEntityEvent.
	 *
	 * @param entityId the entity id
	 */
	public LivingEntityEvent(long entityId) {
		this.entityId = entityId;
	}
    public EventType getType() {
        return EventType.LIVING_ENTITY_EVENT;
    }

	/**
	 * Gets sub type.
	 *
	 * @return the sub type
	 */
	public abstract LivingEntityEventType getSubType();

	/**
	 * Gets entity id.
	 *
	 * @return the entity id
	 */
	public long getEntityId() {
		return this.entityId;
	}
}
