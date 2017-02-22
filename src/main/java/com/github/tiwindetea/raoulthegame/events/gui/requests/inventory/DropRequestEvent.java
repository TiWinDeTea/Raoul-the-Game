//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.gui.requests.inventory;

import com.github.tiwindetea.raoulthegame.events.gui.requests.RequestEventType;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;

/**
 * The type DropRequestEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class DropRequestEvent extends InventoryRequestEvent {
	private Vector2i dropPosition;

	/**
	 * Instantiates a new DropRequestEvent.
	 *
	 * @param objectId     the object id
	 * @param dropPosition the drop position
	 */
	public DropRequestEvent(long objectId, Vector2i dropPosition) {
		super(objectId);
		this.dropPosition = dropPosition;
	}

	@Override
	public RequestEventType getSubType() {
		return RequestEventType.DROP_REQUEST_EVENT;
	}

	/**
	 * Gets drop position.
	 *
	 * @return the drop position
	 */
	public Vector2i getDropPosition() {
		return this.dropPosition;
	}
}
