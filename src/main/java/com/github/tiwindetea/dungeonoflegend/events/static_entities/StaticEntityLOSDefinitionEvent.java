//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.events.static_entities;

/**
 * Created by maxime on 5/6/16.
 */
public class StaticEntityLOSDefinitionEvent extends StaticEntityEvent {
	public boolean[][] newLOS;

	public StaticEntityLOSDefinitionEvent(long entityId, boolean[][] newLOS) {
		super(entityId);
		this.newLOS = newLOS;
	}

    @Override
    public StaticEntityEventType getSubType() {
        return StaticEntityEventType.STATIC_ENTITY_LOS_DEFINITION_EVENT;
    }
}
