package com.github.tiwindetea.dungeonoflegend;

/**
 * Created by maxime on 4/24/16.
 */
public enum Tile {
	WALL {
		/*public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("wall.sprite.position.x")),
			  Integer.parseInt(resourceBundle.getString("wall.sprite.position.y")));
		}*/
	},
	GROUND {
		/*public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("ground.sprite.position.x")),
			  Integer.parseInt(resourceBundle.getString("ground.sprite.position.y")));
		}*/
	},
	OPENED_DOOR {
		/*public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("opened-door.sprite.position.x")),
					Integer.parseInt(resourceBundle.getString("opened-door.sprite.position.y")));
		}*/
	},
	CLOSED_DOOR {
		/*public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("closed-door.sprite.position.x")),
			  Integer.parseInt(resourceBundle.getString("closed-door.sprite.position.y")));
		}*/
	},
	STAIR_UP {
		/*public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("stair-up.sprite.position.x")),
			  Integer.parseInt(resourceBundle.getString("stair-up.sprite.position.y")));
		}*/
	},
	STAIR_DOWN {
		/*public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("stair-down.sprite.position.x")),
			  Integer.parseInt(resourceBundle.getString("stair-down.sprite.position.y")));
		}*/
	},
	UNKNOWN {
		/*public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("unknown.sprite.position.x")),
			  Integer.parseInt(resourceBundle.getString("unknown.sprite.position.y")));
		}*/
	}
	;

	//private final static String bundleName = "Tile";

	//private final static ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName);

	/*public Vector2i getSpritePosition() {
		return null;
	}*/
}