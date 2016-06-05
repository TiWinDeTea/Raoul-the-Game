//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.events.requests;

import com.github.tiwindetea.dungeonoflegend.model.Direction;

/**
 * Created by maxime on 5/6/16.
 */
public class MoveRequestEvent extends RequestEvent {
	public Direction moveDirection;

	public MoveRequestEvent(Direction moveDirection) {
		this.moveDirection = moveDirection;
	}

	@Override
	public RequestEventType getSubType() {
		return RequestEventType.MOVE_REQUEST_EVENT;
	}
}
