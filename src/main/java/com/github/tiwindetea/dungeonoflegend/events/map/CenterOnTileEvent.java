package com.github.tiwindetea.dungeonoflegend.events.map;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;

/**
 * Created by maxime on 5/21/16.
 */
public class CenterOnTileEvent extends MapEvent {
	public Vector2i tilePosition;

	public CenterOnTileEvent(Vector2i tilePosition) {
		this.tilePosition = tilePosition;
	}
}
