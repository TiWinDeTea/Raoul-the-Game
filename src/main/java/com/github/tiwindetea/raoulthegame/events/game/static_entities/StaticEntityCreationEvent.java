//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.game.static_entities;

import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.github.tiwindetea.raoulthegame.view.entities.StaticEntityType;

/**
 * The type StaticEntityCreationEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class StaticEntityCreationEvent extends StaticEntityEvent {
	private StaticEntityType staticEntityType;
	private Vector2i position;
	private String description;

	/**
	 * Instantiates a new StaticEntityCreationEvent.
	 *
	 * @param entityId         the entity id
	 * @param staticEntityType the static entity type
	 * @param position         the position
	 * @param description      the description
	 */
	public StaticEntityCreationEvent(long entityId, StaticEntityType staticEntityType, Vector2i position, String description) {
		super(entityId);
		this.staticEntityType = staticEntityType;
		this.position = position;
		this.description = description;
	}

	@Override
	public StaticEntityEventType getSubType() {
		return StaticEntityEventType.STATIC_ENTITY_CREATION_EVENT;
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
	 * Gets position.
	 *
	 * @return the position
	 */
	public Vector2i getPosition() {
		return this.position;
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
