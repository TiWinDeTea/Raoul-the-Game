//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.model.spells;

import com.github.tiwindetea.raoulthegame.listeners.game.spells.SpellListener;
import com.github.tiwindetea.raoulthegame.model.Pair;
import com.github.tiwindetea.raoulthegame.model.items.InteractiveObject;
import com.github.tiwindetea.raoulthegame.model.items.StorableObject;
import com.github.tiwindetea.raoulthegame.model.livings.LivingThing;
import com.github.tiwindetea.raoulthegame.model.livings.Mob;
import com.github.tiwindetea.raoulthegame.model.livings.Player;
import com.github.tiwindetea.raoulthegame.model.space.Direction;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.github.tiwindetea.raoulthegame.view.entities.LivingEntityType;
import com.github.tiwindetea.raoulthegame.view.entities.StaticEntityType;

import java.util.Collection;
import java.util.List;

/**
 * Created by maliafo on 9/18/16.
 */
public interface SpellsController {


    /**
     * Creates a 'ghost' Living entity at the indicated position. It won't have any logic consistency
     *
     * @param entityType  Type of the entity (for display)
     * @param position    Position of the entity to create
     * @param description Descrition of this entity
     * @param direction
     * @param LOSRange    Range of the LOS of the ghost entity  @return The graphic ID of this entity for further use (moving, deleting, etc…)
     */
    long createGhostEntity(LivingEntityType entityType, Vector2i position, String description, Direction direction, int LOSRange, int explorationRange);


    /**
     * Creates a 'ghost' Storable entity at the indicated position. It won't have any logic consistency
     *
     * @param entityType  Type of the entity (for display)
     * @param position    Position of the entity to create
     * @param description Description of this entity
     * @param LOSRange    Range of the LOS of the ghost entity
     * @return The graphic ID of this entity for further use (moving, deleting, etc…)
     */
    long createGhostEntity(StaticEntityType entityType, Vector2i position, String description, int LOSRange, int explorationRange);

    /**
     * Moves a ghost entity.
     *
     * @param entityId ID of the entity to move
     * @param position New position of the entitycreateEntity
     */
    void moveGhostLivingEntity(long entityId, Vector2i position);

    /**
     * Deletes a ghost storable entity
     *
     * @param entityId ID of the entity to delete
     */
    void deleteGhostStorableEntity(long entityId);

    /**
     * Deletes a ghost living entity
     *
     * @param entityId ID of the entity to delete
     */
    void deleteGhostLivingEntity(long entityId);

    /**
     * Creates a real Living entity with logic consistency.
     *
     * @param entity    The entity to create
     * @param aspect    Graphical aspect of the entity
     * @param direction Initial direction of the entity
     * @implSpec LivingThing#live(List, Collection, Collection, boolean[][])   LivingThing#live won't be called
     */
    void createEntity(LivingThing entity, LivingEntityType aspect, Direction direction);

    /**
     * Creates a real Storable entity with logic consistency
     *
     * @param entity the entity to create
     * @param aspect graphical aspect of the entity
     * @return The logic ID of this entity for further use
     */
    long createEntity(StorableObject entity, Vector2i position, StaticEntityType aspect);

    /**
     * Explores a zone
     *
     * @param position Position to explore
     * @param range    Range of the LOS
     */
    void explore(Vector2i position, int range);

    /**
     * Allows player to see the los of another living entity. Cannot be a 'ghost', as it is already set in ghosts entities
     *
     * @param id    ID of the entity who's LOS is shared
     * @param range Range of the entity's LOS
     */
    void shareLosWith(long id, int range);

    /**
     * Removes an entity from the shared los list
     *
     * @param id ID of the entity to remove from the list
     */
    void unshareLos(long id);

    /**
     * @return All living mobs
     */
    List<Mob> retrieveMobs();

    /**
     * @return All living players
     */
    Collection<Player> retrievePlayers();

    /**
     * @return All living spells
     */
    Collection<LivingThing> retrieveLivingSpells();

    /**
     * @return All interactive objects
     */
    Collection<Pair<InteractiveObject>> retrieveIO();

    /**
     * @return All storable objects
     */
    Collection<javafx.util.Pair<Vector2i, Pair<StorableObject>>> retrieveObjectsOnFloor();

    /**
     * @return The list of all registered spell listeners
     * @see SpellListener
     */
    Collection<? extends SpellListener> getSpellListeners();
}