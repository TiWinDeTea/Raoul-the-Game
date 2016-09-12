//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.view.entities;

import com.github.tiwindetea.raoulthegame.model.MainPackage;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.github.tiwindetea.raoulthegame.view.ViewPackage;
import javafx.scene.image.Image;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The enum StaticEntityType.
 *
 * @author Maxime PINARD.
 */
public enum StaticEntityType {
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
	},
	LIT_BULB {
		@Override
		public String toString() {
			return resourceBundle.getString("lit-bulb.string");
		}
	},
	UNLIT_BULB {
		@Override
		public String toString() {
			return resourceBundle.getString("unlit-bulb.string");
		}
	};

	private final static String bundleName = MainPackage.name + ".StaticEntity";

	private final static ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName, Locale.getDefault());

	@Override
	public String toString() {
		return null;
	}

	/**
	 * Gets sprites position.
	 *
	 * @return the sprites position
	 */
	public Vector2i getSpritesPosition() {
		return new Vector2i(Integer.parseInt(resourceBundle.getString(this.toString() + ".sprites.position.x")),
		  Integer.parseInt(resourceBundle.getString(this.toString() + ".sprites.position.y")));
	}

	/**
	 * Gets sprites number.
	 *
	 * @return the sprites number
	 */
	public int getSpritesNumber() {
		return Integer.parseInt(resourceBundle.getString(this.toString() + ".sprites.number"));
	}

	/**
	 * Gets animation speed.
	 *
	 * @return the animation speed
	 */
	public int getAnimationSpeed() {
		if(this.getSpritesNumber() > 1)
			return Integer.parseInt(resourceBundle.getString(this.toString() + ".animation.speed.millseconds"));
		return -1;
	}

	/**
	 * Gets StaticEntity image.
	 *
	 * @return the StaticEntity image
	 */
	public Image getImage() {
		switch(resourceBundle.getString(this.toString() + ".sprites.file")) {
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
	 * Parse a static entity type.
	 *
	 * @param str the static entity string
	 * @return the StaticEntityType
	 */
	public static StaticEntityType parseStaticEntityType(String str) {
		if (str.equals(HELMET1.toString())) {
			return HELMET1;
		} else if (str.equals(HELMET2.toString())) {
			return HELMET2;
		} else if (str.equals(BREAST_PLATE1.toString())) {
			return BREAST_PLATE1;
		} else if (str.equals(BREAST_PLATE2.toString())) {
			return BREAST_PLATE2;
		} else if (str.equals(PANTS1.toString())) {
			return PANTS1;
		} else if (str.equals(PANTS2.toString())) {
			return PANTS2;
		} else if (str.equals(BOOTS1.toString())) {
			return BOOTS1;
		} else if (str.equals(BOOTS2.toString())) {
			return BOOTS2;
		} else if (str.equals(GLOVES1.toString())) {
			return GLOVES1;
		} else if (str.equals(GLOVES2.toString())) {
			return GLOVES2;
		} else if (str.equals(BOW1.toString())) {
			return BOW1;
		} else if (str.equals(BOW2.toString())) {
			return BOW2;
		} else if (str.equals(SWORD1.toString())) {
			return SWORD1;
		} else if (str.equals(SWORD2.toString())) {
			return SWORD2;
		} else if (str.equals(WAND1.toString())) {
			return WAND1;
		} else if (str.equals(WAND2.toString())) {
			return WAND2;
		} else if (str.equals(HEALING_POT.toString())) {
			return HEALING_POT;
		} else if (str.equals(MANA_POT.toString())) {
			return MANA_POT;
		} else if (str.equals(SUPER_POT.toString())) {
			return SUPER_POT;
		} else if (str.equals(SCROLL1.toString())) {
			return SCROLL1;
		} else if (str.equals(SCROLL2.toString())) {
			return SCROLL2;
		} else if (str.equals(TRAP.toString())) {
			return TRAP;
		} else if (str.equals(ACTIVATED_TRAP.toString())) {
			return ACTIVATED_TRAP;
		} else if (str.equals(CHEST.toString())) {
			return CHEST;
		} else if (str.equals(LIT_BULB.toString())) {
			return LIT_BULB;
		} else if (str.equals(UNLIT_BULB.toString())) {
			return UNLIT_BULB;
		} else {
			return null;
		}
	}
}
