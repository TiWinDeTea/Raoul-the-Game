package com.github.tiwindetea.dungeonoflegend.view.listeners;

import com.github.tiwindetea.dungeonoflegend.model.events.EntityMoveEvent;

/**
 * Created by maxime on 5/2/16.
 */
public interface EntityMoveListener extends java.util.EventListener {
	public void moveEntity(EntityMoveEvent e);
}
