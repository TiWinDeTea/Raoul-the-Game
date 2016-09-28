package com.github.tiwindetea.raoulthegame.model.spells.passives;

import com.github.tiwindetea.raoulthegame.model.livings.LivingThing;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.github.tiwindetea.raoulthegame.model.spells.Spell;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.Collection;

/**
 * Created by Lucas on 01/09/2016.
 */
public class Savior extends Spell {
    // TRIGGERS BEFORE THE HIT, NOT AFTER
    private static final int BASE_COOLDOWN = 500; //(turns)
    private static final double BASE_HP_REGEN = 5;
    private static final double PERCENTAGE_HP_REGEN = 10;
    private static final double TRIGGER_TRESHOLD_PERCENTAGE = 5;
    private static final double TRIGGER_TRESHOLD_HEALTH = 3;
    //will regen 5 + 10% of the max hp upon reaching less than 5% of max hp or less than 3 hp

    private int cd = BASE_COOLDOWN;

    public Savior(LivingThing owner) {
        super(owner, null);
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
        if (this.cd <= 0) {
            LivingThing owner = this.getOwner();
            if (owner != null) {
                double diff;
                final double O_MAX_HP = owner.getMaxHitPoints();
                final double O_HP = owner.getHitPoints();
                if (damages > 0) {
                    double tmp = 0;
                    for (Spell spell : owner.getSpells()) {
                        tmp += spell.ownerDamaged(source, damages);
                    }
                    damages += tmp;
                    diff = owner.getDefensePower() - damages;
                    if (diff >= -1) {
                        diff = -1;
                    }
                } else {
                    diff = Math.min(O_MAX_HP - O_HP, -damages);
                }
                diff += O_HP;
                if (diff < O_MAX_HP * TRIGGER_TRESHOLD_PERCENTAGE / 100 || diff < TRIGGER_TRESHOLD_HEALTH) {
                    this.cd = BASE_COOLDOWN;
                    owner.damage(damages, null);
                    owner.damage(-(O_MAX_HP * PERCENTAGE_HP_REGEN / 100 + BASE_HP_REGEN + 1), null);
                }
            }
        }
        return -damages;
    }

    @Override
    public double ownerAttacking(@NotNull LivingThing target) {
        return 0;
    }

    @Override
    public void update(Collection<LivingThing> targets) {
        if (this.cd > 0) {
            --this.cd;
        }
    }

    @Override
    public void nextOwnerLevel() {

    }

    @Override
    public void nextSpellLevel() {
        this.cd /= 2;
    }

    @Override
    public boolean cast(Collection<LivingThing> targets, Vector2i sourcePosition) {
        return false;
    }
}
