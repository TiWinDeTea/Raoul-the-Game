package com.github.tiwindetea.dungeonoflegend.view.listeners;

import com.github.tiwindetea.dungeonoflegend.view.listeners.living_entities.LivingEntityListener;
import com.github.tiwindetea.dungeonoflegend.view.listeners.players.PlayerListener;
import com.github.tiwindetea.dungeonoflegend.view.listeners.static_entities.StaticEntityListener;

/**
 * Created by maxime on 5/2/16.
 */
public interface EntityListener extends LivingEntityListener, StaticEntityListener, PlayerListener {
}
