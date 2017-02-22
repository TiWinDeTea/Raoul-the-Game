//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.game.players.inventory;

import com.github.tiwindetea.raoulthegame.events.game.players.PlayerEventType;
import com.github.tiwindetea.raoulthegame.view.entities.StaticEntityType;

/**
 * The type InventoryAdditionEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class InventoryAdditionEvent extends InventoryEvent {
	private boolean isEquiped;
	private StaticEntityType staticEntityType;
	private String description;

	/**
	 * Instantiates a new InventoryAdditionEvent.
	 *
	 * @param playerNumber     the player number
	 * @param objectId         the object id
	 * @param isEquiped        the is equiped
	 * @param staticEntityType the static entity type
	 * @param description      the description
	 */
	public InventoryAdditionEvent(int playerNumber, long objectId, boolean isEquiped, StaticEntityType staticEntityType, String description) {
		super(playerNumber, objectId);
		this.isEquiped = isEquiped;
		this.staticEntityType = staticEntityType;
		this.description = description;
	}

	@Override
	public PlayerEventType getSubType() {
		return PlayerEventType.INVENTORY_ADDITION_EVENT;
	}

	/**
	 * Is equiped boolean.
	 *
	 * @return the boolean
	 */
	public boolean isEquiped() {
		return this.isEquiped;
	}

	/**
	 * Gets static entity type.
	 *
	 * @return the static entity type
	 */
	public StaticEntityType getStaticEntityType() {
		return this.staticEntityType;
	}

	/**
	 * Gets description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}
}