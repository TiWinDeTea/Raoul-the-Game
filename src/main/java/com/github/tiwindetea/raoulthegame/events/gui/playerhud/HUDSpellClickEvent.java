//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.gui.playerhud;

import com.github.tiwindetea.raoulthegame.events.Event;
import com.github.tiwindetea.raoulthegame.events.EventType;

/**
 * The type HUDSpellClickEvent.
 *
 * @author Maxime PINARD
 */
public class HUDSpellClickEvent extends Event {

	private int playerNumber;
	private int spellNumber;

	/**
	 * Instantiates a new HUDSpellClickEvent.
	 *
	 * @param playerNumber the player number
	 * @param spellNumber  the spell number
	 */
	public HUDSpellClickEvent(int playerNumber, int spellNumber) {
		this.playerNumber = playerNumber;
		this.spellNumber = spellNumber;
	}

	@Override
	public EventType getType() {
		return EventType.HUD_SPELL_CLICK_EVENT;
	}

	/**
	 * Gets player number.
	 *
	 * @return the player number
	 */
	public int getPlayerNumber() {
		return this.playerNumber;
	}

	/**
	 * Gets spell number.
	 *
	 * @return the spell number
	 */
	public int getSpellNumber() {
		return this.spellNumber;
	}

}
