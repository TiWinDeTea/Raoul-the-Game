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
 * Created by maxime on 5/3/16.
 */
public abstract class StaticEntityEvent extends Event {
	public long entityId;

	public StaticEntityEvent(long entityId) {
		this.entityId = entityId;
	}

	public EventType getType() {
		return EventType.STATIC_ENTITY_EVENT;
	}

	public abstract StaticEntityEventType getSubType();
}
