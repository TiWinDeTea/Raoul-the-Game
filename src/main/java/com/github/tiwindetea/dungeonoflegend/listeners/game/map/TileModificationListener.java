//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.listeners.game.map;

import com.github.tiwindetea.dungeonoflegend.events.map.TileModificationEvent;

/**
 * The interface TileModificationListener
 * @author Maxime PINARD
 */
public interface TileModificationListener {
	/**
	 * Handler associated to a TileModificationEvent
	 * @param e Event to handle
	 */
	void modifyTile(TileModificationEvent e);
}
