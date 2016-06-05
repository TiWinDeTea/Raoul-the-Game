//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.events.static_entities;

import com.github.tiwindetea.dungeonoflegend.events.Event;
import com.github.tiwindetea.dungeonoflegend.events.EventType;

/**
 * The type StaticEntityEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public abstract class StaticEntityEvent extends Event {
	private long entityId;

	/**
	 * Instantiates a new StaticEntityEvent.
	 *
	 * @param entityId the entity id
	 */
	public StaticEntityEvent(long entityId) {
		this.entityId = entityId;
	}

	public EventType getType() {
		return EventType.STATIC_ENTITY_EVENT;
	}

	/**
	 * Gets sub type.
	 *
	 * @return the sub type
	 */
	public abstract StaticEntityEventType getSubType();

	/**
	 * Gets entity id.
	 *
	 * @return the entity id
	 */
	public long getEntityId() {
		return this.entityId;
	}
}
