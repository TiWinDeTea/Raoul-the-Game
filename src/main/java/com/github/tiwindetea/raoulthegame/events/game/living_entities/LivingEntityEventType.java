//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.game.living_entities;

/**
 * The enum LivingEntityEventType.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public enum LivingEntityEventType {
    LIVING_ENTITY_CREATION_EVENT,
    LIVING_ENTITY_DELETION_EVENT,
    LIVING_ENTITY_LOS_DEFINITION_EVENT,
    LIVING_ENTITY_LOS_MODIFICATION_EVENT,
    LIVING_ENTITY_MOVE_EVENT,
    LIVING_ENTITY_HEALTH_UPDATE_EVENT,
    LIVING_ENTITY_HEALTH_VISIBILITY_EVENT,
    LIVING_ENTITY_MANA_UPDATE_EVENT,
    LIVING_ENTITY_XP_UPDATE_EVENT
}
