//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.events.playerinventory;

/**
 * Created by organic-code on 6/4/16.
 */
public class ObjectDragEvent extends PlayerInventoryEvent {
    public Long objectId;

    public ObjectDragEvent(Long objectId) {
        this.objectId = objectId;
    }

    @Override
    public PlayerInventoryEventType getSubType() {
        return PlayerInventoryEventType.OBJECT_DRAG_EVENT;
    }
}
