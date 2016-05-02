package com.github.tiwindetea.dungeonoflegend.view;

import com.github.tiwindetea.dungeonoflegend.model.MainPackage;
import com.github.tiwindetea.dungeonoflegend.model.Vector2i;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by maxime on 5/2/16.
 */
public enum EntityType {
	PLAYER {
		@Override
		public String toString() {
			return resourceBundle.getString("player");
		}

		public Vector2i getSpritePosition(int spriteVersion) {
			if(spriteVersion <= Integer.parseInt(resourceBundle.getString("player.versions")) && spriteVersion > 0) {
				return new Vector2i(Integer.parseInt(resourceBundle.getString("player.v" + spriteVersion + ".sprite.position.x")),
				  Integer.parseInt(resourceBundle.getString("player.v" + spriteVersion + ".sprite.position.y")));
			}
			return null;
		}
	},
	HELMET {
		@Override
		public String toString() {
			return resourceBundle.getString("helmet");
		}

		public Vector2i getSpritePosition(int spriteVersion) {
			if(spriteVersion <= Integer.parseInt(resourceBundle.getString("helmet.versions")) && spriteVersion > 0) {
				return new Vector2i(Integer.parseInt(resourceBundle.getString("helmet.v" + spriteVersion + ".sprite.position.x")),
				  Integer.parseInt(resourceBundle.getString("helmet.v" + spriteVersion + ".sprite.position.y")));
			}
			return null;
		}
	},
	BREAST_PLATE {
		@Override
		public String toString() {
			return resourceBundle.getString("breast-plate");
		}

		public Vector2i getSpritePosition(int spriteVersion) {
			if(spriteVersion <= Integer.parseInt(resourceBundle.getString("plate.versions")) && spriteVersion > 0) {
				return new Vector2i(Integer.parseInt(resourceBundle.getString("plate.v" + spriteVersion + ".sprite.position.x")),
				  Integer.parseInt(resourceBundle.getString("plate.v" + spriteVersion + ".sprite.position.y")));
			}
			return null;
		}
	},
	PANTS {
		@Override
		public String toString() {
			return resourceBundle.getString("pants");
		}

		public Vector2i getSpritePosition(int spriteVersion) {
			if(spriteVersion <= Integer.parseInt(resourceBundle.getString("pants.versions")) && spriteVersion > 0) {
				return new Vector2i(Integer.parseInt(resourceBundle.getString("pants.v" + spriteVersion + ".sprite.position.x")),
				  Integer.parseInt(resourceBundle.getString("pants.v" + spriteVersion + ".sprite.position.y")));
			}
			return null;
		}
	},
	BOOTS {
		@Override
		public String toString() {
			return resourceBundle.getString("boots");
		}

		public Vector2i getSpritePosition(int spriteVersion) {
			if(spriteVersion <= Integer.parseInt(resourceBundle.getString("boots.versions")) && spriteVersion > 0) {
				return new Vector2i(Integer.parseInt(resourceBundle.getString("boots.v" + spriteVersion + ".sprite.position.x")),
				  Integer.parseInt(resourceBundle.getString("boots.v" + spriteVersion + ".sprite.position.y")));
			}
			return null;
		}
	},
	GLOVES {
		@Override
		public String toString() {
			return resourceBundle.getString("gloves");
		}

		public Vector2i getSpritePosition(int spriteVersion) {
			if(spriteVersion <= Integer.parseInt(resourceBundle.getString("gloves.versions")) && spriteVersion > 0) {
				return new Vector2i(Integer.parseInt(resourceBundle.getString("gloves.v" + spriteVersion + ".sprite.position.x")),
				  Integer.parseInt(resourceBundle.getString("gloves.v" + spriteVersion + ".sprite.position.y")));
			}
			return null;
		}
	},
	BOW {
		@Override
		public String toString() {
			return resourceBundle.getString("bow");
		}

		public Vector2i getSpritePosition(int spriteVersion) {
			if(spriteVersion <= Integer.parseInt(resourceBundle.getString("bow.versions")) && spriteVersion > 0) {
				return new Vector2i(Integer.parseInt(resourceBundle.getString("bow.v" + spriteVersion + ".sprite.position.x")),
				  Integer.parseInt(resourceBundle.getString("bow.v" + spriteVersion + ".sprite.position.y")));
			}
			return null;
		}
	},
	SWORD {
		@Override
		public String toString() {
			return resourceBundle.getString("sword");
		}

		public Vector2i getSpritePosition(int spriteVersion) {
			if(spriteVersion <= Integer.parseInt(resourceBundle.getString("sword.versions")) && spriteVersion > 0) {
				return new Vector2i(Integer.parseInt(resourceBundle.getString("sword.v" + spriteVersion + ".sprite.position.x")),
				  Integer.parseInt(resourceBundle.getString("sword.v" + spriteVersion + ".sprite.position.y")));
			}
			return null;
		}
	},
	WAND {
		@Override
		public String toString() {
			return resourceBundle.getString("wand");
		}

		public Vector2i getSpritePosition(int spriteVersion) {
			if(spriteVersion <= Integer.parseInt(resourceBundle.getString("wand.versions")) && spriteVersion > 0) {
				return new Vector2i(Integer.parseInt(resourceBundle.getString("wand.v" + spriteVersion + ".sprite.position.x")),
				  Integer.parseInt(resourceBundle.getString("wand.v" + spriteVersion + ".sprite.position.y")));
			}
			return null;
		}
	},
	POT {
		@Override
		public String toString() {
			return resourceBundle.getString("pot");
		}

		public Vector2i getSpritePosition(int spriteVersion) {
			if(spriteVersion <= Integer.parseInt(resourceBundle.getString("pot.versions")) && spriteVersion > 0) {
				return new Vector2i(Integer.parseInt(resourceBundle.getString("pot.v" + spriteVersion + ".sprite.position.x")),
				  Integer.parseInt(resourceBundle.getString("pot.v" + spriteVersion + ".sprite.position.y")));
			}
			return null;
		}
	},
	SCROLL {
		@Override
		public String toString() {
			return resourceBundle.getString("scroll");
		}

		public Vector2i getSpritePosition(int spriteVersion) {
			if(spriteVersion <= Integer.parseInt(resourceBundle.getString("scroll.versions")) && spriteVersion > 0) {
				return new Vector2i(Integer.parseInt(resourceBundle.getString("scroll.v" + spriteVersion + ".sprite.position.x")),
				  Integer.parseInt(resourceBundle.getString("scroll.v" + spriteVersion + ".sprite.position.y")));
			}
			return null;
		}
	},
	TRAP {
		@Override
		public String toString() {
			return resourceBundle.getString("trap");
		}

		public Vector2i getSpritePosition(int spriteVersion) {
			if(spriteVersion <= Integer.parseInt(resourceBundle.getString("trap.versions")) && spriteVersion > 0) {
				return new Vector2i(Integer.parseInt(resourceBundle.getString("trap.v" + spriteVersion + ".sprite.position.x")),
				  Integer.parseInt(resourceBundle.getString("trap.v" + spriteVersion + ".sprite.position.y")));
			}
			return null;
		}
	},
	CHEST {
		@Override
		public String toString() {
			return resourceBundle.getString("chest");
		}

		public Vector2i getSpritePosition(int spriteVersion) {
			if(spriteVersion <= Integer.parseInt(resourceBundle.getString("chest.versions")) && spriteVersion > 0) {
				return new Vector2i(Integer.parseInt(resourceBundle.getString("chest.v" + spriteVersion + ".sprite.position.x")),
				  Integer.parseInt(resourceBundle.getString("chest.v" + spriteVersion + ".sprite.position.y")));
			}
			return null;
		}
	};

	private final static String bundleName = MainPackage.name + ".Entity";

	private final static ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName, Locale.getDefault());

	@Override
	public String toString() {
		return null;
	}

	public Vector2i getSpritePosition(int versionNumber) {
		return null;
	}
}
