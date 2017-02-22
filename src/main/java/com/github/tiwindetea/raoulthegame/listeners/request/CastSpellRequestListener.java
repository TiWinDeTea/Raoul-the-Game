//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.listeners.request;

import com.github.tiwindetea.raoulthegame.events.requests.CastSpellRequestEvent;

/**
 * The interface CastSpellRequestListener.
 *
 * @author Maxime PINARD
 */
public interface CastSpellRequestListener {

	/**
	 * Handler associated to a CastSpellRequestEvent.
	 *
	 * @param e Event to handle
	 */
	void handle(CastSpellRequestEvent e);

}
