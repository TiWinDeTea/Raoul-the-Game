package com.github.tiwindetea.dungeonoflegend.view.listeners;

import com.github.tiwindetea.dungeonoflegend.model.events.EntityCreationEvent;

/**
 * Created by maxime on 5/3/16.
 */
public interface EntityCreationListener extends java.util.EventListener {
	public void createEntity(EntityCreationEvent e);
}
