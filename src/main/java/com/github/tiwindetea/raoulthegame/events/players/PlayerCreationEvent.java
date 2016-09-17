//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.players;

import com.github.tiwindetea.raoulthegame.view.entities.LivingEntityType;

/**
 * The type PlayerCreationEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class PlayerCreationEvent extends PlayerEvent {
	private LivingEntityType playerType;
	private int maxHealth;
	private int maxMana;
	private int maxXP;
	private int level;
	private String playerName;

	/**
	 * Instantiates a new PlayerCreationEvent.
	 *
	 * @param playerNumber the player number
	 * @param playerType   the player type
	 * @param maxHealth    the max health
	 * @param maxMana      the max mana
	 * @param maxXP        the max xp
	 * @param level        the level
	 * @param playerName   the player name
	 */
	public PlayerCreationEvent(int playerNumber, LivingEntityType playerType, int maxHealth, int maxMana, int maxXP, int level, String playerName) {
		super(playerNumber);
		this.playerType = playerType;
		this.maxHealth = maxHealth;
		this.maxMana = maxMana;
		this.maxXP = maxXP;
		this.level = level;
		this.playerName = playerName;
	}

	@Override
    public PlayerEventType getSubType() {
        return PlayerEventType.PLAYER_CREATION_EVENT;
	}

	/**
	 * Gets player type.
	 *
	 * @return the player type
	 */
	public LivingEntityType getPlayerType() {
		return this.playerType;
	}

	/**
	 * Gets max health.
	 *
	 * @return the max health
	 */
	public int getMaxHealth() {
		return this.maxHealth;
	}

	/**
	 * Gets max mana.
	 *
	 * @return the max mana
	 */
	public int getMaxMana() {
		return this.maxMana;
	}

	/**
	 * Gets max xp.
	 *
	 * @return the max xp
	 */
	public int getMaxXP() {
		return this.maxXP;
	}

	/**
	 * Gets level.
	 *
	 * @return the level
	 */
	public int getLevel() {
		return this.level;
	}

	/**
	 * Gets player name.
	 *
	 * @return the player name
	 */
	public String getPlayerName() {
		return this.playerName;
	}
}
