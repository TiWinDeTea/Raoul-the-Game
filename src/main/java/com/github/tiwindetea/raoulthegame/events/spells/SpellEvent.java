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
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public abstract class SpellEvent extends Event {

    private int playerNumber;
    private int spellNumber;

	/**
	 * Instantiates a new Spell event.
	 *
	 * @param playerNumber the player number
	 * @param spellNumber  the spell number
	 */
	public SpellEvent(int playerNumber, int spellNumber) {
		this.playerNumber = playerNumber;
		this.spellNumber = spellNumber;
	}

	@Override
    public EventType getType() {
        return EventType.SPELL_EVENT;
    }

	/**
	 * Gets sub type.
	 *
	 * @return the sub type
	 */
	public abstract SpellEventType getSubType();

	/**
	 * Gets player number.
	 *
	 * @return the player number
	 */
	public int getPlayerNumber() {
		return playerNumber;
	}

	/**
	 * Gets spell number.
	 *
	 * @return the spell number
	 */
	public int getSpellNumber() {
		return spellNumber;
	}
}
