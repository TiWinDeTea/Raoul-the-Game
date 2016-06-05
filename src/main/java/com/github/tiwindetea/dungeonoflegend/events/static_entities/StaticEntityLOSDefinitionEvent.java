//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.events.static_entities;

/**
 * The type StaticEntityLOSDefinitionEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class StaticEntityLOSDefinitionEvent extends StaticEntityEvent {
	private boolean[][] newLOS;

	/**
	 * Instantiates a new StaticEntityLOSDefinitionEvent.
	 *
	 * @param entityId the entity id
	 * @param newLOS   the new los
	 */
	public StaticEntityLOSDefinitionEvent(long entityId, boolean[][] newLOS) {
		super(entityId);
		this.newLOS = newLOS;
	}

    @Override
    public StaticEntityEventType getSubType() {
        return StaticEntityEventType.STATIC_ENTITY_LOS_DEFINITION_EVENT;
    }

	/**
	 * Get new los.
	 *
	 * @return the new los
	 */
	public boolean[][] getNewLOS() {
		return this.newLOS;
	}
}
