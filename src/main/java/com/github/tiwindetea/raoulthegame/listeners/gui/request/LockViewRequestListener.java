//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.listeners.gui.request;

import com.github.tiwindetea.raoulthegame.events.gui.requests.LockViewRequestEvent;

/**
 * The interface CenterViewRequestListener.
 *
 * @author Lucas LAZARE
 * @author Maxime PINARD
 */
public interface LockViewRequestListener {

    /**
     * Handler associated to an LockViewRequestEvent.
     *
     * @param e Event to handle
     */
    void handle(LockViewRequestEvent e);
}
