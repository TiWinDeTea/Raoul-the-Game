package com.github.tiwindetea.dungeonoflegend.events.requests;

import com.github.tiwindetea.dungeonoflegend.events.EventType;

/**
 * Created by maxime on 5/6/16.
 */
public abstract class RequestEvent {
    public abstract RequestEventType getSubType();

    public EventType getType() {
        return EventType.REQUEST_EVENT;
    }
}
