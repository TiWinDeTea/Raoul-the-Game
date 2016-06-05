//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.listeners.game.entities.living_entities;

import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityHealthVisibilityEvent;

/**
 * The interface LivingEntityHealthVisibilityListener
 *
 * @author Maxime PINARD
 */
public interface LivingEntityHealthVisibilityListener {
	/**
	 * Handler associated to a LivingEntityHealthVisibilityEvent
	 * @param e Event to handle
	 */
	void setLivingEntityHealthVisibility(LivingEntityHealthVisibilityEvent e);
}
