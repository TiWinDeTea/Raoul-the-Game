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
public class Regen extends Spell<LivingThing> {

    private int turn = 0;
    private double healQtt = 5;

    private static final int CD_TURNS = 10;

    public Regen(LivingThing owner) {
        super(owner, SpellType.REGEN);
        updateDescription();
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
        return 0;
    }

    @Override
    public void update(Collection<LivingThing> targets) {
        ++this.turn;
        if (this.turn % CD_TURNS == 0) {
            LivingThing owner = super.getOwner();
            if (owner != null) {
                owner.damage(-this.healQtt, null);
            }
        }
    }

    @Override
    public void nextOwnerLevel() {
        this.turn = 1;
    }

    @Override
    public void nextSpellLevel() {
        this.healQtt += 5;
        updateDescription();
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

    private void updateDescription() {
        description = "Rejuvenate (passive).\n" +
                "Passively regenerate your health (" + this.healQtt + "hp per turn)";
    }
}