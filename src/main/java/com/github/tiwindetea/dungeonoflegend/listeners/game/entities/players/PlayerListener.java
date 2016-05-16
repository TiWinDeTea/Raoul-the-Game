package com.github.tiwindetea.dungeonoflegend.listeners.game.entities.players;

import com.github.tiwindetea.dungeonoflegend.listeners.game.entities.players.inventory.InventoryListener;

/**
 * Created by maxime on 5/6/16.
 */
public interface PlayerListener extends PlayerCreationListener, PlayerStatListener, InventoryListener, PlayerNewTurnListener {
}
