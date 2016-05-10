package com.github.tiwindetea.dungeonoflegend.events.requests;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;

/**
 * Created by maxime on 5/6/16.
 */
public class InteractionRequestEvent extends RequestEvent {
	public Vector2i tilePosition;

	public InteractionRequestEvent(Vector2i tilePosition) {
		this.tilePosition = tilePosition;
	}
}
