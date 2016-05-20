package com.github.tiwindetea.dungeonoflegend.events.map;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;

/**
 * Created by maxime on 5/20/16.
 */
public class FogAdditionEvent extends MapEvent {
	public Vector2i fogCenterPosition;
	public boolean[][] fog;

	public FogAdditionEvent(Vector2i fogCenterPosition, boolean[][] fog) {
		this.fogCenterPosition = fogCenterPosition;
		this.fog = fog;
	}
}
