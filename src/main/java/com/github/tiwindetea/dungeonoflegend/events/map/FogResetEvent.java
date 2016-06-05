//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.events.map;

/**
 * The type FogResetEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class FogResetEvent extends MapEvent {

	/**
	 * Instantiates a new FogResetEvent.
	 */
	public FogResetEvent() {
	}

    @Override
    public MapEventType getSubType() {
        return MapEventType.FOG_RESET_EVENT;
    }
}
