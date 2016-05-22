package com.github.tiwindetea.dungeonoflegend.listeners.game.entities.living_entities;

import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityHealthUpdateEvent;

/**
 * Created by Maxime on 22/05/2016.
 */
public interface LivingEntityHealthUpdateListener {
	void updateLivingEntityHealth(LivingEntityHealthUpdateEvent e);
}
