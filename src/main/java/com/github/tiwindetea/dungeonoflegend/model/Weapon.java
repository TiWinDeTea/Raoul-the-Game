//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

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

	private Weapon() {

	}

	public static Weapon parseWeapon(String str) {
		if (str.equals("weapon={null}")) {
			return null;
		}
		int SEType = str.indexOf("SEType=") + 7;
		int type = str.indexOf("type=", SEType) + 5;
		int attack = str.indexOf("attack=", type) + 7;
		int range = str.indexOf("range=", attack) + 6;
		int mana = str.indexOf("mana=", range) + 5;

		Weapon weapon = new Weapon();
		weapon.gtype = StaticEntityType.parseStaticEntityType(str.substring(SEType, str.indexOf(',', SEType)));
		weapon.type = WeaponType.parseWeaponType(str.substring(type, str.indexOf(',', type)));
		weapon.attackPowerModifier = Integer.parseInt(str.substring(attack, str.indexOf(',', attack)));
		weapon.range = Integer.parseInt(str.substring(range, str.indexOf(',', range)));
		weapon.manaCost = Integer.parseInt(str.substring(mana, str.indexOf(',', mana)));
		return weapon;
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

	@Override
	public String toString() {
		return "weapon={SEType=" + this.gtype
				+ ",type=" + this.type
				+ ",attack=" + this.attackPowerModifier
				+ ",range=" + this.range
				+ ",mana=" + this.manaCost
				+ ",}";
	}
}