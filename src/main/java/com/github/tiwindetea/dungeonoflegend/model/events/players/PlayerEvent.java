package com.github.tiwindetea.dungeonoflegend.model.events.players;

import com.github.tiwindetea.dungeonoflegend.model.events.Event;

/**
 * Created by maxime on 5/5/16.
 */
public abstract class PlayerEvent extends Event {
	public byte playerNumber;

	public PlayerEvent(byte playerNumber) {
		this.playerNumber = playerNumber;
	}
}
