package com.github.tiwindetea.dungeonoflegend.model;

import java.util.Collection;

/**
 * Created by maxime on 4/23/16.
 */
public class Mob extends LivingThing {
	private State state;

	public Mob(Vector2i pos) {
		this.position = pos;
	}

	public Mob(int level, int maxHitPoints, int attackPower, int defensePower, Vector2i position) {
		this.level = level;
		this.maxHitPoints = maxHitPoints;
		this.attackPower = attackPower;
		this.defensePower = defensePower;
		this.position = position;
		this.hitPoints = maxHitPoints;
	}

	public Mob() {
		//TODO
	}

	private void keepPatroling() {
		//TODO
	}

	private void chase() {
		//TODO
	}

	private void wander() {
		//TODO
	}

	@Override
	public boolean equals(Object o) {
		/* Asserting o to be a mob */
		return ((Mob) o).position.equals(this.position);
	}

	@Override
	public void live(Collection<Pair<LivingThing>> livingEntities) {
		//TODO
	}

	@Override
	public LivingThingType getType() {
		return LivingThingType.MOB;
	}
}
