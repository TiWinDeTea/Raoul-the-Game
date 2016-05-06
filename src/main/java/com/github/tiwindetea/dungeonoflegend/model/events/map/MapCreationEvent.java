package com.github.tiwindetea.dungeonoflegend.model.events.map;

import com.github.tiwindetea.dungeonoflegend.model.Tile;

/**
 * Created by maxime on 5/6/16.
 */
public class MapCreationEvent extends MapEvent {
	public Tile[][] map;

	public MapCreationEvent(Tile[][] map) {
		this.map = map;
	}
}
