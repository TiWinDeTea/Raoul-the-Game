//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events;

/**
 * The enum EventType.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public enum EventType {
    TILEMAP_EVENT,
    STATIC_ENTITY_EVENT,
    REQUEST_EVENT,
    PLAYER_EVENT,
    PLAYER_INVENTORY_EVENT,
    MAP_EVENT,
	LIVING_ENTITY_EVENT,
	SCORE_UPDATE_EVENT,
    LEVEL_UPDATE_EVENT,
	SPELL_EVENT,
	HUD_SPELL_CLICK_EVENT,
	SELECTOR_SPELL_CLICK_EVENT,
	SPELL_SELECTION_EVENT
}
