//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.events.map;

import com.github.tiwindetea.dungeonoflegend.model.Tile;

/**
 * Created by maxime on 5/6/16.
 */
public class MapCreationEvent extends MapEvent {
	public Tile[][] map;

	public MapCreationEvent(Tile[][] map) {
		this.map = map;
	}

    @Override
    public MapEventType getSubType() {
        return MapEventType.MAP_CREATION_EVENT;
    }
}
