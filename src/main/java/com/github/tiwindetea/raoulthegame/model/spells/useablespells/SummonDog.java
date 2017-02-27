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

/**
 * Created by maliafo on 9/28/16.
 */
public class SummonDog extends Spell<LivingThing> {

    private class Dog extends Pet {


        private Vector2i requestedPath = new Vector2i(0, 0);
        private boolean isGoingToOwner = false;
        private boolean isAttacking = false;

        public Dog(LivingThing owner, String description, int level, int maxHp, int ap, int def, Vector2i pos) {
            super(owner, description, level, maxHp, ap, def, pos);
            this.hitPoints = 0;
        }

        public void attackThem() {
            this.isGoingToOwner = false;
            this.isAttacking = true;
        }

        @Override
        public void levelUp() {
            ++this.level;
            this.attackPower += Math.max(this.attackPower / 10, 1);
            this.defensePower += Math.max(this.defensePower / 10, 0.5);
            this.maxHitPoints += Math.max(this.maxHitPoints / 2, 5);
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
            if (this.requestedPath != null) {
                return this.requestedPath;
            } else {
                return this.position;
            }
        }

        @Override
        public void live(List<Mob> mobs, Collection<Player> players, Collection<LivingThing> all) {
            if (this.hitPoints < this.maxHitPoints - 4.0625) {
                this.damage(-2, null);
            }
            LivingThing owner = super.getOwner();
            this.requestedAttack = null;
            if (owner != null) {
                Stack<Vector2i> path = Spell.spellsMap.getPath(this.position, owner.getPosition(), true, all);
                if (path != null) {
                    if (this.isGoingToOwner && path.size() > 5) {
                        this.requestedPath = path.peek();
                    } else {
                        if (path.size() > 15 && new Random().nextInt() % 2 != 0 || path.size() < 10 && this.isAttacking || path.size() < 5) {
                            this.isGoingToOwner = false;
                            target(mobs, players, all, owner);
                        } else {
                            this.isAttacking = false;
                            this.isGoingToOwner = true;
                            this.requestedPath = path.peek();
                        }
                    }
                } else {
                    target(mobs, players, all, owner);
                }
            }
        }

        private void target(List<Mob> mobs, Collection<Player> players, Collection<LivingThing> all, LivingThing owner) {
            if (new Random().nextBoolean()) {
                this.requestedPath = this.position.copy().add(Direction.random());
            } else {
                this.requestedPath = this.position;
            }

            ArrayList<LivingThing> targets;
            LivingThingType ownerType = owner.getType();
            if (ownerType == LivingThingType.MOB && players != null) {
                targets = new ArrayList<>(players);
            } else if (ownerType == LivingThingType.PLAYER && mobs != null) {
                targets = new ArrayList<>(mobs);
            } else {
                targets = new ArrayList<>();
            }

            targets.forEach(ptarget -> {
                int distance = ptarget.getPosition().linearDistance(this.position);
                if (distance < 5 && (this.requestedAttack == null || distance < this.requestedAttack.getPosition().linearDistance(this.position))) {
                    this.requestedAttack = ptarget;
                }
            });

            if (this.requestedAttack != null) {
                this.isAttacking = true;
                if (this.requestedAttack.getPosition().linearDistance(this.position) > 1) {
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
            if (!this.isAlive()) {
                if (SummonDog.this.pid != -1) {
                    fire(new SpellCooldownUpdateEvent(
                            SummonDog.this.pid,
                            SummonDog.this.id,
                            1,
                            0
                    ));
                }
            }
        }
    }

    private Dog dog;
    private int level = 1;
    private double manaConsumption = 10.0;
    private static final double MANA_CONSUMPTION_PER_LEVEL = 2.0;

    private final int pid;

    public SummonDog(LivingThing owner) {
        super(owner, Spell.firstNull(owner.getSpells()));
        this.dog = new Dog(owner, "Camembert.\nCamembert is binded to " + owner.getName() + ".", SummonDog.this.level, 200, 10, 1, owner.getPosition().copy());
        if (LivingThingType.PLAYER.equals(owner.getType())) {
            this.pid = ((Player) owner).getNumber();
            updateDescription();
            fire(new SpellCreationEvent(
                    this.pid,
                    this.id,
                    getSpellType(),
                    1,
                    this.description
            ));
        } else {
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
        return this.dog.getPosition();
    }

    @Override
    public double ownerDamaged(@Nullable LivingThing source, double damages) {
        this.dog.ownerDamaged(source);
        return 0;
    }

    @Override
    public double ownerAttacking(@Nonnull LivingThing target) {
        this.dog.ownerAttacking(target);
        return 0;
    }

    @Override
    public void update(Collection<LivingThing> targets) {
        if (this.dog.isAlive()) {
            this.dog.live(Spell.controller.retrieveMobs(), Spell.controller.retrievePlayers(), targets);
            LivingThing target = this.dog.getRequestedAttack();
            if (target != null && target.getPosition().linearDistance(this.dog.getPosition()) < 2) {
                target.damage(this.dog.getAttackPower(), this.dog);
            } else {
                Vector2i pos = this.dog.getRequestedMove();
                Tile tile = spellsMap.getTile(pos);
                if (tile == Tile.CLOSED_DOOR) {
                    spellsMap.triggerTile(pos);
                } else if (!Tile.isObstructed(tile) && !Spell.controller.retrieveLivingSpells().contains(new Mob(pos)) && !Spell.controller.retrieveMobs().contains(new Mob(pos))) {
                    this.dog.setPosition(pos);
                } else {
                    this.dog.attackThem();
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
        this.dog.levelUp();
        this.manaConsumption += MANA_CONSUMPTION_PER_LEVEL;
    }

    @Override
    public boolean cast(Collection<LivingThing> targets, Vector2i sourcePosition) {

        LivingThing owner = super.getOwner();
        if (owner != null && owner.getType() == LivingThingType.PLAYER) {
            Player powner = (Player) owner;
            if (!this.dog.isAlive()) {
                if (powner.useMana(this.manaConsumption)) {
                    this.dog.setPosition(new Vector2i(0, 0));
                    Spell.controller.createEntity(this.dog, LivingEntityType.DOG, null);
                    Spell.controller.shareLosWith(this.dog.getId(), 3);
                    this.dog.setPosition(owner.getPosition());
                    this.dog.revive();
                    if (this.pid != -1) {
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
        if (this.dog.isAlive() && owner != null) {
            this.dog.setPosition(new Vector2i(0, 0));
            Spell.controller.createEntity(this.dog, LivingEntityType.DOG, null);
            Spell.controller.shareLosWith(this.dog.getId(), 3);
            this.dog.setPosition(owner.getPosition());
        }
    }

    @Override
    public void forgotten() {
        this.dog.kill();
        if (this.pid != -1) {
            fire(new SpellDeletionEvent(
                    this.pid,
                    this.id
            ));
        }
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.SUMMON_DOG;
    }

    private void updateDescription() {
        this.description = "Summon dog (active).\n" +
                "Summons a dog to fight with you.\n" +
                "Cost: " + this.manaConsumption + " mana";
    }
}
