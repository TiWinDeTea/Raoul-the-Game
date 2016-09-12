//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.map;

import com.github.tiwindetea.raoulthegame.model.space.Tile;

/**
 * The type MapCreationEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class MapCreationEvent extends MapEvent {
	private Tile[][] map;

	/**
	 * Instantiates a new MapCreationEvent.
	 *
	 * @param map the map
	 */
	public MapCreationEvent(Tile[][] map) {
		this.map = map;
	}

    @Override
    public MapEventType getSubType() {
        return MapEventType.MAP_CREATION_EVENT;
    }

	/**
	 * Get map.
	 *
	 * @return the map
	 */
	public Tile[][] getMap() {
		return this.map;
	}
}
