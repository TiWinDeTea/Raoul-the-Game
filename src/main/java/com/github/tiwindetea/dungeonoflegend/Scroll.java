package com.github.tiwindetea.dungeonoflegend;

/**
 * Created by maxime on 4/23/16.
 */
public class Scroll implements Consumable {

	private int healthModifierPerTick;
	private int healthModifierModifierPerTick;

	public Scroll() {
		//TODO
	}

	public int getHMPT() {
		//TODO
		return 0;
	}

	public int getHMMPT() {
		//TODO
		return 0;
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
