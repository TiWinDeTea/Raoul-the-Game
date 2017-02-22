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
import com.github.tiwindetea.raoulthegame.events.game.spells.SpellDescriptionUpdateEvent;
import com.github.tiwindetea.raoulthegame.model.livings.LivingThing;
import com.github.tiwindetea.raoulthegame.model.livings.Player;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.github.tiwindetea.raoulthegame.model.spells.Spell;
import com.github.tiwindetea.raoulthegame.view.entities.SpellType;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.Collection;

/**
 * Created by Maxime on 20/02/2017.
 */
public class Teleport extends Spell<Player> {

	private static final int BASE_COOLDOWN = 200; //(turns)
	private static final double BASE_MANA_COST = 5;

	private int baseCooldown = BASE_COOLDOWN;
	private int cooldown = 0;
	private double manaCost = BASE_MANA_COST;
	private boolean oddLevel = true;

	public Teleport(Player owner) {
		super(owner, owner.getSpells().size());
		this.range = owner.getLos();
		updateDescription();
		fire(new SpellCreationEvent(
				owner.getNumber(),
				this.id,
				getSpellType(),
				this.baseCooldown,
				this.description
		));
	}

	@Override
	public boolean needSourcePosition() {
		return true;
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
		this.cooldown -= this.cooldown > 0 ? 1 : 0;
		Player owner = getOwner();
		if (owner != null) {
			fire(new SpellCooldownUpdateEvent(
					owner.getNumber(),
					this.id,
					this.baseCooldown,
					this.cooldown
			));
		}
	}

	@Override
	public void nextOwnerLevel() {
		this.cooldown = 0;
	}

	@Override
	public void spellUpgraded() {
		if (this.oddLevel) {
			this.baseCooldown = Math.max(0, this.baseCooldown - 20);
		}
		else{
			this.manaCost = Math.max(1, this.manaCost - 0.6);
		}
		this.oddLevel = !this.oddLevel;
		updateDescription();
		fire(new SpellDescriptionUpdateEvent(
				getOwner().getNumber(),
				this.id,
				this.description
		));
	}

	@Override
	public boolean cast(Collection<LivingThing> targets, Vector2i sourcePosition) {
		if (this.cooldown == 0 && spellsMap.explored[sourcePosition.x][sourcePosition.y]) {
			Player owner = getOwner();
			if (owner != null && owner.useMana(this.manaCost)) {
				owner.setPosition(sourcePosition);
				this.cooldown = this.baseCooldown;
				fire(new SpellCooldownUpdateEvent(
						owner.getNumber(),
						this.id,
						this.baseCooldown,
						this.cooldown
				));
				return true;
			}
		}
		return false;
	}

	@Override
	public void nextFloor() {

	}

	@Override
	public void forgotten() {
		Player owner = getOwner();
		if (owner != null) {
			fire(new SpellDeletionEvent(owner.getNumber(), this.id));
		}
	}

	@Override
	public SpellType getSpellType() {
		return SpellType.TELEPORT;
	}

	private void updateDescription(){
		this.description = "Teleport (active).\n\n" +
		  "Teleport the player to the target location.\n" +
				"(Anywhere where you have explored).\n" +
				"\tMana cost: " + DECIMAL.format(this.manaCost) + "\n" +
				"\tCooldown: " + this.baseCooldown;
	}
}
