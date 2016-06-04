package com.github.tiwindetea.dungeonoflegend.events.playerinventory;

/**
 * Created by organic-code on 6/4/16.
 */
public class ObjectDragEvent extends PlayerInventoryEvent {
    public Long objectId;

    public ObjectDragEvent(Long objectId) {
        this.objectId = objectId;
    }

    @Override
    public PlayerInventoryEventType getSubType() {
        return PlayerInventoryEventType.OBJECT_DRAG_EVENT;
    }
}
