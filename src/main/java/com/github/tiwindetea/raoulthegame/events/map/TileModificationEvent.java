//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.map;

import com.github.tiwindetea.raoulthegame.model.space.Tile;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;

/**
 * The type TileModificationEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class TileModificationEvent extends MapEvent {
	private Vector2i tilePosition;
	private Tile tileType;

	/**
	 * Instantiates a new TileModificationEvent.
	 *
	 * @param tilePosition the tile position
	 * @param tileType     the tile type
	 */
	public TileModificationEvent(Vector2i tilePosition, Tile tileType) {
		this.tilePosition = tilePosition;
		this.tileType = tileType;
	}

    @Override
    public MapEventType getSubType() {
        return MapEventType.TILE_MODIFICATION_EVENT;
    }

	/**
	 * Gets tile position.
	 *
	 * @return the tile position
	 */
	public Vector2i getTilePosition() {
		return this.tilePosition;
	}

	/**
	 * Gets tile type.
	 *
	 * @return the tile type
	 */
	public Tile getTileType() {
		return this.tileType;
	}
}
