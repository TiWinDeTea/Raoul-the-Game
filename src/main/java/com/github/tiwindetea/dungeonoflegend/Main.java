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
