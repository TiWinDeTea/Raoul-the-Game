package com.github.tiwindetea.dungeonoflegend.view;

import com.github.tiwindetea.dungeonoflegend.model.MainPackage;
import com.github.tiwindetea.dungeonoflegend.model.Vector2i;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by maxime on 5/2/16.
 */
public enum EntityType {
	PLAYER1 {
		@Override
		public String toString() {
			return resourceBundle.getString("player1.string");
		}
	},
	PLAYER2 {
		@Override
		public String toString() {
			return resourceBundle.getString("player2.string");
		}
	},
	HELMET1 {
		@Override
		public String toString() {
			return resourceBundle.getString("helmet1.string");
		}
	},
	HELMET2 {
		@Override
		public String toString() {
			return resourceBundle.getString("helmet2.string");
		}
	},
	BREAST_PLATE1 {
		@Override
		public String toString() {
			return resourceBundle.getString("breast-plate1.string");
		}
	},
	BREAST_PLATE2 {
		@Override
		public String toString() {
			return resourceBundle.getString("breast-plate2.string");
		}
	},
	PANTS1 {
		@Override
		public String toString() {
			return resourceBundle.getString("pants1.string");
		}
	},
	PANTS2 {
		@Override
		public String toString() {
			return resourceBundle.getString("pants2.string");
		}
	},
	BOOTS1 {
		@Override
		public String toString() {
			return resourceBundle.getString("boots1.string");
		}
	},
	BOOTS2 {
		@Override
		public String toString() {
			return resourceBundle.getString("boots2.string");
		}
	},
	GLOVES1 {
		@Override
		public String toString() {
			return resourceBundle.getString("gloves1.string");
		}
	},
	GLOVES2 {
		@Override
		public String toString() {
			return resourceBundle.getString("gloves2.string");
		}
	},
	BOW1 {
		@Override
		public String toString() {
			return resourceBundle.getString("bow1.string");
		}
	},
	BOW2 {
		@Override
		public String toString() {
			return resourceBundle.getString("bow2.string");
		}
	},
	SWORD1 {
		@Override
		public String toString() {
			return resourceBundle.getString("sword1.string");
		}
	},
	SWORD2 {
		@Override
		public String toString() {
			return resourceBundle.getString("sword2.string");
		}
	},
	WAND1 {
		@Override
		public String toString() {
			return resourceBundle.getString("wand1.string");
		}
	},
	WAND2 {
		@Override
		public String toString() {
			return resourceBundle.getString("wand2.string");
		}
	},
	HEALING_POT {
		@Override
		public String toString() {
			return resourceBundle.getString("healing-pot.string");
		}
	},
	MANA_POT {
		@Override
		public String toString() {
			return resourceBundle.getString("mana-pot.string");
		}
	},
	SUPER_POT {
		@Override
		public String toString() {
			return resourceBundle.getString("super-pot.string");
		}
	},
	SCROLL1 {
		@Override
		public String toString() {
			return resourceBundle.getString("scroll1.string");
		}
	},
	SCROLL2 {
		@Override
		public String toString() {
			return resourceBundle.getString("scroll2.string");
		}
	},
	TRAP {
		@Override
		public String toString() {
			return resourceBundle.getString("trap.string");
		}
	},
	ACTIVATED_TRAP {
		@Override
		public String toString() {
			return resourceBundle.getString("activated-trap.string");
		}
	},
	CHEST {
		@Override
		public String toString() {
			return resourceBundle.getString("chest.string");
		}
	};

	private final static String bundleName = MainPackage.name + ".Entity";

	private final static ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName, Locale.getDefault());

	@Override
	public String toString() {
		return null;
	}

	public Vector2i getSpritesPosition() {
		return new Vector2i(Integer.parseInt(resourceBundle.getString(this.toString() + ".sprites.position.x")),
		  Integer.parseInt(resourceBundle.getString(this.toString() + ".sprites.position.y")));
	}

	public int getSpritesNumber() {
		return Integer.parseInt(resourceBundle.getString(this.toString() + ".sprites.number"));
	}

	public int getAnimationSpeed() {
		if(this.getSpritesNumber() > 1)
			return Integer.parseInt(resourceBundle.getString(this.toString() + ".animation.speed.millseconds"));
		return -1;
	}

	public static void main(String[] args) {
		EntityType ent = EntityType.SUPER_POT;
		System.out.println(ent.getSpritesPosition());
		System.out.println(ent.getSpritesNumber());
		System.out.println(ent.getAnimationSpeed());
	}
}
