package com.github.tiwindetea.oggplayer;

import com.github.tiwindetea.raoulthegame.model.MainPackage;

import java.util.ResourceBundle;

/**
 * Created by organic-code on 5/31/16.
 */
public class Sound {
    public static final ResourceBundle SOUNDS_BUNDLE = ResourceBundle.getBundle(MainPackage.name + ".Sounds.Sounds");

    public static final UnifiedPlayers<Sounds> player = new UnifiedPlayers<>(10, 2);
}
