//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.events.requests;

/**
 * The type CenterViewRequestEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class CenterViewRequestEvent extends RequestEvent {

	/**
	 * Instantiates a new CenterViewRequestEvent.
	 */
	public CenterViewRequestEvent() {
	}

	@Override
	public RequestEventType getSubType() {
		return RequestEventType.CENTER_VIEW_REQUEST_EVENT;
	}
}
