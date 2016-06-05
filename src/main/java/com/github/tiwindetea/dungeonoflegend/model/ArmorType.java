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
 * ArmorType
 *
 * @author Lucas LAZARE
 */
public enum ArmorType {
	BREAST_PLATE {
		@Override
		public String toString() {
			return resourceBundle.getString("breast-plate.name");
		}
	},
	GLOVES {
		@Override
		public String toString() {
			return resourceBundle.getString("gloves.name");
		}
	},
	HELMET {

		@Override
		public String toString() {
			return resourceBundle.getString("helmet.name");
		}
	},
	BOOTS {
		@Override
		public String toString() {
			return resourceBundle.getString("boots.name");
		}
	},
	PANTS {
		@Override
		public String toString() {
			return resourceBundle.getString("pants.name");
		}
	};

	private final static String bundleName = MainPackage.name + ".ArmorType";

	private final static ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName, Locale.getDefault());

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return null;
	}

	/**
	 * Parses an ArmorType.
	 *
	 * @param str an ArmorType's String
	 * @return the ArmorType
	 */
	public static ArmorType parseArmorType(String str) {
		if (str.equals(BREAST_PLATE.toString())) {
			return BREAST_PLATE;
		} else if (str.equals(GLOVES.toString())) {
			return GLOVES;
		} else if (str.equals(HELMET.toString())) {
			return HELMET;
		} else if (str.equals(BOOTS.toString())) {
			return BOOTS;
		} else if (str.equals(PANTS.toString())) {
			return PANTS;
		} else {
			return null;
		}
	}
}
