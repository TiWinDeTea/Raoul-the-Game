//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.soundplayer;

import com.github.tiwindetea.raoulthegame.model.MainPackage;

import java.net.URL;

import static com.github.tiwindetea.soundplayer.Sound.SOUNDS_BUNDLE;

/**
 * Created by organic-code on 5/31/16.
 */
public enum Sounds {
    ATTACK_SOUND(SOUNDS_BUNDLE.getString("attack-sound")),
    BULB_SOUND(SOUNDS_BUNDLE.getString("bulb-sound")),
    DOOR_SOUND(SOUNDS_BUNDLE.getString("door-sound")),
    LEVEL_UP_SOUND(SOUNDS_BUNDLE.getString("level-up-sound")),
    MAIN_MENU_START_SOUND(SOUNDS_BUNDLE.getString("main-menu-start-sound")),
    LOOT_SOUND(SOUNDS_BUNDLE.getString("loot-sound")),
    NEXT_FLOOR_SOUND(SOUNDS_BUNDLE.getString("next-floor-sound")),
    WALK_SOUND(SOUNDS_BUNDLE.getString("walk-sound")),

    DEATH_MUSIC(SOUNDS_BUNDLE.getString("death-music"), true),
    MAIN_MUSIC(SOUNDS_BUNDLE.getString("main-music"), true),
    MENU_MUSIC(SOUNDS_BUNDLE.getString("menu-music"), true),
    CREDITS_MUSIC(SOUNDS_BUNDLE.getString("credits-music"), true);


    private final URL path;
    private final boolean isLooping;
    private final ClassLoader loader = this.getClass().getClassLoader();

    Sounds(String accessPath, boolean looping) {
        this.path = this.loader.getResource(MainPackage.path + "/" + accessPath);
        this.isLooping = looping;
    }

    Sounds(String accessPath) {
        this(accessPath, false);
    }

    public boolean isLooping() {
        return this.isLooping;
    }

    public URL toURL() {
        return this.path;
    }
}
