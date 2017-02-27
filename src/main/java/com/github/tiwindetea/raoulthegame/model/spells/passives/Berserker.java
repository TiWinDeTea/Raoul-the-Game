//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.model.spells.passives;

import com.github.tiwindetea.raoulthegame.events.game.spells.SpellCreationEvent;
import com.github.tiwindetea.raoulthegame.events.game.spells.SpellDeletionEvent;
import com.github.tiwindetea.raoulthegame.events.game.spells.SpellDescriptionUpdateEvent;
import com.github.tiwindetea.raoulthegame.model.livings.LivingThing;
import com.github.tiwindetea.raoulthegame.model.livings.LivingThingType;
import com.github.tiwindetea.raoulthegame.model.livings.Player;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.github.tiwindetea.raoulthegame.model.spells.Spell;
import com.github.tiwindetea.raoulthegame.view.entities.SpellType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Created by Lucas on 01/09/2016.
 */
public class Berserker extends Spell<LivingThing> {

    private double damageUpPercentage = 10;
    private static final double DAMAGE_UP_PERCENTAGE_PER_LEVEL = 2;

    private double defenseDownPercentage = 5;
    private static final double DEFENSE_DOWN_PERCENTAGE_PER_LEVEL = 1;

    private final int pid;

    public Berserker(LivingThing owner) {
        super(owner, Spell.firstNull(owner.getSpells()));
        if (LivingThingType.PLAYER.equals(owner.getType())) {
            updateDescription();
            this.pid = ((Player) owner).getNumber();
            fire(new SpellCreationEvent(
                    this.pid,
                    this.id,
                    getSpellType(),
                    0,
                    this.description
            ));
        } else {
            this.pid = -1;
        }
    }

    @Override
    public boolean isAOE() {
        return false;
    }

    @Override
    public boolean isPassive() {
        return true;
    }

    @Override
    public Vector2i getSpellSource() {
        return null;
    }

    @Override
    public double ownerDamaged(@Nullable LivingThing source, double damages) {
        return damages * this.defenseDownPercentage / 100;
    }

    @Override
    public double ownerAttacking(@Nonnull LivingThing target) {
        LivingThing owner = this.getOwner();
        if (owner != null) {
            return owner.getAttackPower() * this.damageUpPercentage / 100;
        }
        return 0;
    }

    @Override
    public void update(Collection<LivingThing> targets) {

    }

    @Override
    public void nextOwnerLevel() {

    }

    @Override
    public void spellUpgraded() {
        this.damageUpPercentage += Berserker.DAMAGE_UP_PERCENTAGE_PER_LEVEL;
        this.defenseDownPercentage += Berserker.DEFENSE_DOWN_PERCENTAGE_PER_LEVEL;
        if (this.pid != -1) {
            updateDescription();
            fire(new SpellDescriptionUpdateEvent(
                    this.pid,
                    this.id,
                    this.description
            ));
        }
    }

    @Override
    public boolean cast(Collection<LivingThing> targets, Vector2i sourcePosition) {
        return false;
    }

    @Override
    public void nextFloor() {

    }

    @Override
    protected void forgotten() {
        if (this.pid != -1) {
            fire(new SpellDeletionEvent(
                    this.pid,
                    this.id
            ));
        }
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.BERSERKER;
    }

    private void updateDescription() {
        this.description = "Berserker (passive).\nYou deal +" + DECIMAL.format(this.damageUpPercentage)
                + "% increased damage, but also suffer from +" + DECIMAL.format(this.defenseDownPercentage)
                + "% increased damage";
    }
}
