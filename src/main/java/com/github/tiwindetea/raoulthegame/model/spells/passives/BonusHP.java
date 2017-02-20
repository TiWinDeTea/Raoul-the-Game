package com.github.tiwindetea.raoulthegame.model.spells.passives;

import com.github.tiwindetea.raoulthegame.model.livings.LivingThing;
import com.github.tiwindetea.raoulthegame.model.livings.LivingThingType;
import com.github.tiwindetea.raoulthegame.model.livings.Player;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.github.tiwindetea.raoulthegame.model.spells.Spell;
import com.github.tiwindetea.raoulthegame.view.entities.SpellType;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.lang.ref.WeakReference;
import java.util.Collection;

/**
 * Created by Lucas on 01/09/2016.
 */
public class BonusHP extends Spell<Player> {

    public static final double BASE_HP = 20;
    public static final double HP_PER_LEVEL = 10;

    private double currentUp = 0;

    public BonusHP(Player owner) {
        super(owner, SpellType.BONUS_HP);
        owner.increaseHP(BASE_HP);
        this.currentUp = BASE_HP;
        updateDescription();
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

    }

    @Override
    public void nextOwnerLevel() {

    }

    @Override
    public void nextSpellLevel() {
        Player p = getOwner();
        if (p != null) {
            p.increaseHP(HP_PER_LEVEL);
            this.currentUp += HP_PER_LEVEL;
        }
        updateDescription();
    }

    @Override
    public boolean cast(Collection<LivingThing> targets, Vector2i sourcePosition) {
        return false;
    }

    @Override
    public void nextFloor() {
        Player p = getOwner();
        if (p != null) {
            p.heal(p.getMaxHitPoints() / 10);
            p.addMana(p.getMaxMana() / 10);
        }
    }

    @Override
    public void forgotten() {
        Player p = getOwner();
        if (p != null) {
            p.increaseHP(-this.currentUp);
            this.currentUp = 0;
        }
    }

    private void updateDescription() {
        description = "An apple a day keeps the doctor away\n" +
                "    (especially if you aim carefully).\n" +
                "Give you some extra HP\n" +
                "Current bonus: " + DECIMAL.format(this.currentUp) + "HP";
    }
}