//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.events.tilemap;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;

/**
 * Created by organic-code on 5/24/16.
 */
public class TileDragEvent extends TileMapEvent {
    public Vector2i tilePosition;
    public long objectId;

    public TileDragEvent(Vector2i tilePosition, long objectId) {
        this.tilePosition = tilePosition;
        this.objectId = objectId;
    }

    @Override
    public TileMapEventType getSubType() {
        return TileMapEventType.TILE_DRAG_EVENT;
    }
}
