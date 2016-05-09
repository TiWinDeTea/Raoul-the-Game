package com.github.tiwindetea.dungeonoflegend.model;

import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntityType;

import java.util.Random;

/**
 * Created by maxime on 4/23/16.
 */
public class Scroll implements Consumable {

	private int turns;
	private int healthModifierPerTick;
	private int healthModifierModifierPerTick;
	private LivingThing target;
	private StaticEntityType gtype;

	public Scroll(int turns, int healthModifierPerTick, int healthModifierModifierPerTick) {
		this.turns = turns;
		this.healthModifierPerTick = healthModifierPerTick;
		this.healthModifierModifierPerTick = healthModifierModifierPerTick;
		Random random = new Random();
		if (random.nextBoolean()) {
			this.gtype = StaticEntityType.SCROLL1;
		} else {
			this.gtype = StaticEntityType.SCROLL2;
		}
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

	@Override
	public StorableObjectType getType() {
		return StorableObjectType.CONSUMABLE;
	}

	public StaticEntityType getGtype() {
		return this.gtype;
	}
}
