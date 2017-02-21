//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.listeners.request.inventory;

import com.github.tiwindetea.raoulthegame.events.requests.inventory.UsageRequestEvent;

/**
 * The interface UsageRequestListener.
 *
 * @author Maxime PINARD
 */
public interface UsageRequestListener {

	/**
	 * Handler associated to an UsageRequestEvent.
	 *
	 * @param e Event to handle
	 */
	void handle(UsageRequestEvent e);
}
