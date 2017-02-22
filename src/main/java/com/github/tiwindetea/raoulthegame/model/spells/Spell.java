//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.model.spells;

import com.github.tiwindetea.raoulthegame.events.game.spells.SpellCooldownUpdateEvent;
import com.github.tiwindetea.raoulthegame.events.game.spells.SpellCreationEvent;
import com.github.tiwindetea.raoulthegame.events.game.spells.SpellDeletionEvent;
import com.github.tiwindetea.raoulthegame.events.game.spells.SpellDescriptionUpdateEvent;
import com.github.tiwindetea.raoulthegame.listeners.game.spells.SpellListener;
import com.github.tiwindetea.raoulthegame.model.Descriptable;
import com.github.tiwindetea.raoulthegame.model.MainPackage;
import com.github.tiwindetea.raoulthegame.model.livings.LivingThing;
import com.github.tiwindetea.raoulthegame.model.livings.Player;
import com.github.tiwindetea.raoulthegame.model.space.Map;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.github.tiwindetea.raoulthegame.model.spells.passives.Berserker;
import com.github.tiwindetea.raoulthegame.model.spells.passives.BonusHP;
import com.github.tiwindetea.raoulthegame.model.spells.passives.Drainer;
import com.github.tiwindetea.raoulthegame.model.spells.passives.IronWill;
import com.github.tiwindetea.raoulthegame.model.spells.passives.Regen;
import com.github.tiwindetea.raoulthegame.model.spells.passives.Savior;
import com.github.tiwindetea.raoulthegame.model.spells.passives.Thorn;
import com.github.tiwindetea.raoulthegame.model.spells.useablespells.Explorer;
import com.github.tiwindetea.raoulthegame.model.spells.useablespells.FireBall;
import com.github.tiwindetea.raoulthegame.model.spells.useablespells.Grenade;
import com.github.tiwindetea.raoulthegame.model.spells.useablespells.Heal;
import com.github.tiwindetea.raoulthegame.model.spells.useablespells.Octopus;
import com.github.tiwindetea.raoulthegame.model.spells.useablespells.PotCreator;
import com.github.tiwindetea.raoulthegame.model.spells.useablespells.ScrollSpell;
import com.github.tiwindetea.raoulthegame.model.spells.useablespells.SummonDog;
import com.github.tiwindetea.raoulthegame.model.spells.useablespells.Teleport;
import com.github.tiwindetea.raoulthegame.view.entities.SpellType;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.ResourceBundle;

/**
 * The Spell class.
 * This class should be extended when you want to describe a new spell for a LivingThing
 *
 * @author Lucas LAZARE
 */
public abstract class Spell<T extends LivingThing> implements Descriptable {

    protected static final DecimalFormat DECIMAL = new DecimalFormat("#.0");
    protected static final ResourceBundle BUNDLE = ResourceBundle.getBundle(MainPackage.name + ".Spells");
    protected static SpellsController controller;
    private final WeakReference<T> owner;
    protected int targetNumber;
    protected int range;
    protected int secondaryRange; // for aoe spells
    protected final int id;
    protected String description;
    protected static Map spellsMap;

    public static void setMap(Map map) {
        spellsMap = map;
    }

    public static void setController(SpellsController controller) {
        Spell.controller = controller;
    }

    protected static void fire(SpellCreationEvent e) {
        for (SpellListener listener : controller.getSpellListeners()) {
            listener.handle(e);
        }
    }

    protected static void fire(SpellDeletionEvent e) {
        for (SpellListener listener : controller.getSpellListeners()) {
            listener.handle(e);
        }
    }

    protected static void fire(SpellDescriptionUpdateEvent e) {
        for (SpellListener listener : controller.getSpellListeners()) {
            listener.handle(e);
        }
    }

    protected static void fire(SpellCooldownUpdateEvent e) {
        for (SpellListener listener : controller.getSpellListeners()) {
            listener.handle(e);
        }
    }


    /**
     * Instantiates a new Spell.
     *
     * @param owner          the spell's owner
     * @param targetNumber   the maximum number of target this spell can be used on. If set to 0, all LivingThing within range are passed as targets when the spell is casted
     * @param range          the cast range of this spell
     * @param secondaryRange the aoe range of this spell
     * @param description    the description of this spell
     */
    public Spell(T owner, int targetNumber, int range, int secondaryRange, String description, int id) {
        this.owner = new WeakReference<>(owner);
        this.targetNumber = targetNumber;
        this.range = range;
        this.secondaryRange = secondaryRange;
        this.description = description;
        this.id = id;
        owner.getSpells().add(this);
    }

