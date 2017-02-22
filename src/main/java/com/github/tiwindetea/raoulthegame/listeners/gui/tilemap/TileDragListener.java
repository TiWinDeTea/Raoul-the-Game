//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.listeners.gui.tilemap;

import com.github.tiwindetea.raoulthegame.events.gui.tilemap.TileDragEvent;

/**
 * The interface TileDragListener.
 *
 * @author Maxime PINARD
 */
public interface TileDragListener {

    /**
     * Handler associated to a TileDragEvent.
     *
     * @param e Event to handle
     */
    void handle(TileDragEvent e);
}
