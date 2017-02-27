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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * The type Grenade.
 *
 * @author Maxime PINARD
 */
public class Grenade extends Spell<Player> {

	private static final double BASE_DAMAGES = 10;
	private static final int BASE_COOLDOWN = 20;

	private double damages = BASE_DAMAGES;
	private int baseCooldown = BASE_COOLDOWN;
	private int cooldown = 0;
	private double manaCost = 5;
	private int explosionRadius = 5;

	/**
	 * Instantiates a new Grenade.
	 *
	 * @param owner the owner
	 */
	public Grenade(Player owner) {
		super(owner, Spell.firstNull(owner.getSpells()));
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
	public boolean isAOE() {
		return true;
	}

	@Override
	public boolean isPassive() {
		return false;
	}

	@Override
	public boolean needSourcePosition() {
		return true;
	}

	@Override
	public Vector2i getSpellSource() {
		// todo
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
		if(this.cooldown > 0) {
			--this.cooldown;
		}

		Player owner = getOwner();
		if(owner != null) {
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

	}

	@Override
	public void spellUpgraded() {
		this.damages += 10;
		this.baseCooldown = Math.max(5, this.baseCooldown - 10);
		updateDescription();
		Player owner = getOwner();
		if(owner != null) {
			fire(new SpellDescriptionUpdateEvent(
			  owner.getNumber(),
			  this.id,
			  this.description
			));
		}
	}

	@Override
	public boolean cast(Collection<LivingThing> t, Vector2i sourcePosition) {
		if(this.cooldown == 0) {
			Player owner = getOwner();
			if(owner != null && owner.useMana(this.manaCost)) {
				Collection<LivingThing> targets = controller.retrieveMobs().stream().filter(mob -> mob.getPosition().squaredDistance(sourcePosition) < this.explosionRadius * this.explosionRadius).collect(Collectors.toList());
				for(LivingThing target : targets) {
					target.damage(this.damages, owner);
				}
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
		if(owner != null) {
			fire(new SpellDeletionEvent(
			  owner.getNumber(),
			  this.id
			));
		}
	}

	@Override
	public SpellType getSpellType() {
		return SpellType.GRENADE;
	}

	private void updateDescription() {
		this.description = "Grenade (active).\n\n" +
		  "Throw a grenade at your ennemy dealing " + DECIMAL.format(this.damages) + " damages in a radius of " + this.explosionRadius + " tiles.\n\n" +
		  "Cost: " + this.manaCost + " mana.";
	}
}
