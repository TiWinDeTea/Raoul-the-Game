//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.requests;

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
