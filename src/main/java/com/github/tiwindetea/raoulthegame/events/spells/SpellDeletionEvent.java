//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.spells;

/**
 * The type SpellDeletionEvent.
 *
 * @author Lucas LAZARE
 * @author Maxime PINARD
 */
public class SpellDeletionEvent extends SpellEvent {

    /**
     * Instantiates a new SpellDeletionEvent.
     *
     * @param playerNumber the player number
     * @param spellNumber  the spell number
     */
    public SpellDeletionEvent(int playerNumber, int spellNumber) {
        super(playerNumber, spellNumber);
    }

    @Override
    public SpellEventType getSubType() {
        return SpellEventType.SPELL_DELETION_EVENT;
    }

}
