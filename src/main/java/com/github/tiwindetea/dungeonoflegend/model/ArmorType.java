package com.github.tiwindetea.dungeonoflegend.model;

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
			return resourceBundle.getString("breast-plate.name");
		}

		public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("breast-plate.sprite.position.x")),
			  Integer.parseInt(resourceBundle.getString("breast-plate.sprite.position.y")));
		}
	},
	GLOVES {
		public int toInt() {
			return 1;
		}

		@Override
		public String toString() {
			return resourceBundle.getString("gloves.name");
		}

		public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("gloves.sprite.position.x")),
			  Integer.parseInt(resourceBundle.getString("gloves.sprite.position.y")));
		}
	},
	HELMET {
		public int toInt() {
			return 2;
		}

		@Override
		public String toString() {
			return resourceBundle.getString("helmet.name");
		}

		public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("helmet.sprite.position.x")),
			  Integer.parseInt(resourceBundle.getString("helmet.sprite.position.y")));
		}
	},
	BOOTS {
		public int toInt() {
			return 3;
		}

		@Override
		public String toString() {
			return resourceBundle.getString("boots.name");
		}

		public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("boots.sprite.position.x")),
			  Integer.parseInt(resourceBundle.getString("boots.sprite.position.y")));
		}
	},
	PANTS {
		public int toInt() {
			return 4;
		}

		@Override
		public String toString() {
			return resourceBundle.getString("pants.name");
		}

		public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("pants.sprite.position.x")),
			  Integer.parseInt(resourceBundle.getString("pants.sprite.position.y")));
		}
	};

	private final static String bundleName = MainPackage.name + ".ArmorType";

	private final static ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName, Locale.getDefault());

	public int toInt() {
		return -1;
	}

	@Override
	public String toString() {
		return null;
	}

	public Vector2i getSpritePosition() {
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
