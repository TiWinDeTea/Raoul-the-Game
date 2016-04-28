package com.github.tiwindetea.dungeonoflegend;

/**
 * Created by maxime on 4/24/16.
 */
public class InteractiveObject {

	private boolean isTrap;
	private int manaModifier;
	private int hpModifier;
	private StorableObject loot;
	private LivingThingType target;

	public InteractiveObject(boolean isTrap) {
		this.isTrap = isTrap;
	}

	public void triger(LivingThing livingThing) {
		//TODO
	}

	public boolean isActivableOn(LivingThingType type) {
		//TODO
		return false;
	}

	public void print() {
		//TODO
	}
}
