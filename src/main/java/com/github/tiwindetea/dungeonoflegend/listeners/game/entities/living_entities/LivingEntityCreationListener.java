package com.github.tiwindetea.dungeonoflegend.listeners.game.entities.living_entities;

import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityCreationEvent;

/**
 * Created by maxime on 5/6/16.
 */
public interface LivingEntityCreationListener {
	void createLivingEntity(LivingEntityCreationEvent e);
}
