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
    SAVIOR("savior"),
    SAMPLE_SPELL("sample-spell"),
    REGEN("regen"),
    DRAINER("drainer"),
    BONUS_HP("bonus-hp"),
    Berserker("berserker"),
    SUMMON_DOG("summon-dog");

    static SpellType[] thisValues = SpellType.values();
    static String[] thisStrings = null;

    public String key;

    SpellType(String ressourceBundleName) {
        this.key = ressourceBundleName;
    }

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
