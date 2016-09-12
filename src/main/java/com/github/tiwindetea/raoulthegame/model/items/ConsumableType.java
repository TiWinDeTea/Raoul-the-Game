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
 * ConsumableType
 *
 * @author Lucas LAZARE
 */
public enum ConsumableType {
    POT {
        @Override
        public String toString() {
            return resourceBundle.getString("pot");
        }
    },
    SCROLL {
        @Override
        public String toString() {
            return resourceBundle.getString("scroll");
        }
    };

    private final static String bundleName = MainPackage.name + ".ConsumableType";

    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName, Locale.getDefault());

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return null;
    }
}
