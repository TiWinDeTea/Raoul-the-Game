package com.github.tiwindetea.dungeonoflegend.model;

import java.util.ResourceBundle;

/**
 * Created by maxime on 4/24/16.
 */
public enum Tile {
	WALL_LEFT {
		public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("wall-left.sprite.position.x")),
					Integer.parseInt(resourceBundle.getString("wall-left.sprite.position.y")));
		}
	},
	WALL_RIGHT {
		public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("wall-right.sprite.position.x")),
					Integer.parseInt(resourceBundle.getString("wall-right.sprite.position.y")));
		}
	},
	WALL_DOWN {
		public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("wall-down.sprite.position.x")),
					Integer.parseInt(resourceBundle.getString("wall-down.sprite.position.y")));
		}
	},
	WALL_TOP {
		public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("wall-top.sprite.position.x")),
					Integer.parseInt(resourceBundle.getString("wall-top.sprite.position.y")));
		}
	},
	WALL_TOPLEFT {
		public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("wall-topleft.sprite.position.x")),
					Integer.parseInt(resourceBundle.getString("wall-topleft.sprite.position.y")));
		}
	},
	WALL_TOPRIGHT {
		public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("wall-topright.sprite.position.x")),
					Integer.parseInt(resourceBundle.getString("wall-topright.sprite.position.y")));
		}
	},
	WALL_DOWNLEFT {
		public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("wall-downleft.sprite.position.x")),
					Integer.parseInt(resourceBundle.getString("wall-downleft.sprite.position.y")));
		}
	},
	WALL_DOWNRIGHT {
		public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("wall-downright.sprite.position.x")),
					Integer.parseInt(resourceBundle.getString("wall-downright.sprite.position.y")));
		}
	},
	PILLAR {
		public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("pillar.sprite.position.x")),
					Integer.parseInt(resourceBundle.getString("pillar.sprite.position.y")));
		}
	},
	GROUND {
		public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("ground.sprite.position.x")),
			  Integer.parseInt(resourceBundle.getString("ground.sprite.position.y")));
		}
	},
	OPENED_DOOR {
		public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("opened-door.sprite.position.x")),
					Integer.parseInt(resourceBundle.getString("opened-door.sprite.position.y")));
		}
	},
	CLOSED_DOOR {
		public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("closed-door.sprite.position.x")),
			  Integer.parseInt(resourceBundle.getString("closed-door.sprite.position.y")));
		}
	},
	STAIR_UP {
		public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("stair-up.sprite.position.x")),
			  Integer.parseInt(resourceBundle.getString("stair-up.sprite.position.y")));
		}
	},
	STAIR_DOWN {
		public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("stair-down.sprite.position.x")),
			  Integer.parseInt(resourceBundle.getString("stair-down.sprite.position.y")));
		}
	},
	HOLE {
		public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("hole.sprite.position.x")),
					Integer.parseInt(resourceBundle.getString("hole.sprite.position.y")));
		}
	},
	UNKNOWN {
		public Vector2i getSpritePosition() {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("unknown.sprite.position.x")),
			  Integer.parseInt(resourceBundle.getString("unknown.sprite.position.y")));
		}
	};

	private final static String bundleName = MainPackage.name + ".Tile";

	private final static ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName);

	public static boolean isObstructed(Tile tile) {
		switch (tile) {
			case WALL_LEFT:
			case WALL_RIGHT:
			case WALL_DOWN:
			case WALL_TOP:
			case WALL_TOPLEFT:
			case WALL_TOPRIGHT:
			case WALL_DOWNLEFT:
			case WALL_DOWNRIGHT:
			case CLOSED_DOOR:
			case PILLAR:
			case UNKNOWN:
			case HOLE:
				return true;
			case GROUND:
			case OPENED_DOOR:
			case STAIR_UP:
			case STAIR_DOWN:
			default:
				return false;
		}
	}

	public Vector2i getSpritePosition() {
		return null;
	}
}