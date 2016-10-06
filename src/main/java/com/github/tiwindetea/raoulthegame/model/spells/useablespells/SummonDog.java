package com.github.tiwindetea.raoulthegame.model.spells.useablespells;

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
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 * Created by maliafo on 9/28/16.
 */
public class SummonDog extends Spell {

    private Pet dog;
    private int level = 1;
    private double manaConsumption = 10.0;
    private static final double MANA_CONSUMPTION_PER_LEVEL = 2.0;

    public SummonDog(LivingThing owner) {
        super(owner, SpellType.SUMMON_DOG);
        this.dog = new Pet(owner, "Camembert.\nCamembert is binded to " + owner.getName() + ".", SummonDog.this.level, 100, 10, 1, owner.getPosition().copy()) {

            private Vector2i requestedPath = new Vector2i(0, 0);
            private boolean isGoingToOwner = false;
            private boolean isAttacking = false;

            @Override
            public void levelUp() {
                this.attackPower += 7;
                this.defensePower += 0.5;
                this.maxHitPoints += 5;
                this.hitPoints = this.maxHitPoints;
            }

            @Override
            public void ownerDamaged(@Nullable LivingThing source) {
                this.hitPoints += 0.5;
            }

            @Override
            public void ownerAttacking(@NotNull LivingThing target) {
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
                if (this.hitPoints < this.maxHitPoints - 1.0625) {
                    this.damage(-1, null);
                }
                LivingThing owner = super.getOwner();
                this.requestedAttack = null;
                if (owner != null) {
                    Stack<Vector2i> path = Spell.spellsMap.getPath(this.position, owner.getPosition(), true, all);
                    if (path != null) {
                        if (this.isGoingToOwner && path.size() > 3) {
                            this.requestedPath = path.peek();
                            this.isAttacking = false;
                        } else {
                            if (path.size() < 10 && new Random().nextInt() % 4 != 0 || path.size() < 4 || this.isAttacking) {
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
                System.out.println("Targeting");
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
                System.out.println("this.requestedAttack = " + this.requestedAttack);

                if (this.requestedAttack != null) {
                    this.isAttacking = true;
                    if (this.requestedAttack.getPosition().linearDistance(this.position) > 1) {
                        this.requestedPath = Spell.spellsMap.getPath(this.position, this.requestedAttack.getPosition(), true, null).peek();
                    }
                }
            }
        };
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
    public double ownerAttacking(@NotNull LivingThing target) {
        this.dog.ownerAttacking(target);
        return 0;
    }

    @Override
    public void update(Collection<LivingThing> targets) {
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
            }

        }
    }

    @Override
    public void nextOwnerLevel() {
    }

    @Override
    public void nextSpellLevel() {
        ++this.level;
        this.dog.levelUp();
        this.manaConsumption += MANA_CONSUMPTION_PER_LEVEL;
    }

    @Override
    public boolean cast(Collection<LivingThing> targets, Vector2i sourcePosition) {
        LivingThing owner = super.getOwner();
        if (owner != null && owner.getType() == LivingThingType.PLAYER) {
            Player powner = (Player) owner;
            if (powner.useMana(this.manaConsumption)) {
                Spell.controller.createEntity(this.dog, LivingEntityType.DOG, null);
                Spell.controller.shareLosWith(this.dog.getId(), 3);
                return true;
            }
        }
        return false;
    }
}
