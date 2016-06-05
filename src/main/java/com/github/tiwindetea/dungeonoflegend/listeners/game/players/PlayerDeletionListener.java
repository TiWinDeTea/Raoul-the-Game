//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.listeners.game.players;

import com.github.tiwindetea.dungeonoflegend.events.players.PlayerDeletionEvent;

/**
 * Created by maxime on 5/31/16.
 */
public interface PlayerDeletionListener {
	void deletePlayer(PlayerDeletionEvent e);
}
