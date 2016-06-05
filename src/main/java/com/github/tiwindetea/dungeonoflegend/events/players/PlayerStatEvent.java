//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.events.players;

/**
 * The type PlayerStatEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class PlayerStatEvent extends PlayerEvent {

	/**
	 * The enum StatType.
	 */
	public enum StatType {
		HEALTH,
		MANA,
		XP,
		LEVEL,
		DAMAGES,
		ARMOR,
		RANGE,
		POWER_GRADE
	}

	/**
	 * The enum ValueType.
	 */
	public enum ValueType {
		ACTUAL,
		MAX
	}

	private StatType statType;
	private ValueType valueType;
	private int value;

	/**
	 * Instantiates a new PlayerStatEvent.
	 *
	 * @param playerNumber the player number
	 * @param statType     the stat type
	 * @param valueType    the value type
	 * @param value        the value
	 */
	public PlayerStatEvent(int playerNumber, StatType statType, ValueType valueType, int value) {
		super(playerNumber);
		this.statType = statType;
		this.valueType = valueType;
		this.value = value;
	}

	@Override
	public PlayerEventType getSubType() {
		return PlayerEventType.PLAYER_STAT_EVENT;
	}

	/**
	 * Gets stat type.
	 *
	 * @return the stat type
	 */
	public StatType getStatType() {
		return this.statType;
	}

	/**
	 * Gets value type.
	 *
	 * @return the value type
	 */
	public ValueType getValueType() {
		return this.valueType;
	}

	/**
	 * Gets value.
	 *
	 * @return the value
	 */
	public int getValue() {
		return this.value;
	}
}
