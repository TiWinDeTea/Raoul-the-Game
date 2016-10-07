package com.github.tiwindetea.raoulthegame.model.spells.passives;

import com.github.tiwindetea.raoulthegame.model.livings.LivingThing;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.github.tiwindetea.raoulthegame.model.spells.Spell;
import com.github.tiwindetea.raoulthegame.view.entities.SpellType;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.Collection;

/**
 * Created by Lucas on 01/09/2016.
 */
public class Drainer extends Spell {

    private double stealPercentage = 5;
    private static final double STEAL_PERCENTAGE_PER_LEVEL = 2;

    public Drainer(LivingThing owner) {
        super(owner, SpellType.DRAINER);
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
        return 0;
    }

    @Override
    public double ownerAttacking(@NotNull LivingThing target) {
        double outputDamages = 0;
        LivingThing owner = this.getOwner();
        if (owner != null) {
            double ownerAttackPower = owner.getAttackPower();
            if (ownerAttackPower > 0) {
                for (Spell spell : target.getSpells()) {
                    outputDamages += spell.ownerDamaged(owner, ownerAttackPower);
                }
                outputDamages = outputDamages + ownerAttackPower - target.getDefensePower();
                if (outputDamages < 1) {
                    outputDamages = 1;
                }
            } else {
                outputDamages = -Math.min(target.getMaxHitPoints() - target.getHitPoints(), -ownerAttackPower);
            }
            owner.damage(-outputDamages * this.stealPercentage / 100, null);
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
    public void nextSpellLevel() {
        this.stealPercentage += Drainer.STEAL_PERCENTAGE_PER_LEVEL;
    }

    @Override
    public boolean cast(Collection<LivingThing> targets, Vector2i sourcePosition) {
        return false;
    }

    @Override
    public void nextFloor() {

    }

    @Override
    public void forgotten() {

    }
}
