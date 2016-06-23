//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.listeners.game.entities.living_entities;

import com.github.tiwindetea.raoulthegame.events.living_entities.LivingEntityMoveEvent;

/**
 * The interface LivingEntityMoveListener
 * @author Maxime PINARD
 */
public interface LivingEntityMoveListener {
	/**
	 * Handler associated to a LivingEntityMoveEvent
	 * @param e Event to handle
	 */
	void moveLivingEntity(LivingEntityMoveEvent e);
}
