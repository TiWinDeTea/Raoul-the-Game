//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.model.spells.useablespells;

import com.github.tiwindetea.raoulthegame.model.livings.LivingThing;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.github.tiwindetea.raoulthegame.model.spells.Spell;
import com.github.tiwindetea.raoulthegame.view.entities.SpellType;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.Collection;

/**
 * Created by Maxime on 21/02/2017.
 */
public class FireBall extends Spell<LivingThing> {

	private static final double BASE_DAMAGES = 50;
	private static final int BASE_COOLDOWN = 75;

	private double damages = BASE_DAMAGES;
	private int baseCooldown = BASE_COOLDOWN;
	private int cooldown = baseCooldown;

	public FireBall(LivingThing owner) {
		super(owner, SpellType.FIRE_BALL);
		targetNumber = 1;
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
		cooldown -= cooldown > 0 ? 1 : 0;
	}

	@Override
	public void nextOwnerLevel() {

	}

	@Override
	public void nextSpellLevel() {
		damages += 25;
		baseCooldown = Math.max(25, baseCooldown - 10);
	}

	@Override
	public boolean cast(Collection<LivingThing> targets, Vector2i sourcePosition) {
		if(cooldown == 0){
			if(!targets.isEmpty()){
				for(LivingThing target : targets) {
					target.damage(damages,getOwner());
				}
				cooldown = baseCooldown;
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

	}
}
