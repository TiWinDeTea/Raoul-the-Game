package com.github.tiwindetea.dungeonoflegend.listeners.game.entities;

import com.github.tiwindetea.dungeonoflegend.listeners.game.entities.living_entities.LivingEntityListener;
import com.github.tiwindetea.dungeonoflegend.listeners.game.entities.players.PlayerListener;
import com.github.tiwindetea.dungeonoflegend.listeners.game.entities.static_entities.StaticEntityListener;

/**
 * Created by maxime on 5/2/16.
 */
public interface EntityListener extends LivingEntityListener, StaticEntityListener, PlayerListener {
}
