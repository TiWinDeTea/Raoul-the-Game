//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.model.items;

import com.github.tiwindetea.raoulthegame.model.livings.LivingThing;
import com.github.tiwindetea.raoulthegame.view.entities.StaticEntityType;

/**
 * Scroll
 *
 * @author Lucas LAZARE
 */
public class Scroll implements Consumable {

    private int turns;
    private double healthModifierPerTick;
    private double healthModifierModifierPerTick;
    private LivingThing target;
    private LivingThing source;
    private StaticEntityType gtype;
    private String name;

    /**
     * Instantiates a new Scroll.
     *
     * @param turns                         the turns
     * @param healthModifierPerTick         the health modifier per tick
     * @param healthModifierModifierPerTick the health modifier modifier per tick
     */
    public Scroll(int turns, double healthModifierPerTick, double healthModifierModifierPerTick) {
        this.turns = turns;
        this.healthModifierPerTick = healthModifierPerTick;
        this.healthModifierModifierPerTick = healthModifierModifierPerTick;
        if (turns > 9) {
            this.gtype = StaticEntityType.SCROLL1;
        } else {
            this.gtype = StaticEntityType.SCROLL2;
        }
    }

    private Scroll() {

    }

    /**
     * Sets the source damages landed by the scroll
     *
     * @param source
     */
    public void setSource(LivingThing source) {
        this.source = source;
    }

    /**
     * Parses a Scroll.
     *
     * @param str a Scroll's String
     * @return the Scroll
     */
    public static Scroll parseScroll(String str) {
        if (!str.substring(0, 7).equals("scroll=")) {
            throw new IllegalArgumentException("Invoking Scroll.parseScroll with input string: \"" + str + "\"");
        }
        if (str.equals("scroll={null}")) {
            return null;
        }

		/* Computing values' indexes */
        int SEType = str.indexOf("SEType=") + 7;
        int turns = str.indexOf("turns=", SEType) + 6;
        int hmpt = str.indexOf("healthMod=", turns) + 10;
        int hmmpt = str.indexOf("healthModMod=", hmpt) + 13;

		/* Parsing values */
        Scroll scroll = new Scroll();
        scroll.gtype = StaticEntityType.parseStaticEntityType(str.substring(SEType, str.indexOf(",", SEType)));
        scroll.turns = Integer.parseInt(str.substring(turns, str.indexOf(",", turns)));
        scroll.healthModifierPerTick = Double.parseDouble(str.substring(hmpt, str.indexOf(",", hmpt)));
        scroll.healthModifierModifierPerTick = Double.parseDouble(str.substring(hmmpt, str.indexOf(",", hmmpt)));
        return scroll;
    }

    /**
     * {@inheritDoc}
     */
    public void trigger(LivingThing livingThing) {
        this.target = livingThing;
        livingThing.damage(this.healthModifierPerTick, this.source);
        --this.turns;
    }

    /**
     * {@inheritDoc}
     */
    public boolean nextTick() {
        if (!this.target.isAlive())
            return true;
        if (this.turns > 0) {
            --this.turns;
            this.healthModifierModifierPerTick += this.healthModifierModifierPerTick;
            this.target.damage(this.healthModifierPerTick, this.source);
            return this.turns < 0;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public ConsumableType getConsumableType() {
        return ConsumableType.SCROLL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StorableObjectType getType() {
        return StorableObjectType.CONSUMABLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StaticEntityType getGType() {
        return this.gtype;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "scroll={SEType=" + this.gtype
                + ",turns=" + this.turns
                + ",healthMod=" + this.healthModifierPerTick
                + ",healthModMod=" + this.healthModifierModifierPerTick
                + ",}";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        if (this.name == null) {
            int i = 0;
            this.name = this.gtype.toString().replaceAll("[0-9]+", "");
            this.name = this.name.substring(0, 1).toUpperCase() + this.name.substring(1);
            while ((i = this.name.indexOf("-")) != -1) {
                this.name = this.name.substring(0, i) + " " + this.name.substring(i + 1, i + 2).toUpperCase() + this.name.substring(i + 2);
            }

            this.name += "\n\nDamage per turn: " + (int) this.healthModifierPerTick;
            if (this.healthModifierModifierPerTick != 0) {
                this.name += "\nDamage modifier: " + (int) this.healthModifierModifierPerTick;
            }
            if (this.turns != 0) {
                this.name += "\nEffect duration: " + this.turns;
            }
        }
        return this.name;
    }
}
