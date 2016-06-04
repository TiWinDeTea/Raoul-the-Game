package com.github.tiwindetea.dungeonoflegend.listeners.request.inventory;

import com.github.tiwindetea.dungeonoflegend.events.requests.inventory.EquipRequestEvent;

/**
 * Created by organic-code on 6/4/16.
 */
public interface EquipRequestListener {
    void requestEquipping(EquipRequestEvent event);
}
