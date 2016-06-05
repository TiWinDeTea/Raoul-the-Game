//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.events.static_entities;

/**
 * The type StaticEntityDeletionEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class StaticEntityDeletionEvent extends StaticEntityEvent {
	/**
	 * Instantiates a new StaticEntityDeletionEvent.
	 *
	 * @param entityId the entity id
	 */
	public StaticEntityDeletionEvent(long entityId) {
		super(entityId);
	}
    @Override
    public StaticEntityEventType getSubType() {
        return StaticEntityEventType.STATIC_ENTITY_DELETION_EVENT;
    }

}
