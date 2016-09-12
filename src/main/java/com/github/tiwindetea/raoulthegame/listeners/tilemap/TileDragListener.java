//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.listeners.tilemap;

import com.github.tiwindetea.raoulthegame.events.tilemap.TileDragEvent;

/**
 * The interface TileDrageListener
 * @author Maxime PINARD
 */
public interface TileDragListener {
    /**
     * Handler associated to a TileDragEvent
     * @param e Event to handle
     */
    void tileDragged(TileDragEvent e);
}
