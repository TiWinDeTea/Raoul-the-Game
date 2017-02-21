//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.listeners.playerinventory;

import com.github.tiwindetea.raoulthegame.events.playerinventory.ObjectClickEvent;

/**
 * The interface ObjectClickListener.
 *
 * @author Maxime PINARD
 */
public interface ObjectClickListener {

	/**
	 * Handler associated to an ObjectClickEvent.
	 *
	 * @param e Event to handle
     */
	void handle(ObjectClickEvent e);
}
