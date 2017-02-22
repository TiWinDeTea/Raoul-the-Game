//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.listeners.gui.playerhud;

import com.github.tiwindetea.raoulthegame.events.gui.playerhud.SpellClickEvent;

/**
 * The interface SpellClickListener.
 *
 * @author Maxime PINARD
 */
public interface SpellClickListener {

	/**
	 * Handler associated to an SpellClickEvent.
	 *
	 * @param e Event to handle
	 */
	void handle(SpellClickEvent e);
}
