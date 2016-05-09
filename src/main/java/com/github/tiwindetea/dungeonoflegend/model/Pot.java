package com.github.tiwindetea.dungeonoflegend.model;

import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntityType;

/**
 * Created by maxime on 4/23/16.
 */
public class Pot implements Consumable {

	private int turns;
	private int heal;
	private int defensePowerModifier;
	private int attackPowerModifier;
	private int mana_heal;
	private int heathModifier;
	private int manaModifier;
	private Player target;
	private StaticEntityType gtype;

	public Pot(int turns, int heal, int mana_heal, int defensePowerModifier, int attackPowerModifier, int healthModifier, int manaModifier) {
		this.turns = turns;
		this.heal = heal;
		this.mana_heal = mana_heal;
		this.defensePowerModifier = defensePowerModifier;
		this.attackPowerModifier = attackPowerModifier;
		this.heathModifier = healthModifier;
		this.manaModifier = manaModifier;

		if (defensePowerModifier > 0 || attackPowerModifier > 0 || healthModifier > 0 || manaModifier > 0) {
			this.gtype = StaticEntityType.SUPER_POT;
		} else if (heal == 0) {
			this.gtype = StaticEntityType.HEALING_POT;
		} else {
			this.gtype = StaticEntityType.MANA_POT;
		}
	}

	public void trigger(LivingThing livingThing) {
		if (livingThing.getType() == LivingThingType.PLAYER) {
			this.target = (Player) livingThing;
			this.trigger(this.target);
		} else {
			throw new IllegalArgumentException("Expecting a player");
		}
	}

	public void trigger(Player player) {
		player.addMana(this.mana_heal);
		player.heal(this.heal);
		player.increaseAttack(this.attackPowerModifier);
		player.increaseDefense(this.defensePowerModifier);
		player.increaseHP(this.heathModifier);
		player.increaseMana(this.manaModifier);
	}

	public boolean nextTick() {
		--this.turns;
		this.target.increaseHP(this.heal);
		this.target.addMana(this.mana_heal);
		return this.turns < 0;
	}

	public ConsumableType getConsumableType() {
		return ConsumableType.POT;
	}

	@Override
	public StorableObjectType getType() {
		return StorableObjectType.CONSUMABLE;
	}

	public StaticEntityType getGType() {
		return this.gtype;
	}
}
