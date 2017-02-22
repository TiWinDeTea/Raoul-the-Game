//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.gui.playerinventory;

/**
 * The type ObjectDragEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class ObjectDragEvent extends PlayerInventoryEvent {
	private Long objectId;

	/**
	 * Instantiates a new ObjectDragEvent.
	 *
	 * @param objectId the object id
	 */
	public ObjectDragEvent(Long objectId) {
		this.objectId = objectId;
	}

	@Override
	public PlayerInventoryEventType getSubType() {
		return PlayerInventoryEventType.OBJECT_DRAG_EVENT;
	}

	/**
	 * Gets object id.
	 *
	 * @return the object id
	 */
	public Long getObjectId() {
		return this.objectId;
	}
}
