//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.events.living_entities;

/**
 * Created by maxime on 5/6/16.
 */
public class LivingEntityLOSDefinitionEvent extends LivingEntityEvent {
	public boolean[][] newLOS;

	public LivingEntityLOSDefinitionEvent(long entityId, boolean[][] newLOS) {
		super(entityId);
		this.newLOS = newLOS;
	}

    @Override
    public LivingEntityEventType getSubType() {
        return LivingEntityEventType.LIVING_ENTITY_LOS_DEFINITION_EVENT;
    }
}
