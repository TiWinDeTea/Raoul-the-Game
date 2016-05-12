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
 * Created by maxime on 4/23/16.
 */
public abstract class LivingThing {
	protected Tile[][] sight;
	protected int level;
	protected int maxHitPoints;
	protected int hitPoints;
	protected int attackPower;
	protected int defensePower;
	protected Vector2i position;

	public LivingThing() {

	}
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
		return this.position.copy();
	}

	public void setPosition(Vector2i position) {
		this.position = position;
	}

	public void damage(int damages) {
		if (damages > 0)
			this.hitPoints -= damages;
	}

	public boolean isAlive() {
		return (this.hitPoints > 0);
	}

	public abstract void live(Collection<Pair<LivingThing>> livingEntities);

	public abstract LivingThingType getType();

	public boolean equals(Object o) {
		/* Asserting o to be a LivingThing */
		return this.equals((LivingThing) o);
	}

	public boolean equals(Vector2i vect) {
		return this.position.equals(vect);
	}

	public boolean equals(LivingThing livingThing) {
		return this.position.equals(livingThing.position);
	}
}
