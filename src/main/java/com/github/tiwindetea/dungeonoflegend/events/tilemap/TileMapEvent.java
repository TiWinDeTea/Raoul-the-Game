package com.github.tiwindetea.dungeonoflegend.events.tilemap;

import com.github.tiwindetea.dungeonoflegend.events.Event;
import com.github.tiwindetea.dungeonoflegend.events.EventType;

/**
 * Created by maxime on 5/10/16.
 */
public abstract class TileMapEvent extends Event {

    public abstract TileMapEventType getSubType();

    public EventType getType() {
        return EventType.TILEMAP_EVENT;
    }
}
