package com.github.tiwindetea.dungeonoflegend.listeners.game;

import com.github.tiwindetea.dungeonoflegend.listeners.game.entities.EntityListener;
import com.github.tiwindetea.dungeonoflegend.listeners.game.map.MapListener;

/**
 * Created by maxime on 5/6/16.
 */
public interface GameListener extends EntityListener, MapListener, ScoreUpdateListener {
}
