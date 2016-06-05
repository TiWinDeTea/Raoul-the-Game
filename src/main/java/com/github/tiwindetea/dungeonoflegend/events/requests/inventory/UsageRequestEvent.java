//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.events.requests.inventory;

import com.github.tiwindetea.dungeonoflegend.events.requests.RequestEventType;

/**
 * The type UsageRequestEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class UsageRequestEvent extends InventoryRequestEvent {

	/**
	 * Instantiates a new UsageRequestEvent.
	 *
	 * @param objectId the object id
	 */
	public UsageRequestEvent(long objectId) {
		super(objectId);
	}

	@Override
	public RequestEventType getSubType() {
		return RequestEventType.USAGE_REQUEST_EVENT;
	}
}
