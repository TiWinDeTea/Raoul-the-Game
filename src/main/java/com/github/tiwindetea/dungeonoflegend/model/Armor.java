package com.github.tiwindetea.dungeonoflegend.model;

/**
 * Created by maxime on 4/23/16.
 */
public class Armor implements StorableObject {

	private int defensePowerModifier;
	private int attackPowerModifier;
	private ArmorType type;

	public Armor(int defensePowerModifier, int attackPowerModifier, ArmorType type) {
		this.defensePowerModifier = defensePowerModifier;
		this.attackPowerModifier = attackPowerModifier;
		this.type = type;
	}

	public int getDefensePowerModifier() {
		return this.defensePowerModifier;
	}

	public int getAttackPowerModifier() {
		return this.attackPowerModifier;
	}

	public ArmorType getArmorType() {
		return this.type;
	}

	public StorableObjectType getType() {
		return StorableObjectType.ARMOR;
	}
}
