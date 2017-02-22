//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.listeners.game.spells;

import com.github.tiwindetea.raoulthegame.events.game.spells.SpellDescriptionUpdateEvent;

/**
 * The interface SpellDescriptionUpdateListener.
 *
 * @author Maxime PINARD
 */
public interface SpellDescriptionUpdateListener {

	/**
	 * Handler associated to a SpellDescriptionUpdateEvent.
	 *
	 * @param e Event to handle
	 */
	void handle(SpellDescriptionUpdateEvent e);
}
