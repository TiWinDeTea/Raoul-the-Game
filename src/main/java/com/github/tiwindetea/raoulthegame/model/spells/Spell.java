package com.github.tiwindetea.raoulthegame.model.spells;

import com.github.tiwindetea.raoulthegame.events.spells.SpellCreationEvent;
import com.github.tiwindetea.raoulthegame.listeners.game.spell.SpellListener;
import com.github.tiwindetea.raoulthegame.model.Descriptable;
import com.github.tiwindetea.raoulthegame.model.MainPackage;
import com.github.tiwindetea.raoulthegame.model.Pair;
import com.github.tiwindetea.raoulthegame.model.livings.LivingThing;
import com.github.tiwindetea.raoulthegame.model.space.Map;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.github.tiwindetea.raoulthegame.view.entities.SpellType;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The Spell class.
 * This class should be extended when you want to describe a new spell for a LivingThing
 *
 * @author Lucas LAZARE
 */
public abstract class Spell implements Descriptable {

    protected static final ResourceBundle BUNDLE = ResourceBundle.getBundle(MainPackage.name + ".Spells");
    protected static SpellsController controller;
    private final WeakReference<LivingThing> owner;
    protected int targetNumber;
    protected int range;
    protected int secondaryRange; // for aoe spells
    protected final long id = Pair.getUniqueId();
    protected String description;
    protected static Map spellsMap;

    protected static final List<SpellListener> listeners = new LinkedList<>();

    public static void setMap(Map map) {
        spellsMap = map;
    }

    public static void setController(SpellsController controller) {
        Spell.controller = controller;
    }

    public static void addSpellListener(SpellListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
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
    public Spell(LivingThing owner, int targetNumber, int range, int secondaryRange, String description, SpellType spellType) {
        this.owner = new WeakReference<>(owner);
        this.targetNumber = targetNumber;
        this.range = range;
        this.secondaryRange = secondaryRange;
        this.description = description;
        for (SpellListener listener : listeners) {
            listener.createSpell(new SpellCreationEvent(this.id, spellType));
        }
    }

    public Spell(LivingThing owner, SpellType spellType) {
        this.owner = new WeakReference<>(owner);
        for (SpellListener listener : listeners) {
            listener.createSpell(new SpellCreationEvent(this.id, spellType));
        }
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
    protected LivingThing getOwner() {
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
    public abstract void forgotten();

    /**
     * Updates the description of a spell
     *
     * @param str new description
     */
    protected final void updateDescription(String str) {
        this.description = str;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return this.description;
    }
}
