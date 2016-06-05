//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.events.static_entities;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntityType;

/**
 * Created by maxime on 5/3/16.
 */
public class StaticEntityCreationEvent extends StaticEntityEvent {
	public StaticEntityType type;
	public Vector2i position;
	public String description;

	public StaticEntityCreationEvent(long entityId, StaticEntityType type, Vector2i position, String description) {
		super(entityId);
		this.type = type;
		this.position = position;
		this.description = description;
	}

	@Override
	public StaticEntityEventType getSubType() {
		return StaticEntityEventType.STATIC_ENTITY_CREATION_EVENT;
	}
}
