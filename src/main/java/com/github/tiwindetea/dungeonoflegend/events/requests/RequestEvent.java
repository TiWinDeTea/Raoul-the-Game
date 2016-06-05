package com.github.tiwindetea.dungeonoflegend.events.requests;

import com.github.tiwindetea.dungeonoflegend.events.Event;
import com.github.tiwindetea.dungeonoflegend.events.EventType;

/**
 * Created by maxime on 5/6/16.
 */
public abstract class RequestEvent extends Event {
    public abstract RequestEventType getSubType();

    public EventType getType() {
        return EventType.REQUEST_EVENT;
    }
}
