package com.github.tiwindetea.dungeonoflegend.events.tilemap;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;

/**
 * Created by maxime on 5/10/16.
 */
public class TileClickEvent extends TileMapEvent {
	public Vector2i tilePosition;

	public TileClickEvent(Vector2i tilePosition) {
		this.tilePosition = tilePosition;
	}
}
