//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.model.spells.useablespells;

import com.github.tiwindetea.raoulthegame.events.game.spells.SpellCreationEvent;
import com.github.tiwindetea.raoulthegame.events.game.spells.SpellDeletionEvent;
import com.github.tiwindetea.raoulthegame.events.game.spells.SpellDescriptionUpdateEvent;
import com.github.tiwindetea.raoulthegame.model.livings.LivingThing;
import com.github.tiwindetea.raoulthegame.model.livings.Player;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.github.tiwindetea.raoulthegame.model.spells.Spell;
import com.github.tiwindetea.raoulthegame.view.entities.SpellType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * The type Heal.
 *
 * @author Maxime PINARD
 */
public class Heal extends Spell<Player> {

	private static final double BASE_HEAL = 100;
	private static final int BASE_APPLY_TURNS = 10;

	private double manaCost = 10;
	private double heal = BASE_HEAL;
	private int applyTurns = BASE_APPLY_TURNS;
	private int remainingTurns = 0;


	/**
	 * Instantiates a new Heal.
	 *
	 * @param owner the owner
	 */
	public Heal(Player owner) {
		super(owner, owner.getSpells().size());
		updateDescription();
		fire(new SpellCreationEvent(
		  owner.getNumber(),
		  this.id,
		  getSpellType(),
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
	public double ownerAttacking(@Nonnull LivingThing target) {
		return 0;
	}

	@Override
	public void update(Collection<LivingThing> targets) {
		if (this.remainingTurns > 0) {
			getOwner().damage(-this.heal / this.applyTurns, null);
			--this.remainingTurns;
		}
	}

	@Override
	public void nextOwnerLevel() {

	}

	@Override
	public void spellUpgraded() {
		this.applyTurns -= 2;
		this.heal += 25;
		updateDescription();
		Player owner = getOwner();
		if (owner != null) {
			fire(new SpellDescriptionUpdateEvent(
					owner.getNumber(),
					this.id,
					this.description
			));
		}
	}

	@Override
	public boolean cast(Collection<LivingThing> targets, Vector2i sourcePosition) {
		Player owner = getOwner();
		if (owner != null && owner.useMana(this.manaCost)) {
			this.remainingTurns += this.applyTurns;
		}
		return true;
	}

	@Override
	public void nextFloor() {

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

	@Override
	public SpellType getSpellType() {
		return SpellType.HEAL;
	}

	private void updateDescription(){
		this.description = "Heal (active).\n" +
				"Heal " + this.heal + " hp in " + this.applyTurns + " turns";
	}
}
