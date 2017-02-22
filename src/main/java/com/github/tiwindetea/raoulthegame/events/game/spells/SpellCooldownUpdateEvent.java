package com.github.tiwindetea.raoulthegame.events.game.spells;

/**
 * The type SpellCooldownUpdateEvent.
 *
 * @author Maxime PINARD
 */
public class SpellCooldownUpdateEvent extends SpellEvent {

	private int baseCooldown;
	private int cooldown;

	/**
	 * Instantiates a new SpellCooldownUpdateEvent.
	 *
	 * @param playerNumber the player number
	 * @param spellNumber  the spell number
	 * @param baseCooldown the base cooldown
	 * @param cooldown     the cooldown
	 */
	public SpellCooldownUpdateEvent(int playerNumber, int spellNumber, int baseCooldown, int cooldown) {
		super(playerNumber, spellNumber);
		this.baseCooldown = baseCooldown;
		this.cooldown = cooldown;
	}

	@Override
	public SpellEventType getSubType() {
		return SpellEventType.SPELL_COOLDOWN_UPDATE_EVENT;
	}

	/**
	 * Gets base cooldown.
	 *
	 * @return the base cooldown
	 */
	public int getBaseCooldown() {
		return this.baseCooldown;
	}

	/**
	 * Gets cooldown.
	 *
	 * @return the cooldown
	 */
	public int getCooldown() {
		return this.cooldown;
	}

}
