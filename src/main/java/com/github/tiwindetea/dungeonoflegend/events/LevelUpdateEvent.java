package com.github.tiwindetea.dungeonoflegend.events;

import static com.github.tiwindetea.dungeonoflegend.events.EventType.LEVEL_UPDATE_EVENT;

/**
 * Created by maxime on 6/1/16.
 */
public class LevelUpdateEvent extends Event {
	public int newLevel;

	public LevelUpdateEvent(int newLevel) {
		this.newLevel = newLevel;
	}

	@Override
	public EventType getType() {
		return LEVEL_UPDATE_EVENT;
	}
}
