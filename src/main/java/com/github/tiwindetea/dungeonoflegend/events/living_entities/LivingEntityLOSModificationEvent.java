//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.events.living_entities;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;

import java.util.List;

/**
 * The type LivingEntityLOSModificationEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class LivingEntityLOSModificationEvent extends LivingEntityEvent {
	private List<Vector2i> modifiedTilesPositions;

	/**
	 * Instantiates a new LivingEntityLOSModificationEvent.
	 *
	 * @param entityId               the entity id
	 * @param modifiedTilesPositions the modified tiles positions
	 */
	public LivingEntityLOSModificationEvent(long entityId, List<Vector2i> modifiedTilesPositions) {
		super(entityId);
		this.modifiedTilesPositions = modifiedTilesPositions;
	}

    @Override
    public LivingEntityEventType getSubType() {
        return LivingEntityEventType.LIVING_ENTITY_LOS_MODIFICATION_EVENT;
    }

	/**
	 * Gets modified tiles positions.
	 *
	 * @return the modified tiles positions
	 */
	public List<Vector2i> getModifiedTilesPositions() {
		return this.modifiedTilesPositions;
	}
}
