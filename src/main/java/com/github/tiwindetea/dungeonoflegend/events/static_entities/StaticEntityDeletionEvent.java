package com.github.tiwindetea.dungeonoflegend.events.static_entities;

/**
 * Created by maxime on 5/3/16.
 */
public class StaticEntityDeletionEvent extends StaticEntityEvent {
	public StaticEntityDeletionEvent(long entityId) {
		super(entityId);
	}
    @Override
    public StaticEntityEventType getSubType() {
        return StaticEntityEventType.STATIC_ENTITY_DELETION_EVENT;
    }

}
