package com.github.tiwindetea.dungeonoflegend.model;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by maxime on 4/23/16.
 */
public enum WeaponType {
	BOW {
		@Override
		public String toString() {
			return resourceBundle.getString("bow");
		}
	},
	SWORD {
		@Override
		public String toString() {
			return resourceBundle.getString("sword");
		}
	},
	WAND {
		@Override
		public String toString() {
			return resourceBundle.getString("wand");
		}
	},
	FIST {
		@Override
		public String toString() {
			return resourceBundle.getString("fist");
		}
	};

	private final static String bundleName = MainPackage.name + ".WeaponType";

	private final static ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName, Locale.getDefault());


	public String toString() {
		return null;
	}

	public static WeaponType parseWeaponType(String str) {
		if (str.equals(BOW.toString())) {
			return BOW;
		} else if (str.equals(SWORD.toString())) {
			return SWORD;
		} else if (str.equals(WAND.toString())) {
			return WAND;
		} else if (str.equals(FIST.toString())) {
			return FIST;
		} else {
			return null;
		}
	}
}
