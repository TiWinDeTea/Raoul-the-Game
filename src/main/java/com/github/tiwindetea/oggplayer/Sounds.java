package com.github.tiwindetea.oggplayer;

import com.github.tiwindetea.dungeonoflegend.model.MainPackage;

import java.io.File;

import static com.github.tiwindetea.oggplayer.Sound.SOUNDS_BUNDLE;

/**
 * Created by organic-code on 5/31/16.
 */
public enum Sounds {
    MAIN_THEME(SOUNDS_BUNDLE.getString("main-theme"));

    private final String path;

    Sounds(String accessPath) {
        this.path = new File("src/main/resources/" + MainPackage.path + "/" + accessPath).toURI().toString();
    }

    public String toString() {
        return this.path;
    }
}
