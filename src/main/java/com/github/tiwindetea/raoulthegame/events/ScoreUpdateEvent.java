//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events;

/**
 * The type ScoreUpdateEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class ScoreUpdateEvent extends Event {
	private int newScore;

	/**
	 * Instantiates a new ScoreUpdateEvent.
	 *
	 * @param newScore the new score
	 */
	public ScoreUpdateEvent(int newScore) {
		this.newScore = newScore;
	}

	@Override
	public EventType getType() {
		return EventType.SCORE_UPDATE_EVENT;
	}

	/**
	 * Gets new score.
	 *
	 * @return the new score
	 */
	public int getNewScore() {
		return this.newScore;
	}
}
