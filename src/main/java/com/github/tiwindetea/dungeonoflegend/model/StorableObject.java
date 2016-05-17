//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.model;

import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntityType;

/**
 * StorableObject
 *
 * @author Lucas LAZARE
 */
public interface StorableObject extends Descriptable {

    /**
     * Gets the Storable Object type.
     *
     * @return The storable object type
     *
     * @see StorableObjectType
     */
    StorableObjectType getType();

    /**
     * Gets graphical type.
     *
     * @return the graphical type
     */
    StaticEntityType getGType();
}
