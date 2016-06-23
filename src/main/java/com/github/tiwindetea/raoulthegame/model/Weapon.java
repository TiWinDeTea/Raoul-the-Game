//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.model;

import com.github.tiwindetea.raoulthegame.view.entities.StaticEntityType;

import java.util.Random;

/**
 * Weapon.
 *
 * @author Lucas LAZARE
 */
public class Weapon implements StorableObject {

	private double attackPowerModifier;
	private int range;
	private double manaCost;
	private WeaponType type;
	String name;

	private StaticEntityType gtype;

	/**
	 * Instantiates a new Weapon.
	 *
	 * @param attackPowerModifier the attack power modifier
	 * @param range               the range
	 * @param manaCost            the mana cost
	 */
	public Weapon(double attackPowerModifier, int range, double manaCost) {
		this.attackPowerModifier = attackPowerModifier;
		this.range = range;
		this.manaCost = manaCost;
		Random random = new Random();

		/* Setting the type and the graphical type */
		if (manaCost > 0) {
			this.type = WeaponType.WAND;
			if (random.nextBoolean())
				this.gtype = StaticEntityType.WAND1;
			else
				this.gtype = StaticEntityType.WAND2;
		} else if (range > 1) {
			this.type = WeaponType.BOW;
			if (random.nextBoolean()) {
				this.gtype = StaticEntityType.BOW1;
			} else {
				this.gtype = StaticEntityType.BOW2;
			}
		} else {
			this.type = WeaponType.SWORD;
			if (random.nextBoolean()) {
				this.gtype = StaticEntityType.SWORD1;
			} else {
				this.gtype = StaticEntityType.SWORD2;
			}
		}
		this.setName();
	}

	private Weapon() {

	}

	/**
	 * Parses a weapon.
	 *
	 * @param str a weapon's String
	 * @return the weapon
	 */
	public static Weapon parseWeapon(String str) {
		if (!str.substring(0, 7).equals("weapon=")) {
			throw new IllegalArgumentException("Invoking Weapon.parseWeapon with input string: \"" + str + "\"");
		}
		if (str.equals("weapon={null}")) {
			return null;
		}
		/* Computing values' indexes */
		int SEType = str.indexOf("SEType=") + 7;
		int type = str.indexOf("type=", SEType) + 5;
		int attack = str.indexOf("attack=", type) + 7;
		int range = str.indexOf("range=", attack) + 6;
		int mana = str.indexOf("mana=", range) + 5;

		/* Parsing values */
		Weapon weapon = new Weapon();
		weapon.gtype = StaticEntityType.parseStaticEntityType(str.substring(SEType, str.indexOf(',', SEType)));
		weapon.type = WeaponType.parseWeaponType(str.substring(type, str.indexOf(',', type)));
		weapon.attackPowerModifier = Double.parseDouble(str.substring(attack, str.indexOf(',', attack)));
		weapon.range = Integer.parseInt(str.substring(range, str.indexOf(',', range)));
		weapon.manaCost = Double.parseDouble(str.substring(mana, str.indexOf(',', mana)));
		weapon.setName();
		return weapon;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StaticEntityType getGType() {
		return this.gtype;
	}

	/**
	 * Gets the weapon type.
	 *
	 * @return the weapon type
	 * @see WeaponType
	 */
	public WeaponType getWeaponType() {
		return this.type;
	}

	/**
	 * Gets the attack power modifier.
	 *
	 * @return the attack power modifier
	 */
	public double getAttackPowerModifier() {
		return this.attackPowerModifier;
	}

	/**
	 * Gets the range.
	 *
	 * @return the range
	 */
	public int getRange() {
		return this.range;
	}

	/**
	 * Gets the mana cost.
	 *
	 * @return the mana cost
	 */
	public double getManaCost() {
		return this.manaCost;
	}

	/**
	 * {@inheritDoc}
	 */
	public StorableObjectType getType() {
		return StorableObjectType.WEAPON;
	}

	private void setName() {
		switch (this.gtype) {
			case BOW1:
				this.name = "Bow";
				break;
			case BOW2:
				this.name = "Crossbow";
				break;
			case SWORD1:
				this.name = "Sword";
				break;
			case SWORD2:
				this.name = "Axe";
				break;
			case WAND1:
				/* Falls through */
			case WAND2:
				this.name = "Magic wand";
				break;
			default:
				break;
		}
		this.name += "\n\nAttack power: " + (int) this.attackPowerModifier;
		this.name += "\nRange: " + this.range;
		this.name += "\nMana cost: " + (int) this.manaCost;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "weapon={SEType=" + this.gtype
				+ ",type=" + this.type
				+ ",attack=" + this.attackPowerModifier
				+ ",range=" + this.range
				+ ",mana=" + this.manaCost
				+ ",}";
	}

	/**
	 * {@inheritDoc}
     */
	@Override
	public String getDescription() {
		return this.name;
	}

	/**
	 * Computes the powergrade of this object
	 *
	 * @return the powergrade
	 */
	public int powerGrade() {
		return (int) (5 * (this.attackPowerModifier / 11) + this.range / 3);
	}
}