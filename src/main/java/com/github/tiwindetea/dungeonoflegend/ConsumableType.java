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
	},
	SCROLL {
		@Override
		public String toString() {
			return resourceBundle.getString("scroll");
		}
	};

	private final static String bundleName = "ConsumableType";

	private final static ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName, Locale.getDefault());

	public String toString() {
		return null;
	}
}