    public Spell(T owner, int id) {
        this.owner = new WeakReference<>(owner);
        this.id = id;
        owner.getSpells().add(this);
    }

    /**
     * @return true if this spell is an aoe spell, false otherwise
     */
    public abstract boolean isAOE();

    /**
     * @return true if this spell is a passive spell
     */
    public abstract boolean isPassive();

    /**
     * @return the owner of this spell
     */
    protected T getOwner() {
        return this.owner.get();
    }

    /**
     * @return the range of this spell
     */
    public int getRange() {
        return this.range;
    }

    /**
     * @return the range of the aoe effect
     *
     * @implNote Should be set to 0 if an aoe spell is not currently used
     */
    public int getSecondaryRange() {
        return this.secondaryRange;
    }

    /**
     * getter for the number of targets this spell can hold.
     *
     * @return the number of targets this spell can hold
     */
    public int getTargetNumber() {
        return this.targetNumber;
    }

    /**
     * Gets the coordinate where the spell was casted.
     *
     * @return the spell's coordinates or null if irrelevant
     */
    public abstract Vector2i getSpellSource();

    /**
     * Handler. This function should be called each time the spell's owner is attacked
     *
     * @param source  the source of the damages
     * @param damages the amount of received damages
     * @return the damage modifier of this spell
     */
    public abstract double ownerDamaged(@Nullable LivingThing source, double damages);

    /**
     * Handler. This function should be called time the spell's owner is attacking
     *
     * @param target the damages' target
     * @return the damage modifier of this spell
     */
    public abstract double ownerAttacking(@NotNull LivingThing target);

    /**
     * Handler. Updates the spell. This function should be called once and only once each turn.
     *
     * @param targets the new potential targets of this spell (nullable if the spell isn't an AOE spell)
     */
    public abstract void update(Collection<LivingThing> targets);

    /**
     * Handler. This function should be called each time the spell's owner gained a level.
     */
    public abstract void nextOwnerLevel();

    /**
     * Handler. This function should be called each time the spell gained a level.
     */
    public abstract void nextSpellLevel();

    /**
     * Method called whenever the spell is casted on a LivingThing // a group of LivingThing(s)
     *
     * @param targets        the potential targets of this spell (from 0 to targetNumber if the spell isn't an aoe spell)
     * @param sourcePosition position where the spell was cast. nullable if the spell isn't an aoe spell
     * @return true if the spell was successfully casted, false otherwise
     */
    public abstract boolean cast(Collection<LivingThing> targets, Vector2i sourcePosition);

    /**
     * Method called whenever the floor changes.
     */
    public abstract void nextFloor();

    /**
     * Method called whenever the owner forgots the spell
     */
    protected abstract void forgotten();

    /**
     * Method called whenever the owner forgots the spell
     */
    public final void forgot() {
        T owner = getOwner();
        if (owner != null) {
            owner.getSpells().remove(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    public static Spell<? extends LivingThing> toSpell(SpellType type, Player owner) {
        switch (type) {
            case SUMMON_DOG:
                return new SummonDog(owner);
            case HEAL:
                return new Heal(owner);
            case DRAINER:
                return new Drainer(owner);
            case SAVIOR:
                return new Savior(owner);
            case FIRE_BALL:
                return new FireBall(owner);
            case TELEPORT:
                return new Teleport(owner);
            case REGEN:
                return new Regen(owner);
            case BERSERKER:
                return new Berserker(owner);
            case OCTOPUS:
                return new Octopus(owner);
            case POT_CREATOR:
                return new PotCreator(owner);
            case BONUS_HP:
                return new BonusHP(owner);
            case IRON_WILL:
                return new IronWill(owner);
            case GRENADE:
                return new Grenade(owner);
            case SCROLL_SPELL:
                return new ScrollSpell(owner);
            case THORN:
                return new Thorn(owner);
            case EXPLORER:
                return new Explorer(owner);
        }
        return null;
    }
}
