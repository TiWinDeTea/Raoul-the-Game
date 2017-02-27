//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.model.spells.passives;

import com.github.tiwindetea.raoulthegame.events.game.spells.SpellCooldownUpdateEvent;
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
public class Savior extends Spell<LivingThing> {
    // TRIGGERS BEFORE THE HIT, NOT AFTER
    private static final int BASE_COOLDOWN = 5000; //(turns)
    private static final double BASE_HP_REGEN = 5;
    private static final double PERCENTAGE_HP_REGEN = 10;
    private static final double TRIGGER_TRESHOLD_PERCENTAGE = 5;
    private static final double TRIGGER_TRESHOLD_HEALTH = 3;
    //will regen 5 + 10% of the max hp upon reaching less than 5% of max hp or less than 3 hp

    private int baseCooldown = BASE_COOLDOWN;
    private int cooldown = this.baseCooldown;

    private final int pid;

    public Savior(LivingThing owner) {
        super(owner, Spell.firstNull(owner.getSpells()));

        if (LivingThingType.PLAYER.equals(owner.getType())) {
            this.pid = ((Player) owner).getNumber();
            updateDescription();
            fire(new SpellCreationEvent(
                    this.pid,
                    this.id,
                    getSpellType(),
                    this.baseCooldown,
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
        if (this.cooldown <= 0) {
            LivingThing owner = this.getOwner();
            if (owner != null) {
                double diff;
                double O_MAX_HP = owner.getMaxHitPoints();
                double O_HP = owner.getHitPoints();
                if (damages > 0) {
                    diff = Math.max(-1, owner.getDefensePower() - damages);
                } else {
                    diff = Math.min(O_MAX_HP - O_HP, -damages);
                }
                diff += O_HP;
                if (diff < O_MAX_HP * TRIGGER_TRESHOLD_PERCENTAGE / 100 || diff < TRIGGER_TRESHOLD_HEALTH) {
                    this.cooldown = this.baseCooldown;
                    owner.damage(-damages, null);
                    owner.damage(-(O_MAX_HP * PERCENTAGE_HP_REGEN / 100 + BASE_HP_REGEN + 1), null);
                    if (this.pid != -1) {
                        fire(new SpellCooldownUpdateEvent(
                                this.pid,
                                this.id,
                                this.baseCooldown,
                                this.cooldown
                        ));
                    }
                }
            }
        }
        return -damages;
    }

    @Override
    public double ownerAttacking(@Nonnull LivingThing target) {
        return 0;
    }

    @Override
    public void update(Collection<LivingThing> targets) {
        if (this.cooldown > 0) {
            --this.cooldown;
            if (this.pid != -1) {
                fire(new SpellCooldownUpdateEvent(
                        this.pid,
                        this.id,
                        this.baseCooldown,
                        this.cooldown
                ));
            }
        }
    }

    @Override
    public void nextOwnerLevel() {

    }

    @Override
    public void spellUpgraded() {
        this.cooldown = 0;
        this.baseCooldown = Math.max(1, this.baseCooldown - 100);
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
        return SpellType.SAVIOR;
    }

    private void updateDescription() {
        this.description = "Savior (passive).\n" +
                "Heals you when you are about to die.\n" +
                "Cooldown: " + this.baseCooldown;
    }
}
