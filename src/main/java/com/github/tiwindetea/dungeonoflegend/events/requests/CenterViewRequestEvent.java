package com.github.tiwindetea.dungeonoflegend.events.requests;

/**
 * Created by maxime on 5/21/16.
 */
public class CenterViewRequestEvent extends RequestEvent {

	@Override
	public RequestEventType getType() {
		return RequestEventType.CENTER_VIEW_REQUEST_EVENT;
	}
}
