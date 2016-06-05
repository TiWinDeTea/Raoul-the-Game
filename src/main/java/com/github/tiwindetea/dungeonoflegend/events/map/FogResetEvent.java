//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.events.map;

/**
 * Created by maxime on 5/20/16.
 */
public class FogResetEvent extends MapEvent {

	public FogResetEvent() {
	}

    @Override
    public MapEventType getSubType() {
        return MapEventType.FOG_RESET_EVENT;
    }
}
