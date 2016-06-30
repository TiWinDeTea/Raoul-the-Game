//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.model.items;

import com.github.tiwindetea.raoulthegame.view.entities.StaticEntityType;

import java.util.Random;

/**
 * Armor
 *
 * @author Lucas LAZARE
 */
public class Armor implements StorableObject {

    private double defensePowerModifier;
    private double attackPowerModifier;
    private ArmorType type;
    private StaticEntityType gtype;
    private String name;

    /**
     * Instantiates a new Armor.
     *
     * @param defensePowerModifier the defense power modifier
     * @param attackPowerModifier  the attack power modifier
     * @param type                 the type
     */
    public Armor(double defensePowerModifier, double attackPowerModifier, ArmorType type) {
        this.defensePowerModifier = defensePowerModifier;
        this.attackPowerModifier = attackPowerModifier;
        this.type = type;
        Random random = new Random();

		/* Setting Graphical Type */
        if (random.nextBoolean()) {
            switch (type) {
                case BREAST_PLATE:
                    this.gtype = StaticEntityType.BREAST_PLATE1;
                    break;
                case GLOVES:
                    this.gtype = StaticEntityType.GLOVES1;
                    break;
                case HELMET:
                    this.gtype = StaticEntityType.HELMET1;
                    break;
                case BOOTS:
                    this.gtype = StaticEntityType.BOOTS1;
                    break;
                case PANTS:
                    this.gtype = StaticEntityType.PANTS1;
                    break;
            }
        } else {
            switch (type) {
                case BREAST_PLATE:
                    this.gtype = StaticEntityType.BREAST_PLATE2;
                    break;
                case GLOVES:
                    this.gtype = StaticEntityType.GLOVES2;
                    break;
                case HELMET:
                    this.gtype = StaticEntityType.HELMET2;
                    break;
                case BOOTS:
                    this.gtype = StaticEntityType.BOOTS2;
                    break;
                case PANTS:
                    this.gtype = StaticEntityType.PANTS2;
                    break;
            }
        }
    }

    private Armor() {

    }

    /**
     * Parses an Armor.
     *
     * @param str an Armor's String
     * @return the Armor
     */
    public static Armor parseArmor(String str) {
        if (!str.substring(0, 6).equals("armor=")) {
            throw new IllegalArgumentException("Invoking Armor.parseArmor with input string: \"" + str + "\"");
        }
        if (str.equals("armor={null}")) {
            return null;
        }

		/* Computing values' indexes */
        int SEType = str.indexOf("SEType=") + 7;
        int type = str.indexOf("type=", SEType) + 5;
        int def = str.indexOf("defense=", type) + 8;
        int attack = str.indexOf("attack=", def) + 7;

		/* Parsing values */
        Armor armor = new Armor();
        armor.defensePowerModifier = Double.parseDouble(str.substring(def, str.indexOf(',', def)));
        armor.attackPowerModifier = Double.parseDouble(str.substring(attack, str.indexOf(',', attack)));
        armor.type = ArmorType.parseArmorType(str.substring(type, str.indexOf(',', type)));
        armor.gtype = StaticEntityType.parseStaticEntityType(str.substring(SEType, str.indexOf(',', SEType)));
        return armor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StaticEntityType getGType() {
        return this.gtype;
    }

    /**
     * Gets defense power modifier.
     *
     * @return the defense power modifier
     */
    public double getDefensePowerModifier() {
        return this.defensePowerModifier;
    }

    /**
     * Gets attack power modifier.
     *
     * @return the attack power modifier
     */
    public double getAttackPowerModifier() {
        return this.attackPowerModifier;
    }

    /**
     * Gets armor type.
     *
     * @return the armor type
     *
     * @see ArmorType
     */
    public ArmorType getArmorType() {
        return this.type;
    }

    /**
     * {@inheritDoc}
     */
    public StorableObjectType getType() {
        return StorableObjectType.ARMOR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "armor={SEType=" + this.gtype
                + ",type=" + this.type
                + ",defense=" + this.defensePowerModifier
                + ",attack=" + this.attackPowerModifier
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
                this.name = this.name.substring(0, i) + " " + this.name.substring(i + 1, i + 2).toUpperCase() + this.name.substring(i + 2) + "\n";
            }
            this.name += "\nDefense power: " + (int) this.defensePowerModifier;
            this.name += "\nAttack power: " + (int) this.attackPowerModifier;
        }
        return this.name;
    }

    /**
     * Computes the powergrade of this object
     *
     * @return the powergrade
     */
    public int powerGrade() {
        return (int) (5 * (this.attackPowerModifier / 11 + this.defensePowerModifier));
    }
}
