package com.github.tiwindetea.dungeonoflegend.listeners.game.players;

import com.github.tiwindetea.dungeonoflegend.listeners.game.players.inventory.InventoryListener;

/**
 * Created by maxime on 5/6/16.
 */
public interface PlayerListener extends PlayerCreationListener, PlayerDeletionListener, PlayerStatListener, InventoryListener, PlayerNextTickListener {
}
