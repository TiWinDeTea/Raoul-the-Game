package com.github.tiwindetea.dungeonoflegend.events.players;

/**
 * Created by organic-code on 5/16/16.
 */
public class PlayerNextTickEvent extends PlayerEvent {

    public PlayerNextTickEvent(int playerNumber) {
        super(playerNumber);
    }

    @Override
    public PlayerEventType getSubType() {
        return PlayerEventType.PLAYER_NEXT_TICK_EVENT;
    }
}
