//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend;

import com.github.tiwindetea.oggplayer.Sound;
import com.github.tiwindetea.oggplayer.Sounds;

/**
 * Created by organic-code on 5/31/16.
 */
public class Main {
    public static void main(String[] args) {
        Sounds[] sounds = Sounds.values();
        for (Sounds sound : sounds) {
            Sound.player.put(sound, sound.toURL(), sound.isLooping());
        }
        MainMenu.main(args);
        Sound.player.stopAndClear();
    }
}