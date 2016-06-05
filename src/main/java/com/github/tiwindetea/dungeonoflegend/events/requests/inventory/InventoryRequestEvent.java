//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.events.requests.inventory;

import com.github.tiwindetea.dungeonoflegend.events.requests.RequestEvent;

/**
 * The type InventoryRequestEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public abstract class InventoryRequestEvent extends RequestEvent {
	private long objectId;

	/**
	 * Instantiates a new InventoryRequestEvent.
	 *
	 * @param objectId the object id
	 */
	public InventoryRequestEvent(long objectId) {
		this.objectId = objectId;
	}

	/**
	 * Gets object id.
	 *
	 * @return the object id
	 */
	public long getObjectId() {
		return this.objectId;
	}
}
