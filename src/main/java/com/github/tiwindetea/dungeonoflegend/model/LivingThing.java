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
 * LivingThing.
 * @author Lucas LAZARE
 */
public abstract class LivingThing {

	protected Tile[][] sight;
	protected int level;
	protected int maxHitPoints;
	protected int hitPoints;
	protected int attackPower;
	protected int defensePower;
	protected Vector2i position;
	protected Vector2i requestedAttack;


	/**
	 * Instantiates a new Living thing.
	 */
	public LivingThing() {

	}

	/**
	 * Updates the sight.
	 *
	 * @param sight the new sight
	 */
	public void updateSight(Tile[][] sight) {
		this.sight = sight;
	}

	/**
	 * Gets level.
	 *
	 * @return the level
	 */
	public int getLevel() {
		return this.level;
	}

	/**
	 * Gets max hit points.
	 *
	 * @return the max hit points
	 */
	public int getMaxHitPoints() {
		return this.maxHitPoints;
	}

	/**
	 * Gets hit points.
	 *
	 * @return the hit points
	 */
	public int getHitPoints() {
		return this.hitPoints;
	}

	/**
	 * Gets attack power.
	 *
	 * @return the attack power
	 */
	public int getAttackPower() {
		return this.attackPower;
	}

	/**
	 * Gets defense power.
	 *
	 * @return the defense power
	 */
	public int getDefensePower() {
		return this.defensePower;
	}

	/**
	 * Gets position.
	 *
	 * @return the position
	 */
	public Vector2i getPosition() {
		return this.position.copy();
	}

	/**
	 * Sets position.
	 *
	 * @param position the new position
	 */
	public void setPosition(Vector2i position) {
		this.position = position;
	}

	/**
	 * Damages this.
	 *
	 * @param damages damages taken by this
	 */
	public void damage(int damages) {
		if (damages > 0)
			this.hitPoints -= damages;
	}

	/**
	 * @return true if the LivingThing is alive, false otherwise
	 */
	public boolean isAlive() {
		return (this.hitPoints > 0);
	}

	/**
	 * Make this to live.
	 *
	 * @param mobs Mobs around this
	 * @param players Players around this
	 *
	 */
	public abstract void live(Collection<Pair<Mob>> mobs, Collection<Pair<Player>> players);

	/**
	 * Gets the requested move.
	 *
	 * @return the requested move
	 */
	public abstract Vector2i getRequestedMove();

	/**
	 * Gets the requested attack.
	 *
	 * @return the requested attack
	 */
	public Vector2i getRequestedAttack() {
		return this.requestedAttack.copy();
	}

	/**
	 * Gets type.
	 *
	 * @return the type of this
	 * @see LivingThingType
	 */
	public abstract LivingThingType getType();

	/**
	 * Make the players to attack the target
	 *
	 * @param target target of the attack
	 */
	public abstract void attack(LivingThing target);

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o) {
		/* Asserting o to be a LivingThing */
		return this.equals((LivingThing) o);
	}

	/**
	 * Equals boolean.
	 *
	 * @param vect the vect
	 * @return the boolean
	 */
	public boolean equals(Vector2i vect) {
		return this.position.equals(vect);
	}

	/**
	 * Equals boolean.
	 *
	 * @param livingThing the living thing
	 * @return the boolean
	 */
	public boolean equals(LivingThing livingThing) {
		return this.position.equals(livingThing.position);
	}
}
