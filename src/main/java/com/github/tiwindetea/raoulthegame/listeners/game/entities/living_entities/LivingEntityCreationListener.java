//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.listeners.game.entities.living_entities;

import com.github.tiwindetea.raoulthegame.events.game.living_entities.LivingEntityCreationEvent;

/**
 * The interface LivingEntityCreationListener.
 *
 * @author Maxime Pinard
 */
public interface LivingEntityCreationListener {

	/**
	 * Handler associated to a LivingEntityCreationEvent.
	 *
	 * @param e Event to handle
	 */
	void handle(LivingEntityCreationEvent e);
}
