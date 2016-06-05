//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.events.map;

import com.github.tiwindetea.dungeonoflegend.model.Tile;
import com.github.tiwindetea.dungeonoflegend.model.Vector2i;

/**
 * Created by maxime on 5/12/16.
 */
public class TileModificationEvent extends MapEvent {
	public Vector2i tilePosition;
	public Tile tileType;

	public TileModificationEvent(Vector2i tilePosition, Tile tileType) {
		this.tilePosition = tilePosition;
		this.tileType = tileType;
	}

    @Override
    public MapEventType getSubType() {
        return MapEventType.TILE_MODIFICATION_EVENT;
    }
}
