//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.model;

import java.util.ResourceBundle;

/**
 * Tile
 *
 * @author Lucas LAZARE
 */
public enum Tile {
	WALL_LEFT {
		public Vector2i getSpritePosition(int subIndex) {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("wall-left.sprite.position.x")),
					Integer.parseInt(resourceBundle.getString("wall-left.sprite.position.y")));
		}
	},
	WALL_RIGHT {
		public Vector2i getSpritePosition(int subIndex) {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("wall-right.sprite.position.x")),
					Integer.parseInt(resourceBundle.getString("wall-right.sprite.position.y")));
		}
	},
	WALL_DOWN {
		public Vector2i getSpritePosition(int subIndex) {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("wall-down.sprite.position.x")),
					Integer.parseInt(resourceBundle.getString("wall-down.sprite.position.y")));
		}
	},
	WALL_TOP {
		public Vector2i getSpritePosition(int subIndex) {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("wall-top.sprite.position.x")),
					Integer.parseInt(resourceBundle.getString("wall-top.sprite.position.y")));
		}
	},
	WALL_TOPLEFT {
		public Vector2i getSpritePosition(int subIndex) {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("wall-topleft.sprite.position.x")),
					Integer.parseInt(resourceBundle.getString("wall-topleft.sprite.position.y")));
		}
	},
	WALL_TOPRIGHT {
		public Vector2i getSpritePosition(int subIndex) {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("wall-topright.sprite.position.x")),
					Integer.parseInt(resourceBundle.getString("wall-topright.sprite.position.y")));
		}
	},
	WALL_DOWNLEFT {
		public Vector2i getSpritePosition(int subIndex) {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("wall-downleft.sprite.position.x")),
					Integer.parseInt(resourceBundle.getString("wall-downleft.sprite.position.y")));
		}
	},
	WALL_DOWNRIGHT {
		public Vector2i getSpritePosition(int subIndex) {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("wall-downright.sprite.position.x")),
					Integer.parseInt(resourceBundle.getString("wall-downright.sprite.position.y")));
		}
	},
	PILLAR {
		public Vector2i getSpritePosition(int subIndex) {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("pillar.sprite.position.x")),
					Integer.parseInt(resourceBundle.getString("pillar.sprite.position.y")));
		}
	},
	GROUND {
		public Vector2i getSpritePosition(int subIndex) {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("ground.sprite.position.x")) + subIndex % 5,
			  Integer.parseInt(resourceBundle.getString("ground.sprite.position.y")));
		}
	},
	OPENED_DOOR {
		public Vector2i getSpritePosition(int subIndex) {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("opened-door.sprite.position.x")),
					Integer.parseInt(resourceBundle.getString("opened-door.sprite.position.y")));
		}
	},
	CLOSED_DOOR {
		public Vector2i getSpritePosition(int subIndex) {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("closed-door.sprite.position.x")),
			  Integer.parseInt(resourceBundle.getString("closed-door.sprite.position.y")));
		}
	},
	STAIR_UP {
		public Vector2i getSpritePosition(int subIndex) {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("stair-up.sprite.position.x")),
			  Integer.parseInt(resourceBundle.getString("stair-up.sprite.position.y")));
		}
	},
	STAIR_DOWN {
		public Vector2i getSpritePosition(int subIndex) {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("stair-down.sprite.position.x")),
			  Integer.parseInt(resourceBundle.getString("stair-down.sprite.position.y")));
		}
	},
	HOLE {
		public Vector2i getSpritePosition(int subIndex) {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("hole.sprite.position.x")),
					Integer.parseInt(resourceBundle.getString("hole.sprite.position.y")));
		}
	},
	UNKNOWN {
		public Vector2i getSpritePosition(int subIndex) {
			return new Vector2i(Integer.parseInt(resourceBundle.getString("unknown.sprite.position.x")),
			  Integer.parseInt(resourceBundle.getString("unknown.sprite.position.y")));
		}
	};

	private final static String bundleName = MainPackage.name + ".Tile";
	private final static ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName);

	/**
	 * @param tile tile
	 * @return True if the tile is obstructed (ie : wall), false otherwise
	 */
	public static boolean isObstructed(Tile tile) {
		switch (tile) {
			case GROUND:
			case OPENED_DOOR:
			case STAIR_UP:
			case STAIR_DOWN:
				return false;
			/*case WALL_LEFT:
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
			case HOLE:*/
			default:
				return true;
		}
	}

	/**
	 * Returns true if the tile is a border of a room (wall, door, ...) [with current Map generator]
	 * @param tile The concerned tile
	 * @return     true if the tile is a border of a room
     */
	public static boolean isRoomBorder(Tile tile) {
		switch (tile) {
			case PILLAR:
			case GROUND:
			case STAIR_UP:
			case STAIR_DOWN:
			case HOLE:
			case UNKNOWN:
				return false;
			/*case WALL_LEFT:
			case WALL_RIGHT:
			case WALL_DOWN:
			case WALL_TOP:
			case WALL_TOPLEFT:
			case WALL_TOPRIGHT:
			case WALL_DOWNLEFT:
			case WALL_DOWNRIGHT:
			case OPENED_DOOR:
			case CLOSED_DOOR:*/
			default:
				return true;
		}
	}

	/**
	 * Returns the position of the tile in the spriteSheet
	 * @param subIndex subindex (ie : Tile.GROUND has more than 1 sprite)
	 * @return the position of the tile in the spriteSheet
	 */
	public Vector2i getSpritePosition(int subIndex) {
		return null;
	}
}