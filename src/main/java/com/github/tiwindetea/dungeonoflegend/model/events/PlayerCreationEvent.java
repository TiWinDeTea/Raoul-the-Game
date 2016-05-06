package com.github.tiwindetea.dungeonoflegend.model.events;

/**
 * Created by maxime on 5/5/16.
 */
public class PlayerCreationEvent extends PlayerEvent {
	public PlayerCreationEvent(byte playerNumber) {
		super(playerNumber);
	}
}
