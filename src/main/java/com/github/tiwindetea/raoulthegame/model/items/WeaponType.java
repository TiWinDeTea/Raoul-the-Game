//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.model.items;

import com.github.tiwindetea.raoulthegame.model.MainPackage;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * WeaponType
 *
 * @author Lucas LAZARE
 */
public enum WeaponType {
    BOW {
        @Override
        public String toString() {
            return resourceBundle.getString("bow");
        }
    },
    SWORD {
        @Override
        public String toString() {
            return resourceBundle.getString("sword");
        }
    },
    WAND {
        @Override
        public String toString() {
            return resourceBundle.getString("wand");
        }
    },
    FIST {
        @Override
        public String toString() {
            return resourceBundle.getString("fist");
        }
    };

    private final static String bundleName = MainPackage.name + ".WeaponType";

    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName, Locale.getDefault());


    /**
     * {@inheritDoc}
     */
    public String toString() {
        return null;
    }

    /**
     * Parses a WeaponType.
     *
     * @param str a WeaponType's String
     * @return the WeaponType
     */
    public static WeaponType parseWeaponType(String str) {
        if (str.equals(BOW.toString())) {
            return BOW;
        } else if (str.equals(SWORD.toString())) {
            return SWORD;
        } else if (str.equals(WAND.toString())) {
            return WAND;
        } else if (str.equals(FIST.toString())) {
            return FIST;
        } else {
            return null;
        }
    }
}
