//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.game.map;

import com.github.tiwindetea.raoulthegame.model.space.Vector2i;

/**
 * The type CenterOnTileEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class CenterOnTileEvent extends MapEvent {
	private Vector2i tilePosition;

	/**
	 * Instantiates a new CenterOnTileEvent.
	 *
	 * @param tilePosition the tile position
	 */
	public CenterOnTileEvent(Vector2i tilePosition) {
		this.tilePosition = tilePosition;
	}

    @Override
    public MapEventType getSubType() {
        return MapEventType.CENTER_ON_TILE_EVENT;
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
