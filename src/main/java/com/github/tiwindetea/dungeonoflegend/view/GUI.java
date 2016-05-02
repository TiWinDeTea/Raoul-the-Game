package com.github.tiwindetea.dungeonoflegend.view;

import com.github.tiwindetea.dungeonoflegend.model.events.MoveEvent;
import com.github.tiwindetea.dungeonoflegend.view.listeners.EntityListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by maxime on 5/2/16.
 */
public class GUI implements EntityListener {
	private Map<Integer, Entity> entityMap = new HashMap<>();

	//TODO: entity created(with position), entity deleted ...

	@Override
	public void moveEntity(MoveEvent e) {
		//TODO
	}
}
