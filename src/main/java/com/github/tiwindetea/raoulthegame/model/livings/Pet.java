package com.github.tiwindetea.raoulthegame.model.livings;

import com.github.tiwindetea.raoulthegame.model.Pair;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.lang.ref.WeakReference;

/**
 * Created by organic-code on 7/3/16.
 */
public abstract class Pet extends LivingThing {
    protected WeakReference<Player> owner;
    protected Mob target;

    public Pet(String name, int level, double maxHitPoints, double attackPower, double defensePower, @NotNull Vector2i position) {
        this.name = name;
        this.level = level;
        this.maxHitPoints = maxHitPoints;
        this.attackPower = attackPower;
        this.defensePower = defensePower;
        this.position = position.copy();
        this.id = Pair.getUniqueId();
    }

    protected Player getOwner() {
        return this.owner.get();
    }

    @Override
    public Vector2i getRequestedMove() {
        return this.getOwner().getPosition();
    }

    @Override
    public LivingThingType getType() {
        return LivingThingType.PET;
    }


    /**
     * Handler. This function should be called each time the pet's owner is attacked
     *
     * @param source the source of the damages
     */
    public abstract void ownerDamaged(@Nullable LivingThing source);

    /**
     * Handler. This function should be called time the pet's owner is attacking
     *
     * @param target the damages' target
     */
    public abstract void ownerAttacking(@NotNull LivingThing target);
}
