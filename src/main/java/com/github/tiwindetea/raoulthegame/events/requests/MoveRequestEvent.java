//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.requests;

import com.github.tiwindetea.raoulthegame.model.space.Direction;

/**
 * The type MoveRequestEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class MoveRequestEvent extends RequestEvent {
	private Direction moveDirection;

	/**
	 * Instantiates a new MoveRequestEvent.
	 *
	 * @param moveDirection the move direction
	 */
	public MoveRequestEvent(Direction moveDirection) {
		this.moveDirection = moveDirection;
	}

	@Override
	public RequestEventType getSubType() {
		return RequestEventType.MOVE_REQUEST_EVENT;
	}

	/**
	 * Gets move direction.
	 *
	 * @return the move direction
	 */
	public Direction getMoveDirection() {
		return this.moveDirection;
	}
}
