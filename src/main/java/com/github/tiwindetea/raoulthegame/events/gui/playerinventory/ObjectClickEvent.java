//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.gui.playerinventory;

/**
 * The type ObjectClickEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class ObjectClickEvent extends PlayerInventoryEvent {
	private Long objectId;

	/**
	 * Instantiates a new ObjectClickEvent.
	 *
	 * @param objectId the object id
	 */
	public ObjectClickEvent(Long objectId) {
		this.objectId = objectId;
	}

	@Override
	public PlayerInventoryEventType getSubType() {
		return PlayerInventoryEventType.OJECT_CLICK_EVENT;
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
