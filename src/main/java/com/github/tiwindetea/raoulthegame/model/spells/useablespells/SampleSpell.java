package com.github.tiwindetea.raoulthegame.model.spells.useablespells;

import com.github.tiwindetea.raoulthegame.model.livings.LivingThing;
import com.github.tiwindetea.raoulthegame.model.spells.Spell;

import java.util.Collection;

/**
 * Created by organic-code on 6/30/16.
 */
public class SampleSpell extends Spell {

    private int cooldown = 0;
    private static final int MAXCOOLDOWN = 5;
    private int level = 0;
    private double damages = 64;
    private int heal = 1;


    /**
     * Instantiates a new Spell.
     *
     * @param owner        the spell's owner
     * @param targetNumber the maximum number of target this spell can be used on. If set to 0, all LivingThing within range are passed as targets when the spell is casted
     * @param range        the range of this spell
     * @param description  the description of this spell
     */
    public SampleSpell(LivingThing owner, int targetNumber, int range, String description) {
        super(owner, targetNumber, range, description);
    }

    @Override
    public boolean isAOE() {
        return false;
    }

    @Override
    public void update() {
        if (this.cooldown > 0) {
            --this.cooldown;
        }
    }

    @Override
    public void nextOwnerLevel() {
        this.cooldown = 0;
    }

    @Override
    public void nextSpellLevel() {
        this.cooldown = 0;
        this.heal++;
        this.updateStats(this.targetNumber, this.range, "A random spell level " + this.level);
    }

    @Override
    public boolean cast(Collection<LivingThing> targets) {
        LivingThing owner = this.getOwner();
        if (owner != null) {
            for (LivingThing target : targets) {
                target.damage(this.damages);
            }
            owner.damage(-this.heal);
            this.cooldown = MAXCOOLDOWN;
        }
        return false;
    }
}
