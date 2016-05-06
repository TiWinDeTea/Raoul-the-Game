package com.github.tiwindetea.dungeonoflegend.events.players;

import com.github.tiwindetea.dungeonoflegend.model.Direction;
import com.github.tiwindetea.dungeonoflegend.model.Vector2i;

/**
 * Created by maxime on 5/5/16.
 */
public class PlayerCreationEvent extends PlayerEvent {
	public Vector2i position;
	public Direction direction;
	public int maxHealth;
	public int maxMana;

	public PlayerCreationEvent(int playerNumber, Vector2i position, Direction direction, int maxHealth, int maxMana) {
		super(playerNumber);
		this.position = position;
		this.direction = direction;
		this.maxHealth = maxHealth;
		this.maxMana = maxMana;
	}
}
