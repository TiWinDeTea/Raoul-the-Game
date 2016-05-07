package com.github.tiwindetea.dungeonoflegend.model;

/**
 * Created by maxime on 4/23/16.
 */
public class Weapon implements StorableObject {

	private int attackPowerModifier;
	private int range;
	private int manaCost;
	private WeaponType type;

	public Weapon(int attackPowerModifier, int range, int manaCost) {
		this.attackPowerModifier = attackPowerModifier;
		this.range = range;
		this.manaCost = manaCost;
		if (manaCost != 0) {
			this.type = WeaponType.WAND;
		} else if (range != 0) {
			this.type = WeaponType.BOW;
		} else {
			this.type = WeaponType.SWORD;
		}
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
