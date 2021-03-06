//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.model.spells.passives;

import com.github.tiwindetea.raoulthegame.events.game.spells.SpellCreationEvent;
import com.github.tiwindetea.raoulthegame.events.game.spells.SpellDeletionEvent;
import com.github.tiwindetea.raoulthegame.events.game.spells.SpellDescriptionUpdateEvent;
import com.github.tiwindetea.raoulthegame.model.livings.LivingThing;
import com.github.tiwindetea.raoulthegame.model.livings.LivingThingType;
import com.github.tiwindetea.raoulthegame.model.livings.Player;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.github.tiwindetea.raoulthegame.model.spells.Spell;
import com.github.tiwindetea.raoulthegame.view.entities.SpellType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * todo class
 */
public class Thorn extends Spell<LivingThing> {

    public final int pid;

    public double percentage = 5;

    public Thorn(LivingThing owner) {
        super(owner, Spell.firstNull(owner.getSpells()));

        if (LivingThingType.PLAYER.equals(owner.getType())) {
            updateDescription();
            this.pid = ((Player) owner).getNumber();
            fire(new SpellCreationEvent(
              this.pid,
              this.id,
              getSpellType(),
              0,
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
        if (source != null) {
            source.damage(damages * this.percentage / 100, null);
        }
        return damages * this.percentage / 100;
    }

    @Override
    public double ownerAttacking(@Nonnull LivingThing target) {
        return 0;
    }

    @Override
    public void update(Collection<LivingThing> targets) {

    }

    @Override
    public void nextOwnerLevel() {

    }

    @Override
    public void spellUpgraded() {
        this.percentage += 1;
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
    protected void forgotten() {
        if (this.pid != -1) {
            fire(new SpellDeletionEvent(
              this.pid,
              this.id
            ));
        }
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.THORN;
    }

    private void updateDescription() {
        this.description = "Thorn (passive).\n" +
          "Returns " + this.percentage + "% " +
          "of the incomming damages to the attacker";
    }
}
