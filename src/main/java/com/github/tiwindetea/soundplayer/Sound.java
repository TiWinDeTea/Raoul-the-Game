//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.soundplayer;

import com.github.tiwindetea.raoulthegame.model.MainPackage;

import java.util.ResourceBundle;

/**
 * Created by organic-code on 5/31/16.
 */
public class Sound {
    public static final ResourceBundle SOUNDS_BUNDLE = ResourceBundle.getBundle(MainPackage.name + ".Sounds.Sounds");

    public static final UnifiedPlayers<Sounds> player = new UnifiedPlayers<>(10, 2);
}
