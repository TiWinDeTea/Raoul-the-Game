package com.github.tiwindetea.raoulthegame.model.spells.passives;

import com.github.tiwindetea.raoulthegame.model.livings.LivingThing;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.github.tiwindetea.raoulthegame.model.spells.Spell;
import com.github.tiwindetea.raoulthegame.view.entities.SpellType;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.Collection;

/**
 * todo class
 */
public class Thorn extends Spell<LivingThing> {

    public Thorn(LivingThing owner) {
        super(owner, SpellType.THORN);
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
        if (source != null) {
            // todo
        }
        return 0;
    }

    @Override
    public double ownerAttacking(@NotNull LivingThing target) {
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
        // todo
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
