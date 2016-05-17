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
	private State state = State.SLEEPING;
	private Vector2i requestedPath;
	private Stack<Vector2i> requestedPathStack = new Stack<>();
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
	public Mob(String name, int level, int maxHitPoints, int attackPower, int defensePower, int chaseRange, Vector2i position) {
		this.name = name;
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
		Direction[] directions = {Direction.DOWN, Direction.LEFT, Direction.UP, Direction.RIGHT};
		int index;
		/* Looking for a wall to follow */
		for (index = 0; index < directions.length &&
				!Tile.isRoomBorder(map(this.position.copy().add(directions[index]))); ++index)
			;

		/* If you are in a corner (twice for corridors)*/
		int count = 0;
		while (Tile.isRoomBorder(map(this.position.copy().add(directions[(index + 1) % directions.length]))) && count++ < 2) {
			index = (index + 1) % directions.length;
		}

		/* Follow the wall, or try to find one */
		Vector2i next = this.position.copy().add(directions[(index + 1) % 4]);
		if (!Tile.isObstructed(map(next))) {
			this.requestedPath = next;
		} else {
			/* Look for the a non-obstructed tile, ignoring living entities */
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

	private void chase(Collection<LivingThing> collisionsEntities, Player player) {
		this.requestedPathStack = map.getPath(this.position, player.getPosition(), false, collisionsEntities);
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
		if (o instanceof LivingThing) {
			return ((LivingThing) o).getPosition().equals(this.position);
		} else {
			/* Asserting o to be a pair of LivingThing */
			return ((Pair<LivingThing>) o).object.getPosition().equals(this.position);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void live(Collection<Mob> mobs, Collection<Player> players, boolean[][] los) {
		int distance = this.chaseRange + 1;
		Collection<LivingThing> shadow = new ArrayList<>();
		Player chasedPlayer = null;
		shadow.addAll(mobs);


		for (Player player : players) {
			Vector2i pos = player.getPosition();
			// if the player is in our LOS
			int distanceToPlayer = Math.max(Math.abs(pos.x - this.position.x), Math.abs(pos.y - this.position.y));
			if (distanceToPlayer < los.length / 2) {
				if (los[los.length / 2 - this.position.x + pos.x][los[0].length / 2 - this.position.y + pos.y]) {
					int dist = this.distanceTo(pos, shadow);
					if (distance > dist) {
						chasedPlayer = player;
						distance = dist;
					}
				}
			}
		}
		if (distance <= 1) {
			this.requestedAttack = chasedPlayer.getPosition();
			this.requestedPath = null;
		} else if (distance <= this.chaseRange) {
			this.chase(shadow, chasedPlayer);
			this.state = State.CHASING;
		} else if (this.requestedPathStack.isEmpty()) {

			/* Let's do something at random, if I can't chase anyone */
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
		if (this.requestedPathStack.size() > 0) {
			return this.requestedPathStack.pop();
		}
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

	public int getChaseRange() {
		return this.chaseRange;
	}

	private int distanceTo(Vector2i pos, Collection<LivingThing> entities) {
		Stack<Vector2i> path = map.getPath(this.position, pos, false, entities);
		return (path == null) ? Integer.MAX_VALUE : path.size();
	}

	@Override
	public String getDescription() {
		return this.name + " (Lv" + this.level + ".)";
	}
}
