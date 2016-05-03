package com.github.tiwindetea.dungeonoflegend.view.listeners;

import com.github.tiwindetea.dungeonoflegend.model.events.EntityDeletionEvent;

/**
 * Created by maxime on 5/3/16.
 */
public interface EntityDeletionListener extends java.util.EventListener {
	public void deleteEntity(EntityDeletionEvent e);
}
