package com.github.tiwindetea.raoulthegame.model.spells.useablespells;

import com.github.tiwindetea.raoulthegame.events.game.spells.SpellCooldownUpdateEvent;
import com.github.tiwindetea.raoulthegame.events.game.spells.SpellCreationEvent;
import com.github.tiwindetea.raoulthegame.events.game.spells.SpellDeletionEvent;
import com.github.tiwindetea.raoulthegame.events.game.spells.SpellDescriptionUpdateEvent;
import com.github.tiwindetea.raoulthegame.model.livings.LivingThing;
import com.github.tiwindetea.raoulthegame.model.livings.Player;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.github.tiwindetea.raoulthegame.model.spells.Spell;
import com.github.tiwindetea.raoulthegame.view.entities.SpellType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The type ScrollSpell.
 */
public class ScrollSpell extends Spell<Player> {

	private static final double BASE_DAMAGES = 3;
	private static final int BASE_COOLDOWN = 20;
	private static final int APPLY_TURNS = 10;

	private double damages = BASE_DAMAGES;
	private int baseCooldown = BASE_COOLDOWN;
	private int cooldown = 0;
	private double manaCost = 5;
	private int remainingTurns = 0;

	private Collection<LivingThing> targets;

	/**
	 * Instantiates a new ScrollSpell.
	 *
	 * @param owner the owner
	 */
	public ScrollSpell(Player owner) {
		super(owner, owner.getSpells().size());
		this.targetNumber = 1;
		updateDescription();
		fire(new SpellCreationEvent(
		  owner.getNumber(),
		  this.id,
		  getSpellType(),
		  this.baseCooldown,
		  this.description
		));
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
    public double ownerAttacking(@Nonnull LivingThing target) {
        return 0;
    }

    @Override
    public void update(Collection<LivingThing> targets) {

	    if(this.remainingTurns > 0 && this.targets != null) {
		    for(LivingThing target : this.targets) {
			    target.damage(this.damages / APPLY_TURNS, null);
		    }
		    --this.remainingTurns;
	    }

	    if(this.cooldown > 0) {
		    --this.cooldown;
	    }

	    Player owner = getOwner();
	    if(owner != null) {
		    fire(new SpellCooldownUpdateEvent(
		      owner.getNumber(),
		      this.id,
		      this.baseCooldown,
		      this.cooldown
		    ));
	    }
    }

    @Override
    public void nextOwnerLevel() {

    }

    @Override
    public void spellUpgraded() {
	    this.damages += 4;
	    this.baseCooldown = Math.max(25, this.baseCooldown - 10);
	    updateDescription();
	    Player owner = getOwner();
	    if(owner != null) {
		    fire(new SpellDescriptionUpdateEvent(
		      owner.getNumber(),
		      this.id,
		      this.description
		    ));
	    }
    }

    @Override
    public boolean cast(Collection<LivingThing> targets, Vector2i sourcePosition) {
	    if(this.cooldown == 0) {
		    if(!targets.isEmpty()) {
			    this.targets = new ArrayList<>(targets);
			    this.remainingTurns += APPLY_TURNS;
		    }
		    this.cooldown = this.baseCooldown;
		    fire(new SpellCooldownUpdateEvent(
		      getOwner().getNumber(),
		      this.id,
		      this.baseCooldown,
		      this.cooldown
		    ));
		    return true;
	    }
        return false;
    }

    @Override
    public void nextFloor() {

    }

    @Override
    protected void forgotten() {
	    Player owner = getOwner();
	    if(owner != null) {
		    fire(new SpellDeletionEvent(
		      owner.getNumber(),
		      this.id
		    ));
	    }
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.SCROLL_SPELL;
    }

	private void updateDescription() {
		this.description = "Scroll (active).\n" +
		  "Cast a spell on an enemy dealing " + this.damages + " damages.\n\n" +
		  "Cost: " + this.manaCost + " mana.";
	}

}
