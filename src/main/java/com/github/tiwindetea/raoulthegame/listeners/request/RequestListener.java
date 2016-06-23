//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.listeners.request;

import com.github.tiwindetea.raoulthegame.listeners.request.inventory.InventoryRequestListener;

/**
 * The interface RequestListener, that regroups some other listener
 * @author Maxime PINARD
 */
public interface RequestListener extends CenterViewRequestListener, InventoryRequestListener, MoveRequestListener, InteractionRequestListener, LockViewRequestListener {
}
