package com.github.tiwindetea.dungeonoflegend;

/**
 * Created by maxime on 4/24/16.
 */
public class Map {
	private static Map ourInstance = new Map();

	public static Map getInstance() {
		return ourInstance;
	}

	private Map() {
	}
}
