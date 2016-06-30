//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.events.tilemap;

import com.github.tiwindetea.raoulthegame.model.space.Vector2i;

/**
 * The type TileDragEvent.
 *
 * @author Maxime PINARD
 * @author Lucas LAZARE
 */
public class TileDragEvent extends TileMapEvent {
    private Vector2i tilePosition;
    private long objectId;

    /**
     * Instantiates a new TileDragEvent.
     *
     * @param tilePosition the tile position
     * @param objectId     the object id
     */
    public TileDragEvent(Vector2i tilePosition, long objectId) {
        this.tilePosition = tilePosition;
        this.objectId = objectId;
    }

    @Override
    public TileMapEventType getSubType() {
        return TileMapEventType.TILE_DRAG_EVENT;
    }

    /**
     * Gets tile position.
     *
     * @return the tile position
     */
    public Vector2i getTilePosition() {
        return this.tilePosition;
    }

    /**
     * Gets object id.
     *
     * @return the object id
     */
    public long getObjectId() {
        return this.objectId;
    }
}
