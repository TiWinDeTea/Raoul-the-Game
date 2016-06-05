//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.events.players;

import com.github.tiwindetea.dungeonoflegend.view.entities.LivingEntityType;

/**
 * Created by maxime on 5/5/16.
 */
public class PlayerCreationEvent extends PlayerEvent {
	public LivingEntityType playerType;
	public int maxHealth;
	public int maxMana;
	public int maxXP;
	public int level;
	public String playerName;

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
}
