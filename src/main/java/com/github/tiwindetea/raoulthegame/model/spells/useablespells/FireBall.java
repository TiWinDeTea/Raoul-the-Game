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
 * Created by Maxime on 21/02/2017.
 */
public class FireBall extends Spell<Player> {

	private static final double BASE_DAMAGES = 3;
	private static final int BASE_COOLDOWN = 75;

	private double damages = BASE_DAMAGES;
	private int baseCooldown = BASE_COOLDOWN;
	private int cooldown = this.baseCooldown;
	private double manaCost = 5;

	public FireBall(Player owner) {
		super(owner, owner.getSpells().size());
		this.targetNumber = 1;
		updateDescription();
		fire(new SpellCreationEvent(
				owner.getNumber(),
				this.id,
				SpellType.FIRE_BALL,
				this.baseCooldown,
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

	}

	@Override
	public void nextSpellLevel() {
		this.damages += 1;
		this.baseCooldown = Math.max(25, this.baseCooldown - 10);
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
		if (this.cooldown == 0) {
			if(!targets.isEmpty()){
				Player owner = getOwner();
				for(LivingThing target : targets) {
					target.damage(this.damages + target.getDefensePower(), owner);
				}
				this.cooldown = this.baseCooldown;
				if (owner != null) {
					fire(new SpellCooldownUpdateEvent(
							owner.getNumber(),
							this.id,
							this.baseCooldown,
							this.cooldown
					));
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void nextFloor() {

	}

	@Override
	protected void forgotten() {
	}

	private void updateDescription() {
		this.description = "Fireball (active).\n" +
				"Throw a mighty fireball at your ennemy for " +
				DECIMAL.format(this.damages) + "true damages.\n" +
				"Cost: " + this.manaCost + " mana.";
	}
}
