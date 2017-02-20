package com.github.tiwindetea.raoulthegame.model.spells.passives;

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
public class IronWill extends Spell {

	private static final double BASE_ARMOR = 2.5;

	private double armor = BASE_ARMOR;

	public IronWill(LivingThing owner) {
		super(owner, SpellType.IRON_WILL);
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
		return -armor;
	}

	@Override
	public double ownerAttacking(@NotNull LivingThing target) {
		return 0;
	}

	@Override
	public void update(Collection<LivingThing> targets) {

	}

	@Override
	public void nextOwnerLevel() {

	}

	@Override
	public void nextSpellLevel() {
		++armor;
		updateDescription();
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

	}

	private void updateDescription(){
		description = "Iron Will (passive).\n" +
		  "Increase the armor by " + DECIMAL.format(armor);
	}
}
