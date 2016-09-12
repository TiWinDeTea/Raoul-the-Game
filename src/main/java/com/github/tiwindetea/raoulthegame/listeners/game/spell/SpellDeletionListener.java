//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.listeners.game.spell;

import com.github.tiwindetea.raoulthegame.events.spells.SpellDeletionEvent;

/**
 * The interface SpellDeletionListener
 *
 * @author Lucas LAZARE
 */
public interface SpellDeletionListener {
    /**
     * Handler associated to a SpellDeletionEvent
     *
     * @param e Event to handle
     */
    void deleteSpell(SpellDeletionEvent e);
}
