//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.events.players;

/**
 * Created by maxime on 5/5/16.
 */
public class PlayerStatEvent extends PlayerEvent {
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
	public enum ValueType {
		ACTUAL,
		MAX
	}

	public StatType statType;
	public ValueType valueType;
	public int value;

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
}
