//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.listeners.request.inventory;

import com.github.tiwindetea.dungeonoflegend.events.requests.inventory.EquipRequestEvent;

/**
 * The interface EquipRequestListener
 * @author Lucas LAZARE
 */
public interface EquipRequestListener {
    /**
     * Handler associated to an EquipRequestEvent
     * @param e Event to handle
     */
    void requestEquipping(EquipRequestEvent e);
}
