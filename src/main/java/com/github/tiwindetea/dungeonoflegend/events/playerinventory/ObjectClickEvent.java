//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.events.playerinventory;

/**
 * Created by maxime on 5/18/16.
 */
public class ObjectClickEvent extends PlayerInventoryEvent {
	public Long objectId;

	public ObjectClickEvent(Long objectId) {
		this.objectId = objectId;
	}
    @Override
    public PlayerInventoryEventType getSubType() {
        return PlayerInventoryEventType.OJECT_CLICK_EVENT;
    }
}
