package com.github.tiwindetea.dungeonoflegend.events.requests;

/**
 * The type LockViewRequestEvent.
 *
 * @author Lucas LAZARE
 */
public class LockViewRequestEvent extends RequestEvent {

    /**
     * Instantiates a new LevelUpdateEvent.
     */
    public LockViewRequestEvent() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RequestEventType getSubType() {
        return RequestEventType.LOCK_VIEW_REQUEST_EVENT;
    }
}
