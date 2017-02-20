package com.github.tiwindetea.raoulthegame.model.spells.useablespells;

import com.github.tiwindetea.raoulthegame.model.livings.LivingThing;
import com.github.tiwindetea.raoulthegame.model.livings.Player;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.github.tiwindetea.raoulthegame.model.spells.Spell;
import com.github.tiwindetea.raoulthegame.view.entities.SpellType;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.Collection;

/**
 * Created by Maxime on 20/02/2017.
 */
public class Teleport extends Spell<Player> {

	private static final int BASE_COOLDOWN = 200; //(turns)
	private static final double BASE_MANA_COST = 5;

	private int baseCooldown = BASE_COOLDOWN;
	private int cooldown = 0;
	private double manaCost = BASE_MANA_COST;
	private boolean oddLevel = true;

	public Teleport(Player owner) {
		super(owner, SpellType.TELEPORT);
		range = owner.getLos();
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
		cooldown = 0;
	}

	@Override
	public void nextSpellLevel() {
		if(oddLevel){
			baseCooldown = Math.max(0, baseCooldown - 20);
		}
		else{
			manaCost = Math.max(1, manaCost - 0.6);
		}
		oddLevel = !oddLevel;
		updateDescription();
	}

	@Override
	public boolean cast(Collection<LivingThing> targets, Vector2i sourcePosition) {
		Player owner = getOwner();
		if(owner != null && owner.useMana(manaCost)) {
			cooldown = baseCooldown;
			owner.setPosition(sourcePosition);
			return true;
		}
		return false;
	}

	@Override
	public void nextFloor() {

	}

	@Override
	public void forgotten() {

	}

	private void updateDescription(){
		description = "Teleport (active).\n" +
		  "Teleport the player to the target location.\n" +
		  "\tMana cost: " + DECIMAL.format(manaCost) + "\n" +
		  "\tCooldown: " + baseCooldown;
	}
}
