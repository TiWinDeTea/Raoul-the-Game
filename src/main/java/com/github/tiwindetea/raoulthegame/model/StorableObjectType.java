//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.model;

/**
 * StorableObjectType
 *
 * @author Lucas LAZARE
 */
public enum StorableObjectType {
	/**
	 * Weapon storable object type.
	 */
	WEAPON,
	/**
	 * Armor storable object type.
	 */
	ARMOR,
	/**
	 * Consumable storable object type.
	 */
	CONSUMABLE;

	/**
	 * Parses a storable object type.
	 *
	 * @param str a storable object type's string
	 * @return the storable object type, or null.
	 */
	public static StorableObjectType parseStorableObjectType(String str) {
		str.toUpperCase();
		switch (str) {
			case "WEAPON":
				return WEAPON;
			case "ARMOR":
				return ARMOR;
			case "CONSUMABLE":
				return CONSUMABLE;
			default:
				return null;
		}
	}
}
