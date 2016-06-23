//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events;

import static com.github.tiwindetea.raoulthegame.events.EventType.LEVEL_UPDATE_EVENT;

/**
 * The type LevelUpdateEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class LevelUpdateEvent extends Event {
	private int newLevel;

	/**
	 * Instantiates a new LevelUpdateEvent.
	 *
	 * @param newLevel the new level
	 */
	public LevelUpdateEvent(int newLevel) {
		this.newLevel = newLevel;
	}

	@Override
	public EventType getType() {
		return LEVEL_UPDATE_EVENT;
	}

	/**
	 * Gets new level.
	 *
	 * @return the new level
	 */
	public int getNewLevel() {
		return this.newLevel;
	}
}
