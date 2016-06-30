//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.model.items;

import com.github.tiwindetea.raoulthegame.model.Descriptable;
import com.github.tiwindetea.raoulthegame.model.Pair;
import com.github.tiwindetea.raoulthegame.model.livings.LivingThing;
import com.github.tiwindetea.raoulthegame.model.livings.LivingThingType;
import com.github.tiwindetea.raoulthegame.model.livings.Player;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;

/**
 * Created by maxime on 4/24/16.
 */
public class InteractiveObject implements Descriptable {

    private boolean isTrap;
    private double manaModifier;
    private double hpModifier;
    private StorableObject loot;
    private Vector2i position;

    /**
     * Instantiates a new Interactive object that can be used to find an Interactive object by comparison.
     *
     * @param position the position
     */
    public InteractiveObject(Vector2i position) {
        this.position = position;
    }

    /**
     * Instantiates a new Interactive object (chest).
     *
     * @param position the position
     * @param loot     the loot
     */
    public InteractiveObject(Vector2i position, StorableObject loot) {
        this.position = position;
        this.loot = loot;
        this.isTrap = false;
    }

    /**
     * Instantiates a new Interactive object (trap).
     *
     * @param position     the position
     * @param hpModifier   the hp modifier
     * @param manaModifier the mana modifier
     */
    public InteractiveObject(Vector2i position, double hpModifier, double manaModifier) {
        this.position = position;
        this.hpModifier = hpModifier;
        this.manaModifier = manaModifier;
        this.isTrap = true;
    }

    /**
     * Triggers the interactive object.
     *
     * @param livingThing the target
     * @return true if the Interactive object should be removed, false otherwise
     */
    public boolean trigger(LivingThing livingThing) {
        if (this.isTrap) {
            livingThing.damage(this.hpModifier);
            if (livingThing.getType() == LivingThingType.PLAYER)
                ((Player) livingThing).useMana(this.manaModifier);
            return true;
        } else if (livingThing.getType() == LivingThingType.PLAYER) {
            return ((Player) livingThing).addToInventory(new Pair<>(this.loot));
        } else {
            return false;
        }
    }

    /**
     * Getter.
     *
     * @return true if the interactive object is a trap, false otherwise.
     */
    public boolean isTrap() {
        return this.isTrap;
    }

    /**
     * Gets position.
     *
     * @return the position
     */
    public Vector2i getPosition() {
        return this.position;
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        /* asserting o to be an interactive object */
        return this.equals((InteractiveObject) o);
    }

    /**
     * Equals boolean.
     *
     * @param io the Intercative object
     * @return true if this and io have the same position.
     */
    public boolean equals(InteractiveObject io) {
        return this.position.equals(io.position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        if (this.isTrap) {
            return "A dangerous oil puddle";
        } else {
            return "A mysterious gift";
        }
    }
}
