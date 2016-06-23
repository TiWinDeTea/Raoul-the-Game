//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.playerinventory;

import com.github.tiwindetea.raoulthegame.events.Event;
import com.github.tiwindetea.raoulthegame.events.EventType;

/**
 * The type PlayerInventoryEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public abstract class PlayerInventoryEvent extends Event {

    public EventType getType() {
        return EventType.PLAYER_INVENTORY_EVENT;
    }

    /**
     * Gets sub type.
     *
     * @return the sub type
     */
    public abstract PlayerInventoryEventType getSubType();
}
