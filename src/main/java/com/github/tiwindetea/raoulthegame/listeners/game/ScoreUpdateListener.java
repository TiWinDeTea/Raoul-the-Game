//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.listeners.game;

import com.github.tiwindetea.raoulthegame.events.game.ScoreUpdateEvent;

/**
 * The interface ScoreUpdateListener.
 *
 * @author Maxime PINARD
 */
public interface ScoreUpdateListener{

	/**
	 * Handler associated to a ScoreUpdateEvent.
	 *
	 * @param e Event to handle
	 */
	void handle(ScoreUpdateEvent e);
}
