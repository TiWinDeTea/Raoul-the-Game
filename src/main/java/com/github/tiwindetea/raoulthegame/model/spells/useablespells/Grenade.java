package com.github.tiwindetea.raoulthegame.model.spells.useablespells;

import com.github.tiwindetea.raoulthegame.model.livings.LivingThing;
import com.github.tiwindetea.raoulthegame.model.livings.Player;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.github.tiwindetea.raoulthegame.model.spells.Spell;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.Collection;

/**
 * todo class
 */
public class Grenade extends Spell<Player> {

    public Grenade(Player owner) {
        super(owner, owner.getSpells().size());
    }

    @Override
    public boolean isAOE() {
        return true;
    }

    @Override
    public boolean isPassive() {
        return false;
    }

    @Override
    public Vector2i getSpellSource() {
        // todo
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
        // todo
    }

    @Override
    public void nextOwnerLevel() {

    }

    @Override
    public void nextSpellLevel() {

    }

    @Override
    public boolean cast(Collection<LivingThing> targets, Vector2i sourcePosition) {
        // todo
        return false;
    }

    @Override
    public void nextFloor() {

    }

    @Override
    public void forgotten() {
        // todo
    }
}
