package com.github.tiwindetea.raoulthegame.model.livings;

import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;

/**
 * Created by organic-code on 7/3/16.
 */
public abstract class Pet extends LivingThing {
    protected WeakReference<LivingThing> owner;
    protected LivingThing target;

    public Pet(LivingThing owner, String name, int level, double maxHitPoints, double attackPower, double defensePower, @NotNull Vector2i position) {
        super();
        this.name = name;
        this.level = level;
        this.maxHitPoints = maxHitPoints;
        this.hitPoints = maxHitPoints;
        this.attackPower = attackPower;
        this.defensePower = defensePower;
        this.position = position.copy();
        this.owner = new WeakReference<>(owner);
    }

    protected LivingThing getOwner() {
        return this.owner.get();
    }

    @Override
    public final LivingThingType getType() {
        return LivingThingType.PET;
    }

    /**
     * This function should be called when you consider this should be upgraded
     */
    public abstract void levelUp();

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


    @Override
    public void live(List<Mob> mobs, Collection<Player> players, Collection<LivingThing> others, boolean[][] los) {
        this.live(mobs, players, null);
    }

    public abstract void live(List<Mob> mobs, Collection<Player> players, Collection<LivingThing> all);
}
