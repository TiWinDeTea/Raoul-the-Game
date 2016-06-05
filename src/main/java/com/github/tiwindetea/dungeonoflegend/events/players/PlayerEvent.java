//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.events.players;

import com.github.tiwindetea.dungeonoflegend.events.Event;
import com.github.tiwindetea.dungeonoflegend.events.EventType;

/**
 * Created by maxime on 5/5/16.
 */
public abstract class PlayerEvent extends Event {
	public int playerNumber;

	public PlayerEvent(int playerNumber) {
		this.playerNumber = playerNumber;
	}

    public EventType getType() {
        return EventType.PLAYER_EVENT;
    }

    public abstract PlayerEventType getSubType();
}
