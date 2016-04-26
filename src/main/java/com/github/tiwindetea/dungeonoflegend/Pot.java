package com.github.tiwindetea.dungeonoflegend;

/**
 * Created by maxime on 4/23/16.
 */
public class Pot implements Consumable {

	private int healthModifier;
	private int defensePowerModifier;
	private int attackPowerModifier;

	public Pot() {
		//TODO
	}

	public int getHM() {
		//TODO
		return 0;
	}

	public int getDPM() {
		//TODO
		return 0;
	}

	public int getAPM() {
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
