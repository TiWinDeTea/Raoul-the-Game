//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.model;

import java.util.ResourceBundle;

/**
 * MainPackage (constants)
 * @author Lucas LAZARE
 */
public class MainPackage {
    public static final String name = "com.github.tiwindetea.dungeonoflegend";
    public static final String path = name.replace('.', '/');
    private final static String bundleName = name + ".SpriteSheet";
    public final static ResourceBundle spriteSheetBundle = ResourceBundle.getBundle(bundleName);
}
