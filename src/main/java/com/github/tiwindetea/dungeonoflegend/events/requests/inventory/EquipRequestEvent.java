package com.github.tiwindetea.dungeonoflegend.events.requests.inventory;

import com.github.tiwindetea.dungeonoflegend.events.requests.RequestEventType;

/**
 * Created by organic-code on 6/4/16.
 */
public class EquipRequestEvent extends InventoryRequestEvent {

    public EquipRequestEvent(long objectId) {
        super(objectId);
    }

    @Override
    public RequestEventType getSubType() {
        return RequestEventType.EQUIP_REQUEST_EVENT;
    }
}
