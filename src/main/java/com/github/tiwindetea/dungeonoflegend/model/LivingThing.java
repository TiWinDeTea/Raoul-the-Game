package com.github.tiwindetea.dungeonoflegend.model;

/**
 * Created by maxime on 4/23/16.
 */
public abstract class LivingThing {
	protected Direction requestedAttack;
	protected Direction requestedMove;
	protected Tile[][] sight;
	protected int level;
	protected int maxHitPoints;
	protected int hitPoints;
	protected int attackPower;
	protected int defensePower;
	protected Vector2i position;

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

	public void setPosition(Vector2i position) {
		this.position = position;
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

	public boolean equals(Object o) {
		/* Asserting o to be a LivingThing */
		return this.equals((LivingThing) o);
	}

	public boolean equals(LivingThing livingThing) {
		return this.position.equals(livingThing.position);
	}
}
