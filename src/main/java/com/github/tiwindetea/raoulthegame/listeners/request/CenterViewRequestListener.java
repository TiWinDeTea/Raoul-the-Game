//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.listeners.request;

import com.github.tiwindetea.raoulthegame.events.requests.CenterViewRequestEvent;

/**
 * The interface CenterViewRequestListener.
 *
 * @author Maxime PINARD
 */
public interface CenterViewRequestListener {

	/**
	 * Handler associated to a CenterViewRequestEvent.
	 *
	 * @param e Event to handle
	 */
	void handle(CenterViewRequestEvent e);
}
