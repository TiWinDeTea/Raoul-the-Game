package com.github.tiwindetea.dungeonoflegend.view.listeners.entities;

import com.github.tiwindetea.dungeonoflegend.view.listeners.entities.living_entities.LivingEntityListener;
import com.github.tiwindetea.dungeonoflegend.view.listeners.entities.players.PlayerListener;
import com.github.tiwindetea.dungeonoflegend.view.listeners.entities.static_entities.StaticEntityListener;

/**
 * Created by maxime on 5/2/16.
 */
public interface EntityListener extends LivingEntityListener, StaticEntityListener, PlayerListener {
}
