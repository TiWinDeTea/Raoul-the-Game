package com.github.tiwindetea.dungeonoflegend.model;

import java.util.ResourceBundle;

/**
 * Created by organic-code on 4/30/16.
 */
public class MainPackage {
    public static final String name = "com.github.tiwindetea.dungeonoflegend";
    public static final String path = name.replace('.', '/');
    private final static String bundleName = name + ".SpriteSheet";
    public final static ResourceBundle spriteSheetBundle = ResourceBundle.getBundle(bundleName);
}
