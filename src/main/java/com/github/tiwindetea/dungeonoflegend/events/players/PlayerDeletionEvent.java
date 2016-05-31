package com.github.tiwindetea.dungeonoflegend.events.players;

/**
 * Created by maxime on 5/31/16.
 */
public class PlayerDeletionEvent extends PlayerEvent {
	public PlayerDeletionEvent(int playerNumber) {
		super(playerNumber);
	}

	@Override
	public PlayerEventType getSubType() {
		return PlayerEventType.PLAYER_DELETION_EVENT;
	}
}
