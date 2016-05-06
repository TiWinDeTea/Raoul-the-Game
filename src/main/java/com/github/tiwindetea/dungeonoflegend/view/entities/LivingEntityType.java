package com.github.tiwindetea.dungeonoflegend.view.entities;

import com.github.tiwindetea.dungeonoflegend.model.MainPackage;
import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import com.github.tiwindetea.dungeonoflegend.view.ViewPackage;
import javafx.scene.image.Image;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by maxime on 5/5/16.
 */
public enum LivingEntityType {
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
	LITTLE_PUNK_DUCK {
		@Override
		public String toString() {
			return resourceBundle.getString("little-punk-duck.string");
		}
	},
	PUNK_DUCK {
		@Override
		public String toString() {
			return resourceBundle.getString("punk-duck.string");
		}
	},
	LITTLE_ARISTOCRAT_DUCK {
		@Override
		public String toString() {
			return resourceBundle.getString("little-aristocrat-duck.string");
		}
	},
	ARISTOCRAT_DUCK {
		@Override
		public String toString() {
			return resourceBundle.getString("aristocrat-duck.string");
		}
	},
	LITTLE_MONSTER_TRUCK_DUCK {
		@Override
		public String toString() {
			return resourceBundle.getString("little-monster-truck-duck.string");
		}
	},
	MONSTER_TRUCK_DUCK {
		@Override
		public String toString() {
			return resourceBundle.getString("monster-truck-duck.string");
		}
	},
	LITTLE_SPACE_SHIP_DUCK {
		@Override
		public String toString() {
			return resourceBundle.getString("little-space-ship-duck.string");
		}
	},
	SPACE_SHIP_DUCK {
		@Override
		public String toString() {
			return resourceBundle.getString("space-ship-duck.string");
		}
	},
	LITTLE_SATANIC_DUCK {
		@Override
		public String toString() {
			return resourceBundle.getString("little-satanic-duck.string");
		}
	},
	SATANIC_DUCK {
		@Override
		public String toString() {
			return resourceBundle.getString("satanic-duck.string");
		}
	};

	private final static String bundleName = MainPackage.name + ".Player";

	private final static ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName, Locale.getDefault());

	@Override
	public String toString() {
		return null;
	}

	public Vector2i getSpriteDownPosition() {
		return new Vector2i(Integer.parseInt(resourceBundle.getString(this.toString() + ".sprite.down.position.x")),
		  Integer.parseInt(resourceBundle.getString(this.toString() + ".sprite.down.position.y")));
	}

	public Vector2i getSpriteUpPosition() {
		return new Vector2i(Integer.parseInt(resourceBundle.getString(this.toString() + ".sprite.up.position.x")),
		  Integer.parseInt(resourceBundle.getString(this.toString() + ".sprite.up.position.y")));
	}

	public Vector2i getSpriteRightPosition() {
		return new Vector2i(Integer.parseInt(resourceBundle.getString(this.toString() + ".sprite.right.position.x")),
		  Integer.parseInt(resourceBundle.getString(this.toString() + ".sprites.right.position.y")));
	}

	public Vector2i getSpriteLeftPosition() {
		return new Vector2i(Integer.parseInt(resourceBundle.getString(this.toString() + ".sprite.left.position.x")),
		  Integer.parseInt(resourceBundle.getString(this.toString() + ".sprites.left.position.y")));
	}

	public Image getImage() {
		switch(resourceBundle.getString(this.toString() + ".sprites.file")) {
		case "Objects.png":
			return ViewPackage.objectsImage;
		case "Players.png":
			return ViewPackage.playersImage;
		case "Enemies.png":
			return ViewPackage.enemiesImage;
		default:
			return ViewPackage.playersImage; // for tests
		//return null;
		}
	}
}
