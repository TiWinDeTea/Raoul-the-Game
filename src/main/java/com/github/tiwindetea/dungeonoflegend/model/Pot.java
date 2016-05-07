package com.github.tiwindetea.dungeonoflegend.model;

/**
 * Created by maxime on 4/23/16.
 */
public class Pot implements Consumable {

	private byte turns;
	private int heal;
	private int defensePowerModifier;
	private int attackPowerModifier;
	private int mana_heal;
	private int heathModifier;
	private int manaModifier;
	private Player target;

	public Pot(byte turns, int heal, int mana_heal, int defensePowerModifier, int attackPowerModifier, int healthModifier, int manaModifier) {
		this.turns = turns;
		this.heal = heal;
		this.mana_heal = mana_heal;
		this.defensePowerModifier = defensePowerModifier;
		this.attackPowerModifier = attackPowerModifier;
		this.heathModifier = healthModifier;
		this.manaModifier = manaModifier;
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
}
