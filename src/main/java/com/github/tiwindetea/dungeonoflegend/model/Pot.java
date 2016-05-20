//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.model;

import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntityType;

/**
 * Pot.
 *
 * @author Lucas LAZARE
 */
public class Pot implements Consumable {

	private int turns;
	private int heal;
	private int defensePowerModifier;
	private int attackPowerModifier;
	private int mana_heal;
	private int healthModifier;
	private int manaModifier;
	private String name;
	private Player target;
	private StaticEntityType gtype;

	/**
	 * Instantiates a new Pot.
	 *
	 * @param turns                the turns
	 * @param heal                 the heal
	 * @param mana_heal            the mana heal
	 * @param defensePowerModifier the defense power modifier
	 * @param attackPowerModifier  the attack power modifier
	 * @param healthModifier       the health modifier
	 * @param manaModifier         the mana modifier
	 */
	public Pot(int turns, int heal, int mana_heal, int defensePowerModifier, int attackPowerModifier,
			   int healthModifier, int manaModifier) {
		this.turns = turns;
		this.heal = heal;
		this.mana_heal = mana_heal;
		this.defensePowerModifier = defensePowerModifier;
		this.attackPowerModifier = attackPowerModifier;
		this.healthModifier = healthModifier;
		this.manaModifier = manaModifier;

		if (defensePowerModifier > 0 || attackPowerModifier > 0 || healthModifier > 0 || manaModifier > 0) {
			this.gtype = StaticEntityType.SUPER_POT;
		} else if (heal != 0) {
			this.gtype = StaticEntityType.HEALING_POT;
		} else {
			this.gtype = StaticEntityType.MANA_POT;
		}
	}

	private Pot() {
	}

	/**
	 * Parses a pot.
	 *
	 * @param str a pot's String
	 * @return the pot
	 */
	public static Pot parsePot(String str) {
		if (!str.substring(0, 4).equals("pot=")) {
			throw new IllegalArgumentException("Invoking Pot.parsePot with input string: \"" + str + "\"");
		}
		if (str.equals("pot={null}")) {
			return null;
		}

		/* Computing values' indexes */
		int SEType = str.indexOf("SEType=") + 7;
		int turns = str.indexOf("turns=", SEType) + 6;
		int heal = str.indexOf("heal=", turns) + 5;
		int defenseMod = str.indexOf("defenseMod=", heal) + 11;
		int attackMod = str.indexOf("attackMod=", defenseMod) + 10;
		int mana = str.indexOf("mana=", attackMod) + 5;
		int healthMod = str.indexOf("healthMod=", mana) + 10;
		int manaMod = str.indexOf("manaMod=", healthMod) + 8;

		/* Parsing values */
		Pot pot = new Pot();
		pot.gtype = StaticEntityType.parseStaticEntityType(str.substring(SEType, str.indexOf(',', SEType)));
		pot.turns = Integer.parseInt(str.substring(turns, str.indexOf(',', turns)));
		pot.heal = Integer.parseInt(str.substring(heal, str.indexOf(',', heal)));
		pot.defensePowerModifier = Integer.parseInt(str.substring(defenseMod, str.indexOf(',', defenseMod)));
		pot.attackPowerModifier = Integer.parseInt(str.substring(attackMod, str.indexOf(',', attackMod)));
		pot.mana_heal = Integer.parseInt(str.substring(mana, str.indexOf(',', mana)));
		pot.healthModifier = Integer.parseInt(str.substring(healthMod, str.indexOf(',', healthMod)));
		pot.manaModifier = Integer.parseInt(str.substring(manaMod, str.indexOf(',', manaMod)));
		return pot;
	}

	/**
	 * {@inheritDoc}
	 */
	public void trigger(LivingThing livingThing) {
		if (livingThing.getType() == LivingThingType.PLAYER) {
			this.target = (Player) livingThing;
			this.trigger(this.target);
		} else {
			throw new IllegalArgumentException("Expecting a player");
		}
	}

	/**
	 * Triggers the pot.
	 *
	 * @param player the target of the pot
	 */
	public void trigger(Player player) {
		this.target = player;
		if (this.attackPowerModifier != 0) {
			player.increaseAttack(this.attackPowerModifier);
		}
		if (this.defensePowerModifier != 0) {
			player.increaseDefense(this.defensePowerModifier);
		}
		if (this.healthModifier != 0) {
			player.increaseHP(this.healthModifier);
		}
		if (this.manaModifier != 0) {
			player.increaseMana(this.manaModifier);
		}
		this.healTarget();
		this.manaHealTarget();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean nextTick() {
		if (!this.target.isAlive())
			return true;
		if (this.turns != 0) {
			--this.turns;
			this.healTarget();
			this.manaHealTarget();
			this.target.addMana(this.mana_heal);
			return this.turns <= 0;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public ConsumableType getConsumableType() {
		return ConsumableType.POT;
	}

	@Override
	public StorableObjectType getType() {
		return StorableObjectType.CONSUMABLE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StaticEntityType getGType() {
		return this.gtype;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "pot={SEType=" + this.gtype
				+ ",turns=" + this.turns
				+ ",heal=" + this.heal
				+ ",defenseMod=" + this.defensePowerModifier
				+ ",attackMod=" + this.attackPowerModifier
				+ ",mana=" + this.mana_heal
				+ ",healthMod=" + this.healthModifier
				+ ",manaMod=" + this.manaModifier
				+ ",}";
	}

	private void manaHealTarget() {
		if (this.mana_heal != 0) {
			this.target.addMana(this.mana_heal);
		}
	}

	private void healTarget() {
		if (this.heal != 0) {
			this.target.heal(this.heal);
		}
	}

	@Override
	public String getDescription() {
		if (this.name == null) {
			int i = 0;
			this.name = this.gtype.toString().replaceAll("[0-9]+", "");
			this.name = this.name.substring(0, 1).toUpperCase() + this.name.substring(1);
			while ((i = this.name.indexOf("-")) != -1) {
				this.name = this.name.substring(0, i) + " " + this.name.substring(i + 1, i + 2).toUpperCase() + this.name.substring(i + 2);
			}
			boolean effects = false;
			this.name += "\n\nHealing effects:";
			if (this.heal != 0) {
				this.name += "\nHeal: " + this.heal;
				effects = true;
			}
			if (this.mana_heal != 0) {
				this.name += "\nMana: " + this.mana_heal;
				effects = true;
			}
			if (this.turns != 0) {
				this.name += "\nHeals duration: " + this.turns;
				effects = true;
			}
			if (!effects) {
				this.name += "\nNone.";
			}

			this.name += "\n\nPermanent effects:";
			effects = false;
			if (this.defensePowerModifier != 0) {
				this.name += "\nDefense bonus: " + this.defensePowerModifier;
				effects = true;
			}
			if (this.attackPowerModifier != 0) {
				this.name += "\nAttack bonus: " + this.attackPowerModifier;
				effects = true;
			}
			if (this.healthModifier != 0) {
				this.name += "\nBonus health: " + this.healthModifier;
				effects = true;
			}
			if (!effects) {
				this.name += "\nNone.";
			}
		}
		return this.name;
	}
}
