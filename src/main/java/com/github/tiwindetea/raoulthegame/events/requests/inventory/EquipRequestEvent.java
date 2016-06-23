//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.requests.inventory;

import com.github.tiwindetea.raoulthegame.events.requests.RequestEventType;

/**
 * The type EquipRequestEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class EquipRequestEvent extends InventoryRequestEvent {

    /**
     * Instantiates a new EquipRequestEvent.
     *
     * @param objectId the object id
     */
    public EquipRequestEvent(long objectId) {
        super(objectId);
    }

    @Override
    public RequestEventType getSubType() {
        return RequestEventType.EQUIP_REQUEST_EVENT;
    }
}
