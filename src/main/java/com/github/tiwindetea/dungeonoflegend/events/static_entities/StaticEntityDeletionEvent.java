//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.events.static_entities;

/**
 * Created by maxime on 5/3/16.
 */
public class StaticEntityDeletionEvent extends StaticEntityEvent {
	public StaticEntityDeletionEvent(long entityId) {
		super(entityId);
	}
    @Override
    public StaticEntityEventType getSubType() {
        return StaticEntityEventType.STATIC_ENTITY_DELETION_EVENT;
    }

}
