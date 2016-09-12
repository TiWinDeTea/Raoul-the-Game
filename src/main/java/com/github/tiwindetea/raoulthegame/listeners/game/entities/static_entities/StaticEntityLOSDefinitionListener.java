//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.listeners.game.entities.static_entities;

import com.github.tiwindetea.raoulthegame.events.static_entities.StaticEntityLOSDefinitionEvent;

/**
 * The interface StaticEntityLOSDefinitionListener
 * @author Maxime PINARD
 */
public interface StaticEntityLOSDefinitionListener {
	/**
	 * Handler associated to a StaticEntityLOSDefinitionEvent
	 * @param e Event to handle
	 */
	void defineStaticEntityLOS(StaticEntityLOSDefinitionEvent e);
}
