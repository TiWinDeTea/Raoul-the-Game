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
 */
public class SpellDeletionEvent extends SpellEvent {

    public SpellDeletionEvent(long id) {
        super(id);
    }

    @Override
    public SpellEventType getSubType() {
        return null;
    }
}
