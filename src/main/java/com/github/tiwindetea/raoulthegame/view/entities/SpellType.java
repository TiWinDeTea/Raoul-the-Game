//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.view.entities;

import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.github.tiwindetea.raoulthegame.view.ViewPackage;
import javafx.scene.image.Image;

/**
 * The enum SpellType
 *
 * @author Lucas LAZARE
 */
public enum SpellType {
	SUMMON_DOG("summon_dog"),
	HEAL("heal"),
	DRAINER("drainer"),
	SAVIOR("savior"),
	FIRE_BALL("fire_ball"),
	TELEPORT("teleport"),
	REGEN("regen"),
	BERSERKER("berserker"),
	OCTOPUS("octopus"), // todo
	POT_CREATOR("pot_creator"), // todo
	BONUS_HP("bonus_hp"),
	IRON_WILL("iron_will"),
	GRENADE("grenade"), // todo
	SCROLL_SPELL("scroll_spell"),
	THORN("thorn"),
	EXPLORER("explorer");

	public String name;

	/**
	 * Gets sprite position.
	 *
	 * @return the sprite position
	 */
	public Vector2i getSpritePosition() {
		return new Vector2i(Integer.parseInt(ViewPackage.ST_resourceBundle.getString(this.toString() + ".sprite.position.x")),
		  Integer.parseInt(ViewPackage.ST_resourceBundle.getString(this.toString() + ".sprite.position.y")));
	}

	/**
	 * Gets Spells image.
	 *
	 * @return the image
	 */
	public Image getImage() {
		switch(ViewPackage.ST_resourceBundle.getString(this.name + ".sprite.file")) {
		case "SpellsIcons.png":
			return ViewPackage.SPELLS_IMAGE;
		case "Objects.png":
			return ViewPackage.OBJECTS_IMAGE;
		case "Players.png":
			return ViewPackage.PLAYERS_IMAGE;
		case "Mobs.png":
			return ViewPackage.MOBS_IMAGE;
		default:
			return null;
		}
	}

    SpellType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
    	return this.name;
	}

	public static SpellType parse(String str) {
		for (SpellType spellType : SpellType.values()) {
			if (spellType.toString().equals(str)) {
				return spellType;
			}
		}
		return null;
	}

    public String getString(String key) {
    	return ViewPackage.ST_resourceBundle.getString(this.name + "." + key);
	}
}
