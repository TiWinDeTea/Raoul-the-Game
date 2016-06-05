//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.events.living_entities;

/**
 * Created by Maxime on 22/05/2016.
 */
public class LivingEntityHealthVisibilityEvent extends LivingEntityEvent {
	public boolean healthVisibility;

	public LivingEntityHealthVisibilityEvent(long entityId, boolean healthVisibility) {
		super(entityId);
		this.healthVisibility = healthVisibility;
	}

	@Override
	public LivingEntityEventType getSubType() {
		return LivingEntityEventType.LIVING_ENTITY_HEALTH_VISIBILITY_EVENT;
	}
}
