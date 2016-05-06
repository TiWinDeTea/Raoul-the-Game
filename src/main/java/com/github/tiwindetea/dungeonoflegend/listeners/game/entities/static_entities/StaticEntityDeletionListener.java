package com.github.tiwindetea.dungeonoflegend.listeners.game.entities.static_entities;

import com.github.tiwindetea.dungeonoflegend.events.static_entities.StaticEntityDeletionEvent;

/**
 * Created by maxime on 5/6/16.
 */
public interface StaticEntityDeletionListener {
	void deleteStaticEntity(StaticEntityDeletionEvent e);
}
