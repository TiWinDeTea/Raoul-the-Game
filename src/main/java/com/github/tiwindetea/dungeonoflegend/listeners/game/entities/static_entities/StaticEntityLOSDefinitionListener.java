//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.listeners.game.entities.static_entities;

import com.github.tiwindetea.dungeonoflegend.events.static_entities.StaticEntityLOSDefinitionEvent;

/**
 * Created by maxime on 5/6/16.
 */
public interface StaticEntityLOSDefinitionListener {
	void defineStaticEntityLOS(StaticEntityLOSDefinitionEvent e);
}
