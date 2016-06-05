//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.events.living_entities;

import com.github.tiwindetea.dungeonoflegend.events.Event;
import com.github.tiwindetea.dungeonoflegend.events.EventType;

/**
 * Created by maxime on 5/6/16.
 */
public abstract class LivingEntityEvent extends Event {
	public long entityId;

	public LivingEntityEvent(long entityId) {
		this.entityId = entityId;
	}
    public EventType getType() {
        return EventType.LIVING_ENTITY_EVENT;
    }

    public abstract LivingEntityEventType getSubType();
}
