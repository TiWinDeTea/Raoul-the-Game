package com.github.tiwindetea.dungeonoflegend.view.listeners.static_entities;

import com.github.tiwindetea.dungeonoflegend.model.events.static_entities.StaticEntityDeletionEvent;

/**
 * Created by maxime on 5/6/16.
 */
public interface StaticEntityDeletionListener {
	void deleteStaticEntity(StaticEntityDeletionEvent e);
}
