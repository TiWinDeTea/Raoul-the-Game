package com.github.tiwindetea.dungeonoflegend.listeners.game.entities.living_entities;

import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityLOSModificationEvent;

/**
 * Created by maxime on 5/6/16.
 */
public interface LivingEntityLOSModificationListener {
	void modifieLivingEntityLOS(LivingEntityLOSModificationEvent e);
}
