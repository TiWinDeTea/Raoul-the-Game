//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.model.items;

import com.github.tiwindetea.raoulthegame.model.livings.LivingThing;

/**
 * Consumable
 *
 * @author Lucas LAZARE
 */
public interface Consumable extends StorableObject {

    /**
     * Triggers a consumable.
     *
     * @param livingThing the target of the consumable
     */
    void trigger(LivingThing livingThing);

    /**
     * method to call after each game tick
     *
     * @return true if the object finished its effects, false otherwise
     */
    boolean nextTick();

    /**
     * Gets the consumable type.
     *
     * @return the consumable type
     *
     * @see ConsumableType
     */
    ConsumableType getConsumableType();
}
