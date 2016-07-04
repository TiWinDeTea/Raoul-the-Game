//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.spells;

import com.github.tiwindetea.raoulthegame.events.Event;
import com.github.tiwindetea.raoulthegame.events.EventType;

/**
 * The type SpellEvent.
 *
 * @author Lucas LAZARE
 */
public abstract class SpellEvent extends Event {

    private long id;

    public SpellEvent(long id) {
        this.id = id;
    }

    @Override
    public EventType getType() {
        return EventType.SPELL_EVENT;
    }

    public abstract SpellEventType getSubType();

    public long getSpellId() {
        return this.id;
    }
}
