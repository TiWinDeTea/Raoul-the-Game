//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.model.spells.passives;

import com.github.tiwindetea.raoulthegame.events.game.spells.SpellCooldownUpdateEvent;
import com.github.tiwindetea.raoulthegame.events.game.spells.SpellCreationEvent;
import com.github.tiwindetea.raoulthegame.events.game.spells.SpellDeletionEvent;
import com.github.tiwindetea.raoulthegame.events.game.spells.SpellDescriptionUpdateEvent;
import com.github.tiwindetea.raoulthegame.model.livings.LivingThing;
import com.github.tiwindetea.raoulthegame.model.livings.LivingThingType;
import com.github.tiwindetea.raoulthegame.model.livings.Player;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.github.tiwindetea.raoulthegame.model.spells.Spell;
import com.github.tiwindetea.raoulthegame.view.entities.SpellType;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.Collection;

/**
 * Created by Lucas on 01/09/2016.
 */
public class Regen extends Spell<LivingThing> {

    private int turn = 0;
    private double healQtt = 5;

    private static final int CD_TURNS = 10;

    private final int pid;

    public Regen(LivingThing owner) {
        super(owner, owner.getSpells().size());
        if (LivingThingType.PLAYER.equals(owner.getType())) {
            updateDescription();
            this.pid = ((Player) owner).getNumber();
            fire(new SpellCreationEvent(
                    this.pid,
                    this.id,
                    SpellType.REGEN,
                    CD_TURNS,
                    this.description
            ));
        } else {
            this.pid = -1;
        }
    }

    @Override
    public boolean isAOE() {
        return false;
    }

    @Override
    public boolean isPassive() {
        return true;
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
        ++this.turn;
        if (this.turn % CD_TURNS == 0) {
            LivingThing owner = super.getOwner();
            if (owner != null) {
                owner.damage(-this.healQtt, null);
            }
        }
        if (this.pid != -1) {
            fire(new SpellCooldownUpdateEvent(
                    this.pid,
                    this.id,
                    CD_TURNS - 1,
                    CD_TURNS - this.turn % CD_TURNS - 1
            ));
        }
    }

    @Override
    public void nextOwnerLevel() {
        this.turn = 1;
    }

    @Override
    public void nextSpellLevel() {
        this.healQtt += 5;
        if (this.pid != -1) {
            updateDescription();
            fire(new SpellDescriptionUpdateEvent(
                    this.pid,
                    this.id,
                    this.description
            ));
        }
    }

    @Override
    public boolean cast(Collection<LivingThing> targets, Vector2i sourcePosition) {
        return false;
    }

    @Override
    public void nextFloor() {

    }

    @Override
    public void forgotten() {
        if (this.pid != -1) {
            fire(new SpellDeletionEvent(
                    this.pid,
                    this.id
            ));
        }
    }

    private void updateDescription() {
        this.description = "Rejuvenate (passive).\n" +
                "Passively regenerate your health (" + this.healQtt + "hp per turn)";
    }
}