//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.model;

/**
 * Created by maxime on 4/23/16.
 */
public enum StorableObjectType {
	WEAPON,
	ARMOR,
	CONSUMABLE;

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
