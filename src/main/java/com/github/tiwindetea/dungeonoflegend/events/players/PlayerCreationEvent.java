package com.github.tiwindetea.dungeonoflegend.events.players;

import com.github.tiwindetea.dungeonoflegend.model.Direction;
import com.github.tiwindetea.dungeonoflegend.model.Vector2i;

/**
 * Created by maxime on 5/5/16.
 */
public class PlayerCreationEvent extends PlayerEvent {
	public long entityId;
	public Vector2i position;
	public Direction direction;
	public int maxHealth;
	public int maxMana;
	public int maxXP;
	public String description;

	public PlayerCreationEvent(int playerNumber, long entityId, Vector2i position, Direction direction, int maxHealth, int maxMana, int maxXP, String description) {
		super(playerNumber);
		this.entityId = entityId;
		this.position = position;
		this.direction = direction;
		this.maxHealth = maxHealth;
		this.maxMana = maxMana;
		this.maxXP = maxXP;
		this.description = description;
	}

	@Override
    public PlayerEventType getSubType() {
        return PlayerEventType.PLAYER_CREATION_EVENT;
    }
}
