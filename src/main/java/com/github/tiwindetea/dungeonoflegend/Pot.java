package com.github.tiwindetea.dungeonoflegend;

/**
 * Created by maxime on 4/23/16.
 */
public class Pot implements Consumable {

	private byte turns;
	private int healthModifier;
	private int defensePowerModifier;
	private int attackPowerModifier;

	public Pot(byte turns, int healthModifier, int defensePowerModifier, int attackPowerModifier) {
		this.turns = turns;
		this.healthModifier = healthModifier;
		this.defensePowerModifier = defensePowerModifier;
		this.attackPowerModifier = attackPowerModifier;
	}

	public int getHM() {
		return this.healthModifier;
	}

	public int getDPM() {
		return this.defensePowerModifier;
	}

	public int getAPM() {
		return this.attackPowerModifier;
	}

	@Override
	public void trigger(LivingThing livingThing) {
		//TODO
	}

	@Override
	public boolean nextTick() {
		//TODO
		return false;
	}

	@Override
	public ConsumableType getConsumableType() {
		//TODO
		return null;
	}
}
