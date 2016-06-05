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
 * Created by organic-code on 6/4/16.
 */
public class EquipRequestEvent extends InventoryRequestEvent {

    public EquipRequestEvent(long objectId) {
        super(objectId);
    }

    @Override
    public RequestEventType getSubType() {
        return RequestEventType.EQUIP_REQUEST_EVENT;
    }
}
