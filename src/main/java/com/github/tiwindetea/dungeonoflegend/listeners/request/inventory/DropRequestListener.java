package com.github.tiwindetea.dungeonoflegend.listeners.request.inventory;

import com.github.tiwindetea.dungeonoflegend.events.requests.inventory.DropRequestEvent;

/**
 * Created by maxime on 5/6/16.
 */
public interface DropRequestListener {
	void requestDrop(DropRequestEvent e);
}
