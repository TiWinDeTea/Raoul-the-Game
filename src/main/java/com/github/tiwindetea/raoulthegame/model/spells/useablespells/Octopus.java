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
import com.github.tiwindetea.raoulthegame.model.livings.LivingThingType;
import com.github.tiwindetea.raoulthegame.model.livings.Mob;
import com.github.tiwindetea.raoulthegame.model.livings.Pet;
import com.github.tiwindetea.raoulthegame.model.livings.Player;
import com.github.tiwindetea.raoulthegame.model.space.Direction;
import com.github.tiwindetea.raoulthegame.model.space.Tile;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.github.tiwindetea.raoulthegame.model.spells.Spell;
import com.github.tiwindetea.raoulthegame.view.entities.LivingEntityType;
import com.github.tiwindetea.raoulthegame.view.entities.SpellType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class Octopus extends Spell<LivingThing> {

	private class Octo extends Pet {


		private Vector2i requestedPath = new Vector2i(0, 0);
		private boolean isGoingToOwner = false;
		private boolean isAttacking = false;

		public Octo(LivingThing owner, String description, int level, int maxHp, int ap, int def, Vector2i pos) {
			super(owner, description, level, maxHp, ap, def, pos);
			super.hitPoints = -100;
		}

		public void attackThem() {
			this.isGoingToOwner = false;
			this.isAttacking = true;
		}

		@Override
		public void levelUp() {
			++this.level;
			this.attackPower += Math.max(this.attackPower / 5, 1);
			this.defensePower += Math.max(this.defensePower / 5, 0.5);
			this.maxHitPoints += Math.max(this.maxHitPoints / 10, 5);
			this.hitPoints = this.maxHitPoints;
		}

		@Override
		public void ownerDamaged(@Nullable LivingThing source) {
			this.hitPoints += 0.5;
		}

		@Override
		public void ownerAttacking(@Nonnull LivingThing target) {
		}

		@Override
		public Vector2i getRequestedMove() {
			if(this.requestedPath != null) {
				return this.requestedPath;
			}
			else {
				return this.position;
			}
		}

		@Override
		public void live(List<Mob> mobs, Collection<Player> players, Collection<LivingThing> all) {
			if(this.hitPoints < this.maxHitPoints - 4.0625) {
				this.damage(-2, null);
			}
			LivingThing owner = super.getOwner();
			this.requestedAttack = null;
			if(owner != null) {
				Stack<Vector2i> path = Spell.spellsMap.getPath(this.position, owner.getPosition(), true, all);
				if(path != null) {
					if(this.isGoingToOwner && path.size() > 2) {
						this.requestedPath = path.peek();
					}
					else {
						if(path.size() > 10 && new Random().nextInt() % 2 != 0 || path.size() < 6 && this.isAttacking || path.size() < 3) {
							this.isGoingToOwner = false;
							target(mobs, players, all, owner);
						}
						else {
							this.isAttacking = false;
							this.isGoingToOwner = true;
							this.requestedPath = path.peek();
						}
					}
				}
				else {
					target(mobs, players, all, owner);
				}
			}
		}

		private void target(List<Mob> mobs, Collection<Player> players, Collection<LivingThing> all, LivingThing owner) {
			if(new Random().nextBoolean()) {
				this.requestedPath = this.position.copy().add(Direction.random());
			}
			else {
				this.requestedPath = this.position;
			}

			ArrayList<LivingThing> targets;
			LivingThingType ownerType = owner.getType();
			if(ownerType == LivingThingType.MOB && players != null) {
				targets = new ArrayList<>(players);
			}
			else if(ownerType == LivingThingType.PLAYER && mobs != null) {
				targets = new ArrayList<>(mobs);
			}
			else {
				targets = new ArrayList<>();
			}

			targets.forEach(ptarget -> {
				int distance = ptarget.getPosition().linearDistance(this.position);
				if(distance < 5 && (this.requestedAttack == null || distance < this.requestedAttack.getPosition().linearDistance(this.position))) {
					this.requestedAttack = ptarget;
				}
			});

			if(this.requestedAttack != null) {
				this.isAttacking = true;
				if(this.requestedAttack.getPosition().linearDistance(this.position) > 1) {
					this.requestedPath = Spell.spellsMap.getPath(this.position, this.requestedAttack.getPosition(), true, null).peek();
				}
			}
		}

		public void kill() {
			this.damage(99999999d, null);
		}

		public void revive() {
			this.hitPoints = this.maxHitPoints;
		}

		@Override
		public void damage(double damages, @Nullable LivingThing source) {
			super.damage(damages, source);
			if(!this.isAlive()) {
				if(Octopus.this.pid != -1) {
					fire(new SpellCooldownUpdateEvent(
					  Octopus.this.pid,
					  Octopus.this.id,
					  1,
					  0
					));
				}
			}
		}
	}

	private Octo octo;
	private int level = 1;
	private double manaConsumption = 10.0;
	private static final double MANA_CONSUMPTION_PER_LEVEL = 2.0;

	private final int pid;

	public Octopus(LivingThing owner) {
		super(owner, Spell.firstNull(owner.getSpells()));
		this.octo = new Octo(owner, "Octopoulpe.\nOctopoulpe is  " + owner.getName() + ".", Octopus.this.level, 50, 30, 3, owner.getPosition().copy());
		if(LivingThingType.PLAYER.equals(owner.getType())) {
			this.pid = ((Player) owner).getNumber();
			updateDescription();
			fire(new SpellCreationEvent(
			  this.pid,
			  this.id,
			  getSpellType(),
			  1,
			  this.description
			));
		}
		else {
			this.pid = -1;
		}
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
	public Vector2i getSpellSource() {
		return this.octo.getPosition();
	}

	@Override
	public double ownerDamaged(@Nullable LivingThing source, double damages) {
		this.octo.ownerDamaged(source);
		return 0;
	}

	@Override
	public double ownerAttacking(@Nonnull LivingThing target) {
		this.octo.ownerAttacking(target);
		return 0;
	}

	@Override
	public void update(Collection<LivingThing> targets) {
		if(this.octo.isAlive()) {
			this.octo.live(Spell.controller.retrieveMobs(), Spell.controller.retrievePlayers(), targets);
			LivingThing target = this.octo.getRequestedAttack();
			if(target != null && target.getPosition().linearDistance(this.octo.getPosition()) < 2) {
				target.damage(this.octo.getAttackPower(), this.octo);
			}
			else {
				Vector2i pos = this.octo.getRequestedMove();
				Tile tile = spellsMap.getTile(pos);
				if(tile == Tile.CLOSED_DOOR) {
					spellsMap.triggerTile(pos);
				}
				else if(!Tile.isObstructed(tile) && !Spell.controller.retrieveLivingSpells().contains(new Mob(pos)) && !Spell.controller.retrieveMobs().contains(new Mob(pos))) {
					this.octo.setPosition(pos);
				}
				else {
					this.octo.attackThem();
				}
			}
		}
	}

	@Override
	public void nextOwnerLevel() {
	}

	@Override
	public void spellUpgraded() {
		++this.level;
		this.octo.levelUp();
		this.manaConsumption += MANA_CONSUMPTION_PER_LEVEL;
	}

	@Override
	public boolean cast(Collection<LivingThing> targets, Vector2i sourcePosition) {
		LivingThing owner = super.getOwner();
		if(owner != null && owner.getType() == LivingThingType.PLAYER) {
			Player powner = (Player) owner;
			if(!this.octo.isAlive()) {
				if(powner.useMana(this.manaConsumption)) {
					this.octo.setPosition(new Vector2i(0, 0));
					Spell.controller.createEntity(this.octo, LivingEntityType.OCTOPUS, null);
					Spell.controller.shareLosWith(this.octo.getId(), 3);
					this.octo.setPosition(owner.getPosition());
					this.octo.revive();
					if(this.pid != -1) {
						fire(new SpellCooldownUpdateEvent(
						  this.pid,
						  this.id,
						  1,
						  1
						));
					}
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void nextFloor() {
		LivingThing owner = getOwner();
		if(this.octo.isAlive() && owner != null) {
			this.octo.setPosition(new Vector2i(0, 0));
			Spell.controller.createEntity(this.octo, LivingEntityType.OCTOPUS, null);
			Spell.controller.shareLosWith(this.octo.getId(), 3);
			this.octo.setPosition(owner.getPosition());
		}
	}

	@Override
	public void forgotten() {
		this.octo.kill();
		if(this.pid != -1) {
			fire(new SpellDeletionEvent(
			  this.pid,
			  this.id
			));
		}
	}

	@Override
	public SpellType getSpellType() {
		return SpellType.OCTOPUS;
	}

	private void updateDescription() {
		this.description = "Summon octo (active).\n" +
		  "Summons a octo to fight with you.\n" +
		  "Cost: " + this.manaConsumption + " mana";
	}
}
