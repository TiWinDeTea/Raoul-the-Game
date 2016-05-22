package com.github.tiwindetea.dungeonoflegend.events;

/**
 * Created by Maxime on 22/05/2016.
 */
public class ScoreUpdateEvent extends Event {
	public int newScore;

	public ScoreUpdateEvent(int newScore) {
		this.newScore = newScore;
	}

	@Override
	public EventType getType() {
		return EventType.SCORE_UPDATE_EVENT;
	}
}
