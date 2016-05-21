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
