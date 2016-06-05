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
 * Created by maxime on 5/6/16.
 */
public class LivingEntityMoveEvent extends LivingEntityEvent {
	public Vector2i newPosition;

	public LivingEntityMoveEvent(long entityId, Vector2i newPosition) {
		super(entityId);
		this.newPosition = newPosition;
	}

    @Override
    public LivingEntityEventType getSubType() {
        return LivingEntityEventType.LIVING_ENTITY_MOVE_EVENT;
    }
}
