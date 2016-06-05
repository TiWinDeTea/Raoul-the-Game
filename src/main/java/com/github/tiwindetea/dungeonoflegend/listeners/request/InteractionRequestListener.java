//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.listeners.request;

import com.github.tiwindetea.dungeonoflegend.events.requests.InteractionRequestEvent;

/**
 * Created by maxime on 5/6/16.
 */
public interface InteractionRequestListener {
	void requestInteraction(InteractionRequestEvent e);
}
