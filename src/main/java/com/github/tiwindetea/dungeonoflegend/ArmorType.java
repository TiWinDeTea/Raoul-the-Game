package com.github.tiwindetea.dungeonoflegend;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by maxime on 4/23/16.
 */
public enum ArmorType {
	BREAST_PLATE {
		public int toInt() {
			return 0;
		}

		@Override
		public String toString() {
			return resourceBundle.getString("breast-plate");
		}
	},
	GLOVES {
		public int toInt() {
			return 1;
		}

		@Override
		public String toString() {
			return resourceBundle.getString("gloves");
		}
	},
	HELMET {
		public int toInt() {
			return 2;
		}

		@Override
		public String toString() {
			return resourceBundle.getString("helmet");
		}
	},
	BOOTS {
		public int toInt() {
			return 3;
		}

		@Override
		public String toString() {
			return resourceBundle.getString("boots");
		}
	},
	PANTS {
		public int toInt() {
			return 4;
		}

		@Override
		public String toString() {
			return resourceBundle.getString("plants");
		}
	};

	private final static String bundleName = "ArmorType";

	private final static ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName, Locale.getDefault());

	public int toInt() {
		return -1;
	}

	@Override
	public String toString() {
		return null;
	}

	static public ArmorType fromInt(int a) {
		//Java code style convention are ugly
		switch(a) {
		case 0:
			return BREAST_PLATE;
		case 1:
			return GLOVES;
		case 2:
			return HELMET;
		case 3:
			return BOOTS;
		case 4:
			return PANTS;
		default:
			return null;
		}
	}
}
