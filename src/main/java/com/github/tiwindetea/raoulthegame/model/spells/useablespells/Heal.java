package com.github.tiwindetea.raoulthegame.model.spells.useablespells;

import com.github.tiwindetea.raoulthegame.model.livings.LivingThing;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.github.tiwindetea.raoulthegame.model.spells.Spell;
import com.github.tiwindetea.raoulthegame.view.entities.SpellType;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.Collection;

/**
 * Created by Maxime on 20/02/2017.
 */
public class Heal extends Spell<LivingThing> {

	private static final double BASE_HEAL = 100;
	private static final int BASE_APPLY_TURNS = 10;

	private double heal = BASE_HEAL;
	private int applyTurns = BASE_APPLY_TURNS;
	private int remainingTurns = 0;

	public Heal(LivingThing owner) {
		super(owner, SpellType.HEAL);
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
		if(remainingTurns > 0){
			getOwner().damage(-heal / applyTurns, null);
			--remainingTurns;
		}
	}

	@Override
	public void nextOwnerLevel() {

	}

	@Override
	public void nextSpellLevel() {
		--applyTurns;
		heal += 25;
	}

	@Override
	public boolean cast(Collection<LivingThing> targets, Vector2i sourcePosition) {
		remainingTurns = applyTurns;
		return true;
	}

	@Override
	public void nextFloor() {

	}

	@Override
	public void forgotten() {

	}

	private void updateDescription(){
		description = "Heal (active).\n" +
		  "Heal " + heal + " hp in " + applyTurns + " turns";
	}
}
