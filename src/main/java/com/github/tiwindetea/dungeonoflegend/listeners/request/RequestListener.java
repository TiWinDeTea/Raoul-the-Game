package com.github.tiwindetea.dungeonoflegend.listeners.request;

import com.github.tiwindetea.dungeonoflegend.listeners.request.inventory.InventoryRequestListener;
import com.github.tiwindetea.dungeonoflegend.listeners.request.moves.MoveRequestListener;

/**
 * Created by maxime on 5/6/16.
 */
public interface RequestListener extends InventoryRequestListener, MoveRequestListener, InteractionRequestListener {
}
