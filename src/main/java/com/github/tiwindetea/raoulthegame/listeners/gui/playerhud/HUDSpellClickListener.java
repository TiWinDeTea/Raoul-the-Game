//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.listeners.gui.playerhud;

import com.github.tiwindetea.raoulthegame.events.gui.playerhud.HUDSpellClickEvent;

/**
 * The interface HUDSpellClickListener.
 *
 * @author Maxime PINARD
 */
public interface HUDSpellClickListener {

	/**
	 * Handler associated to an HUDSpellClickEvent.
	 *
	 * @param e Event to handle
	 */
	void handle(HUDSpellClickEvent e);
}
