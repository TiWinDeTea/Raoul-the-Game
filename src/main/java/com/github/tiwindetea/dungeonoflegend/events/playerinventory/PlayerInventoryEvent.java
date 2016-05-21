package com.github.tiwindetea.dungeonoflegend.events.playerinventory;

import com.github.tiwindetea.dungeonoflegend.events.Event;
import com.github.tiwindetea.dungeonoflegend.events.EventType;

/**
 * Created by maxime on 5/18/16.
 */
public abstract class PlayerInventoryEvent extends Event {

    public EventType getType() {
        return EventType.PLAYER_INVENTORY_EVENT;
    }

    public abstract PlayerInventoryEventType getSubType();
}
