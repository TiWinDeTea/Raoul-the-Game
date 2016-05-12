//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.model;

/**
 * Created by maxime on 4/24/16.
 */
public class InteractiveObject {

	private boolean isTrap;
	private int manaModifier;
	private int hpModifier;
	private StorableObject loot;
    private Vector2i position;

    public InteractiveObject(Vector2i position) {
        this.position = position;
    }

    public InteractiveObject(Vector2i position, StorableObject loot) {
        this.position = position;
        this.loot = loot;
        this.isTrap = false;
    }

    public InteractiveObject(Vector2i position, int hpModifier, int manaModifier) {
        this.position = position;
        this.hpModifier = hpModifier;
        this.manaModifier = manaModifier;
        this.isTrap = true;
    }

    public boolean trigger(LivingThing livingThing) {
        if (this.isTrap) {
            livingThing.damage(this.hpModifier);
            if (livingThing.getType() == LivingThingType.PLAYER)
                ((Player) livingThing).useMana(this.manaModifier);
            return true;
        } else if (livingThing.getType() == LivingThingType.PLAYER) {
            //((Player) livingThing).addToInventory(this.loot);
            return true;
        } else {
            return false;
        }
    }

    public boolean isTrap() {
        return this.isTrap;
    }

    public Vector2i getPosition() {
        return this.position;
    }

    public boolean equals(Object o) {
        /* asserting o to be an interactive object */
        return this.equals((InteractiveObject) o);
    }

    public boolean equals(InteractiveObject io) {
        return this.position.equals(io.position);
    }
}
