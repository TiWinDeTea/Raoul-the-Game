//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

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

    @Override
    public TileMapEventType getSubType() {
        return TileMapEventType.TILE_CLICK_EVENT;
    }
}
