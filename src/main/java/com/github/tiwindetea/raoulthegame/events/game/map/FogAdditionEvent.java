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
 * The type FogAdditionEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class FogAdditionEvent extends MapEvent {
	private Vector2i fogCenterPosition;
	private boolean[][] fog;

	/**
	 * Instantiates a new FogAdditionEvent.
	 *
	 * @param fogCenterPosition the fog center position
	 * @param fog               the fog
	 */
	public FogAdditionEvent(Vector2i fogCenterPosition, boolean[][] fog) {
		this.fogCenterPosition = fogCenterPosition;
		this.fog = fog;
	}

    @Override
    public MapEventType getSubType() {
        return MapEventType.FOG_ADDITION_EVENT;
    }

	/**
	 * Gets fog center position.
	 *
	 * @return the fog center position
	 */
	public Vector2i getFogCenterPosition() {
		return this.fogCenterPosition;
	}

	/**
	 * Get fog.
	 *
	 * @return the fog
	 */
	public boolean[][] getFog() {
		return this.fog;
	}
}
