//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.listeners.game;

import com.github.tiwindetea.raoulthegame.listeners.game.entities.EntityListener;
import com.github.tiwindetea.raoulthegame.listeners.game.map.MapListener;
import com.github.tiwindetea.raoulthegame.listeners.game.players.PlayerListener;

/**
 * The interface GameListener, that regroups some other listeners.
 *
 * @author Maxime PINARD
 */
public interface GameListener extends EntityListener, MapListener, ScoreUpdateListener, LevelUpdateListener, PlayerListener {
}
