package com.github.tiwindetea.dungeonoflegend.model.events;

/**
 * Created by maxime on 5/5/16.
 */
public class PlayerInformationEvent extends PlayerEvent {
	public enum InformationType {
		HEALTH,
		MANA
	}

	public enum ValueType {
		ACTUAL,
		MAX
	}

	public InformationType informationType;
	public ValueType valueType;
	public int value;

	public PlayerInformationEvent(byte playerNumber, InformationType informationType, ValueType valueType, int value) {
		super(playerNumber);
		this.informationType = informationType;
		this.valueType = valueType;
		this.value = value;
	}
}
