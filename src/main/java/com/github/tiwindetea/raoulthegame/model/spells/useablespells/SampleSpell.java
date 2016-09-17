//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.model.spells.useablespells;

import com.github.tiwindetea.raoulthegame.model.livings.LivingThing;
import com.github.tiwindetea.raoulthegame.model.livings.Player;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.github.tiwindetea.raoulthegame.model.spells.Spell;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.Collection;

/**
 * Created by organic-code on 6/30/16.
 */
public class SampleSpell extends Spell {

    private int cooldown = 0;
    private static final int MAXCOOLDOWN = 5;
    private int level = 0;
    private double damages = 64;
    private int heal = 1;


    /**
     * Instantiates a new Spell.
     *
     * @param owner        the spell's owner
     */
    public SampleSpell(Player owner) {
        super(owner, 2, owner.getLos(), 2, "A wololo spell", null);
    }

    @Override
    public boolean isAOE() {
        return false;
    }

    @Override
    public boolean isPassive() {
        return false;
    }

    @Override
    public Vector2i getSpellSource() {
        return null;
    }

    @Override
    public double ownerDamaged(@Nullable LivingThing source, double damages) {
        return 0;
    }

    @Override
    public double ownerAttacking(@NotNull LivingThing target) {
        return 0;
    }

    @Override
    public void update(Collection<LivingThing> targets) {
        if (this.cooldown > 0) {
            --this.cooldown;
        }
    }

    @Override
    public void nextOwnerLevel() {
        this.cooldown = 0;
    }

    @Override
    public void nextSpellLevel() {
        this.cooldown = 0;
        this.heal++;
    }

    @Override
    public boolean cast(Collection<LivingThing> targets, Vector2i sourcePosition) {
        LivingThing owner = this.getOwner();
        if (owner != null) {
            for (LivingThing target : targets) {
                target.damage(this.damages, owner);
            }
            owner.damage(-this.heal, owner);
            this.cooldown = MAXCOOLDOWN;
        }
        return false;
    }
}
