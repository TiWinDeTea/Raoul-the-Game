package com.github.tiwindetea.dungeonoflegend.events.map;

import com.github.tiwindetea.dungeonoflegend.events.Event;
import com.github.tiwindetea.dungeonoflegend.events.EventType;

/**
 * Created by maxime on 5/6/16.
 */
public abstract class MapEvent extends Event {

    public EventType getType() {
        return EventType.MAP_EVENT;
    }

    public abstract MapEventType getSubType();
}
