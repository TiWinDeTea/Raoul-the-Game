//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.listeners.game;

import com.github.tiwindetea.dungeonoflegend.listeners.game.entities.EntityListener;
import com.github.tiwindetea.dungeonoflegend.listeners.game.map.MapListener;
import com.github.tiwindetea.dungeonoflegend.listeners.game.players.PlayerListener;

/**
 * Created by maxime on 5/6/16.
 */
public interface GameListener extends EntityListener, MapListener, ScoreUpdateListener, LevelUpdateListener, PlayerListener {
}
