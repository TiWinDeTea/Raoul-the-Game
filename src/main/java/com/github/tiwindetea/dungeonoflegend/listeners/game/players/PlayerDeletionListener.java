package com.github.tiwindetea.dungeonoflegend.listeners.game.players;

import com.github.tiwindetea.dungeonoflegend.events.players.PlayerDeletionEvent;

/**
 * Created by maxime on 5/31/16.
 */
public interface PlayerDeletionListener {
	void deletePlayer(PlayerDeletionEvent e);
}
