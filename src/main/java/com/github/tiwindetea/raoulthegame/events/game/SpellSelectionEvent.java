//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.game;

import com.github.tiwindetea.raoulthegame.events.Event;
import com.github.tiwindetea.raoulthegame.events.EventType;
import com.github.tiwindetea.raoulthegame.view.entities.SpellType;

import java.util.Collection;

public class SpellSelectionEvent extends Event {

	private String text;
	private Collection<SpellType> spellTypes;

	public SpellSelectionEvent(String text, Collection<SpellType> spellTypes) {
		this.text = text;
		this.spellTypes = spellTypes;
	}

	@Override
	public EventType getType() {
		return EventType.SPELL_SELECTION_EVENT;
	}

	public String getText() {
		return this.text;
	}

	public Collection<SpellType> getSpellTypes() {
		return this.spellTypes;
	}

}
