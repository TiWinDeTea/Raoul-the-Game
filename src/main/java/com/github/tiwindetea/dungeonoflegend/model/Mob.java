//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.model;

import java.util.Collection;

/**
 * Mob.
 *
 * @author Lucas LAZARE
 */
public class Mob extends LivingThing {
	// Mobs know the map perfectly.
	private static Map map;
	private State state;
	private Vector2i requstedPath;

	public static void setMap(Map map) {
		Mob.map = map;
	}

	/**
	 * Instantiates a new Mob. (for comparison purposes)
	 *
	 * @param pos the pos
	 */
	public Mob(Vector2i pos) {
		this.position = pos;
	}

	/**
	 * Instantiates a new Mob.
	 *
	 * @param level        level
	 * @param maxHitPoints max hit points
	 * @param attackPower  attack power
	 * @param defensePower defense power
	 * @param position     position
	 */
	public Mob(int level, int maxHitPoints, int attackPower, int defensePower, Vector2i position) {
		this.level = level;
		this.maxHitPoints = maxHitPoints;
		this.attackPower = attackPower;
		this.defensePower = defensePower;
		this.position = position;
		this.hitPoints = maxHitPoints;
		this.requstedPath = new Vector2i();
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		/* Asserting o to be a mob */
		return ((Mob) o).position.equals(this.position);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void live(Collection<Pair<Mob>> mobs, Collection<Pair<Player>> players) {
		//TODO
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector2i getRequestedMove() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attack(LivingThing target) {
		target.damage(this.attackPower);
	}

	@Override
	public LivingThingType getType() {
		return LivingThingType.MOB;
	}
}
