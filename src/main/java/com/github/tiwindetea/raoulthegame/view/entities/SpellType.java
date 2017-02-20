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
 * The enum SpellType
 *
 * @author Lucas LAZARE
 */
public enum SpellType {
	SAVIOR {
		@Override
		public String toString() {
			return resourceBundle.getString("savior.string");
		}
	},
	SAMPLE_SPELL {
		@Override
		public String toString() {
			return resourceBundle.getString("sample_spell.string");
		}
	},
	REGEN {
		@Override
		public String toString() {
			return resourceBundle.getString("regen.string");
		}
	},
	DRAINER {
		@Override
		public String toString() {
			return resourceBundle.getString("drainer.string");
		}
	},
	BONUS_HP {
		@Override
		public String toString() {
			return resourceBundle.getString("bonus_hp.string");
		}
	},
	Berserker {
		@Override
		public String toString() {
			return resourceBundle.getString("berserker.string");
		}
	},
	SUMMON_DOG {
		@Override
		public String toString() {
			return resourceBundle.getString("summon_dog.string");
		}
	}, TELEPORT{
		@Override
		public String toString() {
			return resourceBundle.getString("teleport.string");
		}
	}, IRON_WILL{
		@Override
		public String toString() {
			return resourceBundle.getString("iron_will.string");
		}
	}, HEAL{
		@Override
		public String toString() {
			return resourceBundle.getString("heal.string");
		}
	};

	private final static String bundleName = MainPackage.name + ".Spell";

	private final static ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName, Locale.getDefault());

	/**
	 * Gets sprite position.
	 *
	 * @return the sprite position
	 */
	public Vector2i getSpritePosition() {
		return new Vector2i(Integer.parseInt(resourceBundle.getString(this.toString() + ".sprite.position.x")),
		  Integer.parseInt(resourceBundle.getString(this.toString() + ".sprite.position.y")));
	}

	/**
	 * Gets Spells image.
	 *
	 * @return the image
	 */
	public Image getImage() {
		switch(resourceBundle.getString(this.toString() + ".sprite.file")) {
		case "Spells.png":
			return ViewPackage.SPELLS_IMAGE;
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

    static SpellType[] thisValues = SpellType.values();
    static String[] thisStrings = null;

    public String key;

	SpellType() {
		
	}

    SpellType(String ressourceBundleName) {
        this.key = ressourceBundleName;
    }

    static SpellType parseSpellType(String str) {
        if (thisStrings == null) {
            thisStrings = new String[thisValues.length];
            for (int i = 0; i < thisValues.length; i++) {
                thisStrings[i] = thisValues[i].toString();
            }
        }
        for (int i = 0; i < thisValues.length; i++) {
            if (thisStrings[i].equals(str)) {
                return thisValues[i];
            }
        }
        return null;
    }
}
