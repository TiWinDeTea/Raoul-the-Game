//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.view.entities;

import com.github.tiwindetea.raoulthegame.model.space.Direction;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.github.tiwindetea.raoulthegame.view.ViewPackage;
import javafx.scene.image.Image;

/**
 * The enum LivingEntityType
 *
 * @author Maxime PINARD..
 */
public enum LivingEntityType {
	PLAYER1(ViewPackage.LET_resourceBundle.getString("player1.string")),
	PLAYER2(ViewPackage.LET_resourceBundle.getString("player2.string")),
	LITTLE_PUNK_DUCK(ViewPackage.LET_resourceBundle.getString("little-punk-duck.string")),
	PUNK_DUCK(ViewPackage.LET_resourceBundle.getString("punk-duck.string")),
	LITTLE_ARISTOCRAT_DUCK(ViewPackage.LET_resourceBundle.getString("little-aristocrat-duck.string")),
	ARISTOCRAT_DUCK(ViewPackage.LET_resourceBundle.getString("aristocrat-duck.string")),
	LITTLE_MONSTER_TRUCK_DUCK(ViewPackage.LET_resourceBundle.getString("little-monster-truck-duck.string")),
	MONSTER_TRUCK_DUCK(ViewPackage.LET_resourceBundle.getString("monster-truck-duck.string")),
	LITTLE_SPACE_SHIP_DUCK(ViewPackage.LET_resourceBundle.getString("little-space-ship-duck.string")),
	SPACE_SHIP_DUCK(ViewPackage.LET_resourceBundle.getString("space-ship-duck.string")),
	LITTLE_SATANIC_DUCK(ViewPackage.LET_resourceBundle.getString("little-satanic-duck.string")),
	SATANIC_DUCK(ViewPackage.LET_resourceBundle.getString("satanic-duck.string")),
	PEACEFUL_ECTOPLASMA(ViewPackage.LET_resourceBundle.getString("peaceful-ectoplasma.string")),
	DOG(ViewPackage.LET_resourceBundle.getString("dog-pet.string")),
	OCTOPUS(ViewPackage.LET_resourceBundle.getString("octopus-pet.string")),
	GHOST(ViewPackage.LET_resourceBundle.getString("ghost.string"));

	private final String string;

	LivingEntityType(String string) {
		this.string = string;
	}

	@Override
	public String toString() {
		return this.string;
	}

	/**
	 * Gets sprite with down direction position.
	 *
	 * @return the sprite with down direction position
	 */
	public Vector2i getSpriteDownPosition() {
		return new Vector2i(Integer.parseInt(ViewPackage.LET_resourceBundle.getString(this.toString() + ".sprite.down.position.x")),
		  Integer.parseInt(ViewPackage.LET_resourceBundle.getString(this.toString() + ".sprite.down.position.y")));
	}

	/**
	 * Gets sprite with up direction position.
	 *
	 * @return the sprite with up direction position
	 */
	public Vector2i getSpriteUpPosition() {
		return new Vector2i(Integer.parseInt(ViewPackage.LET_resourceBundle.getString(this.toString() + ".sprite.up.position.x")),
		  Integer.parseInt(ViewPackage.LET_resourceBundle.getString(this.toString() + ".sprite.up.position.y")));
	}

	/**
	 * Gets sprite with right direction position.
	 *
	 * @return the sprite with right direction position
	 */
	public Vector2i getSpriteRightPosition() {
		return new Vector2i(Integer.parseInt(ViewPackage.LET_resourceBundle.getString(this.toString() + ".sprite.right.position.x")),
		  Integer.parseInt(ViewPackage.LET_resourceBundle.getString(this.toString() + ".sprite.right.position.y")));
	}

	/**
	 * Gets sprite with left direction position.
	 *
	 * @return the sprite with left direction position
	 */
	public Vector2i getSpriteLeftPosition() {
		return new Vector2i(Integer.parseInt(ViewPackage.LET_resourceBundle.getString(this.toString() + ".sprite.left.position.x")),
		  Integer.parseInt(ViewPackage.LET_resourceBundle.getString(this.toString() + ".sprite.left.position.y")));
	}

	/**
	 * Gets sprite position.
	 *
	 * @param direction the direction of the wanted sprite
	 * @return the sprite position
	 */
	public Vector2i getSpritePosition(Direction direction) {
		switch(direction) {
		case UP:
			return this.getSpriteUpPosition();
		case LEFT:
			return this.getSpriteLeftPosition();
		case RIGHT:
			return this.getSpriteRightPosition();
		case DOWN:
			/* Falls through */
		default:
			return this.getSpriteDownPosition();
		}
	}

	/**
	 * Gets LivingEntity image.
	 *
	 * @return the LivingEntity image
	 */
	public Image getImage() {
		switch(ViewPackage.LET_resourceBundle.getString(this.toString() + ".sprites.file")) {
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

	/**
	 * Parse a living entity type.
	 *
	 * @param str the living entity string
	 * @return the LivingEntityType
	 */
	public static LivingEntityType parseLivingEntity(String str) {
		for (LivingEntityType type : LivingEntityType.values()) {
			if (type.toString().equals(str)) {
				return type;
			}
		}
		return null;
	}
}
