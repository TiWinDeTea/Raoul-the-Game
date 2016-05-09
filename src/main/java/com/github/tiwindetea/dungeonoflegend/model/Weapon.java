package com.github.tiwindetea.dungeonoflegend.model;

import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntityType;

import java.util.Random;

/**
 * Created by maxime on 4/23/16.
 */
public class Weapon implements StorableObject {

	private int attackPowerModifier;
	private int range;
	private int manaCost;
	private WeaponType type;

	private StaticEntityType gtype;

	public Weapon(int attackPowerModifier, int range, int manaCost) {
		this.attackPowerModifier = attackPowerModifier;
		this.range = range;
		this.manaCost = manaCost;
		Random random = new Random();
		if (manaCost != 0) {
			this.type = WeaponType.WAND;
			if (random.nextBoolean())
				this.gtype = StaticEntityType.WAND1;
			else
				this.gtype = StaticEntityType.WAND2;
		} else if (range != 0) {
			this.type = WeaponType.BOW;
			if (random.nextBoolean())
				this.gtype = StaticEntityType.BOW1;
			else
				this.gtype = StaticEntityType.BOW2;
		} else {
			this.type = WeaponType.SWORD;
			if (random.nextBoolean())
				this.gtype = StaticEntityType.SWORD1;
			else
				this.gtype = StaticEntityType.SWORD2;
		}
	}

	public StaticEntityType getGtype() {
		return this.gtype;
	}

	public WeaponType getWeaponType() {
		return this.type;
	}

	public int getAttackPowerModifier() {
		return this.attackPowerModifier;
	}

	public int getRange() {
		return this.range;
	}

	public int getManaCost() {
		return this.manaCost;
	}

	public StorableObjectType getType() {
		return StorableObjectType.WEAPON;
	}
}
