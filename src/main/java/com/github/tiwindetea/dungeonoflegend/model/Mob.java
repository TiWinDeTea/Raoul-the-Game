//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.Stack;

/**
 * Mob.
 *
 * @author Lucas LAZARE
 */
public class Mob extends LivingThing {
	// Mobs know the map perfectly.
	private static Map map;
	private State state;
	private Vector2i requestedPath;
	private int chaseRange;

	/**
	 * Sets the map.
	 *
	 * @param map the map
	 */
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
	public Mob(int level, int maxHitPoints, int attackPower, int defensePower, int chaseRange, Vector2i position) {
		this.level = level;
		this.maxHitPoints = maxHitPoints;
		this.attackPower = attackPower;
		this.defensePower = defensePower;
		this.chaseRange = chaseRange;
		this.position = position;
		this.hitPoints = maxHitPoints;
	}

	private Tile map(Vector2i pos) {
		return Mob.map.getTile(pos);
	}

	private void keepPatroling() {
		Direction[] directions = {Direction.DOWN, Direction.LEFT, Direction.UP, Direction.DOWN};
		int index;
		for (index = 0; !Tile.isRoomBorder(map(this.position.copy().add(directions[index])))
				&& index < directions.length; ++index)
			;

		Vector2i next = this.position.copy().add(directions[index]);
		if (Tile.isRoomBorder(map(next))) {
			this.requestedPath = next;
		} else {
			for (index = 0; Tile.isObstructed(map(this.position.copy().add(directions[index]))) && index < directions.length; ++index)
				;
			next = this.position.copy().add(directions[index]);
			if (!Tile.isObstructed(map(next))) {
				this.requestedPath = next;
			} else {
				this.requestedPath = next;
			}
		}
	}

	private boolean chase(Collection<Pair<LivingThing>> collisionsEntities, Collection<Pair<Player>> players) {
		double minDistance = Double.POSITIVE_INFINITY;
		double distance;
		Vector2i target = null;
		Player player;
		for (Pair<Player> playerPair : players) {
			player = playerPair.object;
			distance = this.position.distance(player.getPosition());
			if (distance <= this.chaseRange && distance <= minDistance) {
				minDistance = distance;
				target = player.getPosition();
			}
		}

		if (target != null) {
			Stack<Vector2i> tmp = map.getPathPair(this.position, target, false, collisionsEntities);
			if (tmp == null) {
				this.requestedPath = null;
				return false;
			}
			this.requestedPath = tmp.peek();
			return true;
		}
		return false;
	}

	private void wander() {
		Direction[] directions = {Direction.DOWN, Direction.LEFT, Direction.UP, Direction.DOWN};
		ArrayList<Direction> possibleDirs = new ArrayList<>(4);
		for (Direction direction : directions) {
			if (!Tile.isObstructed(map(this.position.copy().add(direction)))) {
				possibleDirs.add(direction);
			}
		}

		if (possibleDirs.size() > 0)
			this.requestedPath = this.position.copy().add(possibleDirs.get(new Random().nextInt(possibleDirs.size())));
		else
			this.requestedPath = this.position.copy();
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

		//TODO : check all that
		double distance = Double.POSITIVE_INFINITY;
		for (Pair<Player> player : players) {
			distance = Math.min(distance, this.position.distance(player.object.getPosition()));
		}
		if (distance < this.chaseRange) {
			Collection<Pair<LivingThing>> shadow = new ArrayList<>();
			for (Pair<Mob> mob : mobs) {
				shadow.add(new Pair<>(mob));
			}
			this.chase(shadow, players);
			this.state = State.CHASING;
		} else {

			switch (this.state) {
				case PATROLING:
					this.keepPatroling();
					switch (new Random().nextInt(20)) {
						case 0:
							this.state = State.STANDING;
							break;
						case 1:
							this.state = State.WANDERING;
							break;
						default:
					}
					break;
				case STANDING:
					switch (new Random().nextInt(4)) {
						case 0:
							this.state = State.PATROLING;
							break;
						case 1:
							this.state = State.WANDERING;
							break;
						default:
					}
					break;
				case SLEEPING:
					switch (new Random().nextInt(3)) {
						case 0:
							this.state = State.STANDING;
							break;
						default:
					}
					break;
				case WANDERING:
					wander();
					switch (new Random().nextInt(20)) {
						case 0:
							this.state = State.PATROLING;
							break;
						case 1:
							this.state = State.STANDING;
							break;
						default:
					}
					this.wander();
					break;
				default:
					wander();
					this.state = State.WANDERING;
					break;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector2i getRequestedMove() {
		return this.requestedPath;
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
