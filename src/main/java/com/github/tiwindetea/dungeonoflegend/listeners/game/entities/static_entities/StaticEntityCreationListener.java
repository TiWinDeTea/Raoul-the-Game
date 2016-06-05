//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.listeners.game.entities.static_entities;

import com.github.tiwindetea.dungeonoflegend.events.static_entities.StaticEntityCreationEvent;

/**
 * The interface StaticEntityCreationListener
 * @author Maxime PINARD
 */
public interface StaticEntityCreationListener {
	/**
	 * Handler associated to a StaticEntityCreationEvent
	 * @param e Event to handle
	 */
	void createStaticEntity(StaticEntityCreationEvent e);
}
