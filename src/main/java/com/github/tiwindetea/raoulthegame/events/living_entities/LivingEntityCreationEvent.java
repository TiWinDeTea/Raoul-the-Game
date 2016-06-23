//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.living_entities;

import com.github.tiwindetea.raoulthegame.model.Direction;
import com.github.tiwindetea.raoulthegame.model.Vector2i;
import com.github.tiwindetea.raoulthegame.view.entities.LivingEntityType;

/**
 * The type LivingEntityCreationEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class LivingEntityCreationEvent extends LivingEntityEvent {
	private LivingEntityType livingEntityType;
	private Vector2i position;
	private Direction direction;
	private String description;

	/**
	 * Instantiates a new LivingEntityCreationEvent.
	 *
	 * @param entityId         the entity id
	 * @param livingEntityType the livingEntityType
	 * @param position         the position
	 * @param direction        the direction
	 * @param description      the description
	 */
	public LivingEntityCreationEvent(long entityId, LivingEntityType livingEntityType, Vector2i position, Direction direction, String description) {
		super(entityId);
		this.livingEntityType = livingEntityType;
		this.position = position;
		this.direction = direction;
		this.description = description;
	}
    @Override
    public LivingEntityEventType getSubType() {
        return LivingEntityEventType.LIVING_ENTITY_CREATION_EVENT;
    }

	/**
	 * Gets living entity staticEntityType.
	 *
	 * @return the living entity staticEntityType
	 */
	public LivingEntityType getLivingEntityType() {
		return this.livingEntityType;
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
	 * Gets direction.
	 *
	 * @return the direction
	 */
	public Direction getDirection() {
		return this.direction;
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
