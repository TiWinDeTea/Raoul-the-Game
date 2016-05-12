package com.github.tiwindetea.dungeonoflegend.events.map;

import com.github.tiwindetea.dungeonoflegend.model.Tile;
import com.github.tiwindetea.dungeonoflegend.model.Vector2i;

/**
 * Created by maxime on 5/12/16.
 */
public class TileModificationEvent extends MapEvent {
	Vector2i tilePosition;
	Tile tileType;

	public TileModificationEvent(Vector2i tilePosition, Tile tileType) {
		this.tilePosition = tilePosition;
		this.tileType = tileType;
	}
}
