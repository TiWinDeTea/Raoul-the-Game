//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.view.entities;

/**
 * The enum SpellType
 *
 * @author Lucas LAZARE
 */
public enum SpellType {
    SAMPLE_SPELL;

    static SpellType[] thisValues = SpellType.values();
    static String[] thisStrings = null;

    static SpellType parseSpellType(String str) {
        if (thisStrings == null) {
            thisStrings = new String[thisValues.length];
            for (int i = 0; i < thisValues.length; i++) {
                thisStrings[i] = thisValues[i].toString();
            }
        }
        for (int i = 0; i < thisValues.length; i++) {
            if (thisStrings[i].equals(str)) {
                return thisValues[i];
            }
        }
        return null;
    }
}
