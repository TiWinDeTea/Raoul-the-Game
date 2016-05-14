//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.model;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * ConsumableType
 * @author Lucas LAZARE
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

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return null;
	}

	public Vector2i getSpritePosition() {
		return null;
	}
}
