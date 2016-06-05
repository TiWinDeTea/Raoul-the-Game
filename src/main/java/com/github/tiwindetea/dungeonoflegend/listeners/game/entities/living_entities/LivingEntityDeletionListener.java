//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.listeners.game.entities.living_entities;

import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityDeletionEvent;

/**
 * The interface LivingEntityDeletionEvent
 * @author Maxime PINARD
 */
public interface LivingEntityDeletionListener {
	/**
	 * Handler associated to a LivingEntityTypeDeletionEvent
	 * @param e Event to handle
	 */
	void deleteLivingEntity(LivingEntityDeletionEvent e);
}
