package com.github.tiwindetea.raoulthegame.model.spells.useablespells;

import com.github.tiwindetea.raoulthegame.model.livings.LivingThing;
import com.github.tiwindetea.raoulthegame.model.livings.Player;
import com.github.tiwindetea.raoulthegame.model.space.Direction;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.github.tiwindetea.raoulthegame.model.spells.Spell;
import com.github.tiwindetea.raoulthegame.view.entities.LivingEntityType;
import com.github.tiwindetea.raoulthegame.view.entities.SpellType;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Lucas Lazare
 */
public class Explorer extends Spell {

	private static final double MANA_COST = 5;

	private Vector2i position;
	private long id;
	private int explorationRange = 0;
	private boolean castedOnLastFloor = false;

	public Explorer(LivingThing owner) {
		super(owner, SpellType.EXPLORER);
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
		if (position != null) {
			Vector2i stairsPosition = spellsMap.getStairsDownPosition();
			if (!position.equals(stairsPosition)) {
				this.position = spellsMap.getPath(this.position, stairsPosition, true, Collections.emptyList()).peek();
				controller.moveGhostLivingEntity(id, this.position);
			} else {
				controller.deleteGhostLivingEntity(id);
				this.position = null;
			}
		}
	}

	@Override
	public void nextOwnerLevel() {
	}

	@Override
	public void nextSpellLevel() {
		++explorationRange;
		if (position != null) {
			controller.deleteGhostLivingEntity(id);
			makeGhost();
		}
	}

	@Override
	public boolean cast(Collection<LivingThing> targets, Vector2i sourcePosition) {
		LivingThing owner = getOwner();
		if (owner != null) {
			if (((Player) owner).useMana(MANA_COST)) {
				this.position = owner.getPosition();
				makeGhost();
				castedOnLastFloor = true;
				return true;
			}
		}
		return false;
	}

	@Override
	public void nextFloor() {
		if (castedOnLastFloor && explorationRange == 2) {
			LivingThing owner = getOwner();
			if (owner != null) {
				this.position = owner.getPosition();
				makeGhost();
			}
		} else {
			this.position = null;
		}
	}

	@Override
	public void forgotten() {

	}

	private void makeGhost() {
		id = controller.createGhostEntity(
				LivingEntityType.PEACEFUL_ECTOPLASMA
				, position
				, "Basically a living ward..."
				, Direction.random()
				, 3, explorationRange
		);
	}
}
