//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.events;

/**
 * The type Event.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public abstract class Event {

	/**
	 * Instantiates a new Event.
	 */
	public Event() {
	}

	/**
	 * Gets type.
	 *
	 * @return the type
	 */
	public abstract EventType getType();
}
