//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

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

    @Override
    public MapEventType getSubType() {
        return MapEventType.CENTER_ON_TILE_EVENT;
    }
}
