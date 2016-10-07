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
public class BonusHP extends Spell {

    public static final int BASE_HP = 20;
    public static final int HP_PER_LEVEL = 10;

    private int currentUp = 0;
    private WeakReference<Player> p = null;

    public BonusHP(LivingThing owner) {
        super(owner, SpellType.BONUS_HP);
        if (owner.getType() == LivingThingType.PLAYER) {
            Player e = (Player) owner;
            this.p = new WeakReference<>(e);
            e.increaseHP(BASE_HP);
        }
        this.currentUp = BASE_HP;
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
        Player p = this.p.get();
        if (p != null) {
            p.increaseHP(HP_PER_LEVEL);
            this.currentUp += HP_PER_LEVEL;
        }
    }

    @Override
    public boolean cast(Collection<LivingThing> targets, Vector2i sourcePosition) {
        return false;
    }

    @Override
    public void nextFloor() {
        Player p = this.p.get();
        if (p != null) {
            p.heal(p.getMaxHitPoints() / 10);
            p.addMana(p.getMaxMana() / 10);
        }
    }

    @Override
    public void forgotten() {
        Player p = this.p.get();
        if (p != null) {
            p.increaseHP(-this.currentUp);
            this.currentUp = 0;
        }
    }
}