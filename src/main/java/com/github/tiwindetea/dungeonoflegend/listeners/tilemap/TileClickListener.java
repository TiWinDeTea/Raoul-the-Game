//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.listeners.tilemap;

import com.github.tiwindetea.dungeonoflegend.events.tilemap.TileClickEvent;

/**
 * The interface TileClickListener
 * @author Maxime PINARD
 */
public interface TileClickListener {
	/**
	 * Handler associated to a TileClickEvent
	 * @param e Event to handle
	 */
	void tileClicked(TileClickEvent e);
}
