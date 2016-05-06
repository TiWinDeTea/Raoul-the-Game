package com.github.tiwindetea.dungeonoflegend.model;

import com.github.tiwindetea.dungeonoflegend.model.events.players.PlayerCreationEvent;
import com.github.tiwindetea.dungeonoflegend.view.listeners.entities.EntityListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxime on 4/24/16.
 */
public class Game {
	private final List<EntityListener> listeners = new ArrayList<>();

	public void addEntityListener(EntityListener event) {
		this.listeners.add(event);
	}

	public EntityListener[] getEntityListeners() {
		return this.listeners.toArray(new EntityListener[this.listeners.size()]);
	}

	private void firePlayerCreationEvent(PlayerCreationEvent event) {
		for(EntityListener listener : this.getEntityListeners()) {
			listener.createPlayer(event);
		}
	}

	public Game() {
		//TODO
	}

	public void launch(byte numberOfPlayers) {
		//TODO
	}

	private void nextTick() {
		//TODO
	}

	private boolean loadSave() {
		//TODO
		return false;
	}

	private void save() {
		//TODO
	}
}
