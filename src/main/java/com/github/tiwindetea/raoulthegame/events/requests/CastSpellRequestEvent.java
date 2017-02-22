//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.requests;

/**
 * The type CastSpellRequestEvent.
 */
public class CastSpellRequestEvent extends RequestEvent {

	private int playerNumber;
	private int spellNumber;

	/**
	 * Instantiates a new CastSpellRequestEvent.
	 *
	 * @param playerNumber the player number
	 * @param spellNumber  the spell number
	 */
	public CastSpellRequestEvent(int playerNumber, int spellNumber) {
		this.playerNumber = playerNumber;
		this.spellNumber = spellNumber;
	}

	@Override
	public RequestEventType getSubType() {
		return RequestEventType.CAST_SPELL_REQUEST_EVENT;
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
