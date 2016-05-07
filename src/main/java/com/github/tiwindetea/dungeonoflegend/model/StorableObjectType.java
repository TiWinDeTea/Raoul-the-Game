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
