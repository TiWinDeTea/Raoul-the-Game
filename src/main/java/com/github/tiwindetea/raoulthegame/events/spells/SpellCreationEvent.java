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
 * @author Maxime PINARD
 */
public class SpellCreationEvent extends SpellEvent {

    private SpellType spellType;
	private int baseCooldown;
	private String description;

	/**
	 * Instantiates a new SpellCreationEvent.
	 *
	 * @param playerNumber the player number
	 * @param spellNumber  the spell number
	 * @param spellType    the spell type
	 * @param baseCooldown the base cooldown
	 * @param description  the description
	 */
	public SpellCreationEvent(int playerNumber,
	                          int spellNumber,
	                          SpellType spellType,
	                          int baseCooldown,
	                          String description) {
		super(playerNumber, spellNumber);
		this.spellType = spellType;
		this.baseCooldown = baseCooldown;
		this.description = description;
	}

	@Override
	public SpellEventType getSubType() {
		return SpellEventType.SPELL_CREATION_EVENT;
	}

	/**
	 * Gets spell type.
	 *
	 * @return the spell type
	 */
	public SpellType getSpellType() {
		return spellType;
	}

	/**
	 * Gets base cooldown.
	 *
	 * @return the base cooldown
	 */
	public int getBaseCooldown() {
		return baseCooldown;
	}

	/**
	 * Gets description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

}
