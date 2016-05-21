//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.model;

import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityMoveEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.PlayerStatEvent;
import com.github.tiwindetea.dungeonoflegend.listeners.game.GameListener;
import com.github.tiwindetea.dungeonoflegend.listeners.game.entities.players.PlayerStatListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * LivingThing.
 * @author Lucas LAZARE
 */
public abstract class LivingThing implements Descriptable {

	protected Tile[][] sight;
	protected int level;
	protected int maxHitPoints;
	protected int hitPoints;
	protected int attackPower;
	protected int defensePower;
	protected Vector2i position;
	protected Vector2i requestedAttack;
	protected String name;
	protected long id = Pair.getNewId();
	protected static final ArrayList<GameListener> listeners = new ArrayList<>();


	public static void addGameListener(GameListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	protected static GameListener[] getPlayersListeners() {
		return LivingThing.listeners.toArray(new GameListener[LivingThing.listeners.size()]);
	}

	protected void fireStatEvent(PlayerStatEvent event) {
		for (PlayerStatListener listener : getPlayersListeners()) {
			listener.changePlayerStat(event);
		}
	}

	protected void fireMoveEvent(LivingEntityMoveEvent event) {
		for (GameListener listener : getPlayersListeners()) {
			listener.moveLivingEntity(event);
		}
	}


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
		fireMoveEvent(new LivingEntityMoveEvent(this.id, position));
		this.position = position;
	}

	public long getId() {
		return this.id;
	}

	/**
	 * Damages this.
	 *
	 * @param damages damages taken by this
	 */
	public void damage(int damages) {
		if (damages > 0)
			this.hitPoints = Math.min(this.hitPoints + this.defensePower - damages, this.hitPoints - 1);
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
	 * @param los LOS of this
	 */
	public abstract void live(List<Mob> mobs, Collection<Player> players, boolean[][] los);

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
		if (this.requestedAttack == null) {
			return null;
		}
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

	public String getDescription() {
		System.out.println("name = " + this.name);
		return this.name;
	}
}
