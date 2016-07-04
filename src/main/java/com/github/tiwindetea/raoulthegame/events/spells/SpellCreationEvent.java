//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.spells;

import com.github.tiwindetea.raoulthegame.view.entities.SpellType;

/**
 * The type SpellCreationEvent.
 *
 * @author Lucas LAZARE
 */
public class SpellCreationEvent extends SpellEvent {

    private SpellType spellType;

    public SpellCreationEvent(long id, SpellType spellType) {
        super(id);
        this.spellType = spellType;
    }

    @Override
    public SpellEventType getSubType() {
        return SpellEventType.SPELL_CREATION_EVENT;
    }

    public SpellType getSpellType() {
        return this.spellType;
    }
}
