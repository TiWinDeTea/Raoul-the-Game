package com.github.tiwindetea.dungeonoflegend.view.listeners;

import com.github.tiwindetea.dungeonoflegend.model.events.MoveEvent;

/**
 * Created by maxime on 5/2/16.
 */
public interface MoveListener extends java.util.EventListener {
	public void moveEntity(MoveEvent e);
}
