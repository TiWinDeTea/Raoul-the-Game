package com.github.tiwindetea.dungeonoflegend.listeners.game.entities.players;

import com.github.tiwindetea.dungeonoflegend.events.players.PlayerNextTickEvent;

/**
 * Created by organic-code on 5/16/16.
 */
public interface PlayerNewTurnListener {
    void playerNextTick(PlayerNextTickEvent event);
}
