package com.github.tiwindetea.dungeonoflegend.model;

/**
 * Created by maxime on 4/23/16.
 */
public class Scroll implements Consumable {

	private byte turns;
	private int healthModifierPerTick;
	private int healthModifierModifierPerTick;
	private LivingThing target;

	public Scroll(byte turns, int healthModifierPerTick, int healthModifierModifierPerTick) {
		this.turns = turns;
		this.healthModifierPerTick = healthModifierPerTick;
		this.healthModifierModifierPerTick = healthModifierModifierPerTick;
	}

	public void trigger(LivingThing livingThing) {
		this.target = livingThing;
		livingThing.damage(this.healthModifierPerTick);
	}

	public boolean nextTick() {
		--this.turns;
		this.healthModifierModifierPerTick += this.healthModifierModifierPerTick;
		this.target.damage(this.healthModifierPerTick);
		return this.turns < 0;
	}

	public ConsumableType getConsumableType() {
		return ConsumableType.SCROLL;
	}
}
