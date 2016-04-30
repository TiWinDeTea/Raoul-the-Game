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
    private Vector2i position;

    public InteractiveObject(boolean isTrap, Vector2i position) {
        this.isTrap = isTrap;
        this.position = position;
    }

    public boolean trigger(LivingThing livingThing) {
        if (this.isTrap) {
            livingThing.damage(this.hpModifier);
            if (livingThing.getType() == LivingThingType.PLAYER)
                ((Player) livingThing).useMana(this.manaModifier);
            return true;
        } else if (livingThing.getType() == LivingThingType.PLAYER) {
            ((Player) livingThing).addToInventory(this.loot);
            return true;
        } else {
            return false;
        }
    }

    public boolean isActivableOn(LivingThingType type) {
        return this.isTrap || type == LivingThingType.PLAYER;
    }

    public Vector2i getPosition() {
        return this.position;
    }

    public void print() {
        //TODO
    }
}
