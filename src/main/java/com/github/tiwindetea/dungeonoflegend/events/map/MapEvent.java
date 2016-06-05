//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.events.map;

import com.github.tiwindetea.dungeonoflegend.events.Event;
import com.github.tiwindetea.dungeonoflegend.events.EventType;

/**
 * Created by maxime on 5/6/16.
 */
public abstract class MapEvent extends Event {

    public EventType getType() {
        return EventType.MAP_EVENT;
    }

    public abstract MapEventType getSubType();
}
