package com.github.tiwindetea.raoulthegame.events.game.spells;

/**
 * The type SpellDescriptionUpdateEvent.
 *
 * @author Maxime PINARD
 */
public class SpellDescriptionUpdateEvent extends SpellEvent {

	private String description;

	/**
	 * Instantiates a new SpellDescriptionUpdateEvent.
	 *
	 * @param playerNumber the player number
	 * @param spellNumber  the spell number
	 * @param description  the description
	 */
	public SpellDescriptionUpdateEvent(int playerNumber, int spellNumber, String description) {
		super(playerNumber, spellNumber);
		this.description = description;
	}

	@Override
	public SpellEventType getSubType() {
		return SpellEventType.SPELL_DESCRIPTION_UPDATE_EVENT;
	}

	/**
	 * Gets description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

}
