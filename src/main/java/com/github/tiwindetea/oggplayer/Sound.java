package com.github.tiwindetea.oggplayer;

import com.github.tiwindetea.dungeonoflegend.model.MainPackage;

import java.util.ResourceBundle;

/**
 * Created by organic-code on 5/31/16.
 */
public class Sound {
    static final ResourceBundle SOUNDS_BUNDLE = ResourceBundle.getBundle(MainPackage.name + ".Sounds.Sounds");

    public static final OGGPlayers<Sounds> player = new OGGPlayers<>(10, 2);
}
