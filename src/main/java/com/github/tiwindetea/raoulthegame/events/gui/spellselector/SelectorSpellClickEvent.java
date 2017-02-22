//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.gui.spellselector;

import com.github.tiwindetea.raoulthegame.events.Event;
import com.github.tiwindetea.raoulthegame.events.EventType;
import com.github.tiwindetea.raoulthegame.view.entities.SpellType;

/**
 * The type SelectorSpellClickListener.
 *
 * @author Maxime PINARD
 */
public class SelectorSpellClickEvent extends Event {

	private SpellType eventType;

	/**
	 * Instantiates a new SelectorSpellClickListener.
	 *
	 * @param eventType the event type
	 */
	public SelectorSpellClickEvent(SpellType eventType) {
		this.eventType = eventType;
	}

	@Override
	public EventType getType() {
		return EventType.SELECTOR_SPELL_CLICK_EVENT;
	}

	/**
	 * Gets event type.
	 *
	 * @return the event type
	 */
	public SpellType getEventType() {
		return this.eventType;
	}

}
