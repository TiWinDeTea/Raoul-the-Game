//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.gui.requests;

import com.github.tiwindetea.raoulthegame.events.Event;
import com.github.tiwindetea.raoulthegame.events.EventType;
import com.github.tiwindetea.raoulthegame.view.entities.SpellType;

/**
 * The type SpellSelectedRequestEvent.
 *
 * @author Maxime PINARD
 */
public class SpellSelectedRequestEvent extends Event {

	private SpellType spellType;

	/**
	 * Instantiates a new SpellSelectedRequestEvent.
	 *
	 * @param spellType the spell type
	 */
	public SpellSelectedRequestEvent(SpellType spellType) {
		this.spellType = spellType;
	}

	@Override
	public EventType getType() {
		return null;
	}

	/**
	 * Gets spell type.
	 *
	 * @return the spell type
	 */
	public SpellType getSpellType() {
		return this.spellType;
	}

}
