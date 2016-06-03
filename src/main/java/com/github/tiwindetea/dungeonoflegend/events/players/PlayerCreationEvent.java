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
	public String description;

	public PlayerCreationEvent(int playerNumber, LivingEntityType playerType, int maxHealth, int maxMana, int maxXP, int level, String description) {
		super(playerNumber);
		this.playerType = playerType;
		this.maxHealth = maxHealth;
		this.maxMana = maxMana;
		this.maxXP = maxXP;
		this.level = level;
		this.description = description;
	}

	@Override
    public PlayerEventType getSubType() {
        return PlayerEventType.PLAYER_CREATION_EVENT;
    }
}
