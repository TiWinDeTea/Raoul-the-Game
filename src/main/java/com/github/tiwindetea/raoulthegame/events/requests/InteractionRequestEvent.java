//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.requests;

import com.github.tiwindetea.raoulthegame.model.Vector2i;

/**
 * The type InteractionRequestEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class InteractionRequestEvent extends RequestEvent {
	private Vector2i tilePosition;

	/**
	 * Instantiates a new InteractionRequestEvent.
	 *
	 * @param tilePosition the tile position
	 */
	public InteractionRequestEvent(Vector2i tilePosition) {
		this.tilePosition = tilePosition;
	}

	@Override
	public RequestEventType getSubType() {
		return RequestEventType.INTERACTION_REQUEST_EVENT;
	}

	/**
	 * Gets tile position.
	 *
	 * @return the tile position
	 */
	public Vector2i getTilePosition() {
		return this.tilePosition;
	}
}
