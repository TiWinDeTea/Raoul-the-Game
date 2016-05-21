package com.github.tiwindetea.dungeonoflegend.events.requests;

/**
 * Created by maxime on 5/21/16.
 */
public class CenterViewRequestEvent extends RequestEvent {

	public CenterViewRequestEvent() {
	}

	@Override
	public RequestEventType getSubType() {
		return RequestEventType.CENTER_VIEW_REQUEST_EVENT;
	}
}
