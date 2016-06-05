//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

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
