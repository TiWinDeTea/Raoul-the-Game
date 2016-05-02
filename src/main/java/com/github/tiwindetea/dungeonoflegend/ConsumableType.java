package com.github.tiwindetea.dungeonoflegend;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by maxime on 4/23/16.
 */
public enum ConsumableType {
	POT {
		@Override
		public String toString() {
			return resourceBundle.getString("pot");
		}

		public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("pot.sprite.position.x")),
			  Integer.parseInt(resourceBundle.getString("pot.sprite.position.y")));
		}
	},
	SCROLL {
		@Override
		public String toString() {
			return resourceBundle.getString("scroll");
		}

		public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("scroll.sprite.position.x")),
			  Integer.parseInt(resourceBundle.getString("scroll.sprite.position.y")));
		}
	};

	private final static String bundleName = MainPackage.name + ".ConsumableType";

	private final static ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName, Locale.getDefault());

	public String toString() {
		return null;
	}

	public Vector2i getSpritePosition() {
		return null;
	}
}
