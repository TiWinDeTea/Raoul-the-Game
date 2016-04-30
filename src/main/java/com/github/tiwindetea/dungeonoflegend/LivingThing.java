package com.github.tiwindetea.dungeonoflegend;

/**
 * Created by maxime on 4/23/16.
 */
public abstract class LivingThing {
	private Tile[][] sight;
	private int level;
	private int maxHitPoints;
	private int hitPoints;
	private int attackPower;
	private int defensePower;
	private Vector2i position;
	Direction requestedAttack;
	Direction requestedMove;

	public void updateSight(Tile[][] sight) {
		this.sight = sight;
	}

	public Tile[][] getSight() {
		return this.sight;
	}

	public int getLevel() {
		return this.level;
	}

	public int getMaxHitPoints() {
		return this.maxHitPoints;
	}

	public int getHitPoints() {
		return this.hitPoints;
	}

	public int getAttackPower() {
		return this.attackPower;
	}

	public int getDefensePower() {
		return this.defensePower;
	}

	public Vector2i getPosition() {
		return this.position;
	}

	public Direction getRequestedAttack() {
		return this.requestedAttack;
	}

	public Direction getRequestedMove() {
		return this.requestedMove;
	}

	public void damage(int damages) {
		if (damages > 0)
			this.hitPoints -= damages;
	}

	public boolean isAlive() {
		return (this.hitPoints > 0);
	}

	public void print() {
		//TODO
	}

	public abstract void live();

	public abstract LivingThingType getType();
}
