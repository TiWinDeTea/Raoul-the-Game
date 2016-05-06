package com.github.tiwindetea.dungeonoflegend.view.listeners.entities.static_entities;

import com.github.tiwindetea.dungeonoflegend.model.events.static_entities.StaticEntityCreationEvent;

/**
 * Created by maxime on 5/6/16.
 */
public interface StaticEntityCreationListener {
	void createStaticEntity(StaticEntityCreationEvent e);
}
