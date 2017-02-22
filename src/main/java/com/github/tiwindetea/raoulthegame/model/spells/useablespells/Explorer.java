//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.model.spells.useablespells;

import com.github.tiwindetea.raoulthegame.events.game.spells.SpellCooldownUpdateEvent;
import com.github.tiwindetea.raoulthegame.events.game.spells.SpellCreationEvent;
import com.github.tiwindetea.raoulthegame.events.game.spells.SpellDeletionEvent;
import com.github.tiwindetea.raoulthegame.model.livings.LivingThing;
import com.github.tiwindetea.raoulthegame.model.livings.Player;
import com.github.tiwindetea.raoulthegame.model.space.Direction;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.github.tiwindetea.raoulthegame.model.spells.Spell;
import com.github.tiwindetea.raoulthegame.view.entities.LivingEntityType;
import com.github.tiwindetea.raoulthegame.view.entities.SpellType;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Lucas Lazare
 */
public class Explorer extends Spell<Player> {

	private static final double MANA_COST = 5;

	private Vector2i position;
	private int explorationRange = 0;
	private long ghostId;
	private boolean castedOnLastFloor = false;

	public Explorer(Player owner) {
		super(owner, owner.getSpells().size());
		this.description = "Explorer (active).\n" +
				"Summons a ghost spirit that explores the path to the " +
				"next floor for you." +
				"Cost: " + MANA_COST + " mana.";
		fire(new SpellCreationEvent(
				owner.getNumber(),
				this.id,
				SpellType.EXPLORER,
				0,
				this.description
		));
	}

	@Override
	public boolean isAOE() {
		return false;
	}

	@Override
	public boolean isPassive() {
		return false;
	}

	@Override
	public Vector2i getSpellSource() {
		return null;
	}

	@Override
	public double ownerDamaged(@Nullable LivingThing source, double damages) {
		return 0;
	}

	@Override
	public double ownerAttacking(@NotNull LivingThing target) {
		return 0;
	}

	@Override
	public void update(Collection<LivingThing> targets) {
		if (this.position != null) {
			Vector2i stairsPosition = spellsMap.getStairsDownPosition();
			if (!this.position.equals(stairsPosition)) {
				this.position = spellsMap.getPath(this.position, stairsPosition, true, Collections.emptyList()).peek();
				controller.moveGhostLivingEntity(this.ghostId, this.position);
			} else {
				controller.deleteGhostLivingEntity(this.ghostId);
				this.position = null;
			}
		}
	}

	@Override
	public void nextOwnerLevel() {
	}

	@Override
	public void nextSpellLevel() {
		++this.explorationRange;
		if (this.position != null) {
			controller.deleteGhostLivingEntity(this.ghostId);
			makeGhost();
			Player owner = getOwner();
			if (owner != null) {
				fire(new SpellCooldownUpdateEvent(
						owner.getNumber(),
						this.id,
						1,
						0
				));
			}
		}
	}

	@Override
	public boolean cast(Collection<LivingThing> targets, Vector2i sourcePosition) {
		Player owner = getOwner();
		if (owner != null && this.position == null) {
			if (owner.useMana(MANA_COST)) {
				this.position = owner.getPosition();
				makeGhost();
				this.castedOnLastFloor = true;
				fire(new SpellCooldownUpdateEvent(
						owner.getNumber(),
						this.id,
						1,
						1
				));
				return true;
			}
		}
		return false;
	}

	@Override
	public void nextFloor() {
		if (this.castedOnLastFloor && this.explorationRange == 2) {
			LivingThing owner = getOwner();
			if (owner != null) {
				this.position = owner.getPosition();
				makeGhost();
			}
		} else {
			this.position = null;
		}
	}

	@Override
	protected void forgotten() {
		Player owner = getOwner();
		if (owner != null) {
			fire(new SpellDeletionEvent(
					owner.getNumber(),
					this.id
			));
		}
	}

	private void makeGhost() {
		this.ghostId = controller.createGhostEntity(
				LivingEntityType.GHOST
				, this.position
				, "Basically a living ward.\n"
				, Direction.random()
				, 3
				, this.explorationRange
		);
	}
}
